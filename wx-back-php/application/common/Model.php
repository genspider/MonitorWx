<?php

class Model extends Yaf_Bootstrap_Abstract
{
    protected      $db;
    protected      $cache;
    protected      $_config;
    protected      $_session;
    protected      $_table;
    protected      $_autoTime;
    protected      $_updateTime;
    protected      $_createTime;
    protected      $_formatTime;
    protected      $_status;

    protected      $fields = [];
    protected      $where = [];

    private static $instance = array();

    public function __construct()
    {
        $this->cache    = $this->CACHE();
        $this->db       = $this->DB();
        $this->_config  = Yaf_Registry::get('config');
        $this->_session = Yaf_Session::getInstance();
    }

    protected function DB()
    {
        if(@self::$instance['db'] == null)
        {
            self::$instance['db'] = Yaf_Registry::get('_db');
        }
        return self::$instance['db'];
    }

    protected function CACHE()
    {
        if(@self::$instance['cache'] == null)
        {
            self::$instance['cache'] = Yaf_Registry::get('_cache');
        }
        return self::$instance['cache'];
    }

    public function getList(array $field = array(),array $where = array())    :array
    {
        $result = $this->search($field,$where,true);
        return $result;
    }

    private function beforeSearch(array $where = array(),$page = false)
    {
        if($page){
            if(!isset($where["page"]))$where["page"] = 1;
            if(!isset($where["limit"]))$where["limit"] = 10;
            $page = ($where["page"]-1)*$where["limit"];
            $limit = $where["limit"];
            unset($where["page"]);
            unset($where["limit"]);
            $where["LIMIT"] = [$page,$limit];
        }
        if($this->_status && !isset($where["status"])) $where["status"] = 1;
        return $where;
    }

    private function formatList($result)
    {
        if($this->_formatTime){
            foreach ($result as $key =>$value)
            {
                $this->_updateTime?$result[$key]["$this->_updateTime"] = date("Y-m-d H:i:s",$value["$this->_updateTime"]):$result[$key]["create_time"] = date("Y-m-d H:i:s",$value["create_time"]);
                $this->_createTime?$result[$key]["$this->_createTime"] = date("Y-m-d H:i:s",$value["$this->_createTime"]):$result[$key]["update_time"] = date("Y-m-d H:i:s",$value["update_time"]);
            }
        }
        return $result;
    }

    public function getAll(array $field = array(),array $where = array())
    {
        $result = $this->search($field,$where,false);
        return $result;
    }

    private function search(array $field = array(),array $where = array(),bool $page = true)
    {
        if(empty($field)) $field = "*";
        $where = $this->beforeSearch($where,$page);

        $result = $this->DB()->select($this->_table,$field,$where);

        $this->err();
        $result = $this->formatList($result);

        return $result;
    }

    public function leftSearch(array $joinTable = array(),array $field = array(),array $where = array())
    {
        if(empty($field)) $field = "*";
        $result = $this->DB()->select($this->_table,$joinTable,$field,$where);
        $this->err();
        return $result;
    }

    public function update(array $update = array(),$where = array())
    {
        if($this->_autoTime){
            $this->_updateTime?$update["$this->_updateTime"] = time(): $update['update_time'] = time();
        }
        $this->DB()->update($this->_table,$update,$where)->rowCount();
        $this->err();
        return $where['id'];
    }

    public function getLastId() :int
    {
        $id = $this->DB()->id();
        $this->err();
        return $id;
    }
    public function insert(array $insert = array())
    {
        if($this->_autoTime){
            $this->_updateTime?$insert["$this->_updateTime"] = time():$insert['update_time'] = time();
            $this->_createTime?$insert["$this->_createTime"] = time():$insert['create_time'] = time();
        }
        if($this->_status && !isset($insert["status"]))$insert["status"] = 1;
        $this->DB()->insert($this->_table,$insert)->rowCount();
        $this->err();
        return $this->getLastId();
    }

    public function delete($where)
    {
        $this->DB()->delete($this->_table,$where);
        $this->err();
    }
    //软删除
    public function softDelete(array $where = array())
    {
        $update["status"] = 0;
        $this->update($update,$where);
    }

    public function searchOne(array $field = array(),array $where = array())    :array
    {
        $list = $this->getList($field,$where);
        return $list[0];
    }

    public function count(array $where = array())
    {
        unset($where['page']);
        unset($where['limit']);
        if($this->_status && !isset($where['status']))$where["status"] = 1;
        $count = $this->DB()->count($this->_table,$where);
        $this->err();
        return $count;
    }

    public function sum($column,array $where = array())
    {
        unset($where['page']);
        unset($where['limit']);
        if($this->_status && !isset($where['status']))$where["status"] = 1;
        $count = $this->DB()->sum($this->_table,$column,$where);
        $this->err();
        return $count;
    }

    public function dbQuery(string $sql)
    {
        $result = $this->DB()->query($sql)->fetchAll();
        $this->err();
        return $result;
    }
    private function err()
    {
        if($this->DB()->error()[2])throw new \Exception\MyException($this->DB()->error()[2],$this->DB()->error()[1]);
    }

}