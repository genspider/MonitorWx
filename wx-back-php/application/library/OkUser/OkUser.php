<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2019/8/14
 * Time: 16:51
 */
namespace OkUser;

use Exception\MyException;
use Yaf_Application;

class OkUser
{
    private $okUserModel;
    private $ipModel;
    private $firendModel;
    private $imgPath;

    public function __construct()
    {
        $this->okUserModel = new \OkUserModel();
        $this->ipModel = new \ServiceIpModel();
        $this->firendModel = new \FirendModel();
        $config = Yaf_Application::app()->getConfig();
        $this->imgPath = $config->application->imgPath;
    }

    public function login(array $parmas = array()):array
    {
        if(!$parmas['UserNo'])throw new MyException("OK账号不能为空");
        $where = [];
        $where['user_no'] = $parmas['UserNo'];
        $ipList = $this->okUserModel->getAll(['ip','cellphone'],$where);
        if(empty($ipList)) throw new MyException("该账号不存在");
        $where2 = [];
        $where2['ip'] = $ipList[0]['ip'];

        $list = $this->ipModel->getAll([],$where2);
/*
        $where = array();
        $where['user_no'] = $parmas['UserNo'];
        $where['startTime'] = mktime(18,0,0,date('m'),date('d')-1,date('Y'))*1000;
        $where['endTime'] = time()*1000;
        $fansArr = $this->firendAll($where);*/

        $list[0]['user_no'] = $parmas['UserNo'];
        $list[0]['cellphone'] = $ipList[0]['cellphone'];
        //$list[0]['fans'] = $fansArr;
        $list['msg'] = "登录成功";
        return $list;
    }

    public function addFirend(array $parmas = array()):array
    {
        if(!$parmas['UserNo'])throw new MyException("微信号不能为空");
        $insert = [];
        $insert['user_no'] = $parmas['UserNo'];
        $insert['fans_name'] = $parmas['fansName'];
        $insert['create_time'] = time();
        $this->firendModel->insert($insert);
        $res['msg'] = "增加成功";
        return $res;
    }

    public function insertUser(array $parmas = array()):array
    {
        $insert = [];
        $insert['user_no'] = $parmas['user_no'];
        $count = $this->okUserModel->count($insert);
        if($count > 0)throw new MyException("不能有重复的微信号");
        $insert['groups'] = $parmas['groups'];
        $insert['cellphone'] = $parmas['cellphone'];
        $insert['power'] = $parmas['power'];
        $this->okUserModel->insert($insert);
        $res['msg'] = "增加账号成功";
        return $res;
    }

    public function userList(array $parmas = array()):array
    {
        $where = [
            "ORDER"=>[
                "is_online" => "DESC",
            ]
        ];
        if($parmas["user_no"])$where["user_no[~]"] = $parmas["user_no"];
        if($parmas["page"])$where["page"] = $parmas["page"];
        if($parmas["limit"])$where["limit"] = $parmas["limit"];
        if($parmas["groups"])$where["groups"] = $parmas["groups"];
        if($parmas["is_online"]!="")$where["is_online"] = $parmas["is_online"];

        $list = $this->okUserModel->getList([],$where);
        $res['list'] = $list;
        $res['count'] = $this->okUserModel->count($where);
        $res['msg'] = "账号查询成功";
        return $res;
    }

    public function firendList(array $parmas = array()):array
    {
        $where1 = array();
        if($parmas["groups"])$where1["groups"] = $parmas["groups"];

        $list = $this->okUserModel->getAll(['user_no'],$where1);

        foreach ($list as $k =>$v)
        {
            $wx_nos[] = $v['user_no'];
        }

        if(empty($wx_nos))$wx_nos[0] = 0;

        $where = array();

        $where['user_no'] = $wx_nos;
        if($parmas["startTime"])$parmas["startTime"] = intval($parmas["startTime"]/1000);
        if($parmas["endTime"])$parmas["endTime"] = intval($parmas["endTime"]/1000);
        if($parmas["startTime"] && !$parmas["endTime"]) $parmas["endTime"] = time();
        if($parmas["startTime"] && $parmas["endTime"]) $where["create_time[<>]"] = [$parmas["startTime"],$parmas["endTime"]];
        if($parmas["user_no"])$where["user_no[~]"] = $parmas["user_no"];
        if($parmas["excel"]){
            $list = $this->firendModel->getAll([],$where);
        }else{
            if($parmas["page"])$where["page"] = $parmas["page"];
            if($parmas["limit"])$where["limit"] = $parmas["limit"];
            $list = $this->firendModel->getList([],$where);
        }

        foreach ($list as $key => $val)
        {
            $list[$key]['groups'] = $this->okUserModel->searchOne(['groups'],['user_no'=>$val['user_no']])['groups'];
            $list[$key]['create_time'] = date("Y-m-d H:i:s",$val["create_time"]);
            unset($key);
            unset($val);
        }
        $res['list'] = $list;
        $res['count'] = $this->firendModel->count($where);
        $res['msg'] = "查询成功";
        return $res;
    }

    public function firendAll(array $parmas = array()):array
    {
        $condition = [];
        if($parmas["user_no"])$condition["user_no[~]"] = $parmas["user_no"];
        if($parmas["groups"])$condition["groups"] = $parmas["groups"];

        $condition['GROUP'] = "user_no";
        $wxnoList = $this->okUserModel->getAll(['user_no'],$condition);

        $wx_nos = [];
        foreach ($wxnoList as $k =>$v)
        {
            $wx_nos[] = $v['user_no'];
        }

        $list = $this->okUserModel->getAll(['user_no','id','groups'],$condition);

        if(empty($wx_nos))$wx_nos[0] = 0;

        $count = count($this->okUserModel->getAll([],$condition));

        if($parmas["startTime"])$parmas["startTime"] = intval($parmas["startTime"]/1000);
        if($parmas["endTime"])$parmas["endTime"] = intval($parmas["endTime"]/1000);
        if($parmas["startTime"] && !$parmas["endTime"]) $parmas["endTime"] = time();

        foreach ($list as $k =>$v)
        {
            $where = [];
            $where['user_no'] = $v['user_no'];

            if($parmas["startTime"] && $parmas["endTime"])
            {
                $where["create_time[<>]"] = [$parmas["startTime"],$parmas["endTime"]];
            }

            $list[$k]['allCount'] = $this->firendModel->count($where);
            unset($k);
            unset($v);
        }
        $list = $this->array_sort($list,"allCount");

        if(!$parmas["excel"]){

            $list = array_slice($list, ($parmas["page"]-1)*$parmas["limit"], $parmas["limit"]);
        }
        /*else{
            if($parmas["page"])$condition["page"] = $parmas["page"];
            if($parmas["limit"])$condition["limit"] = $parmas["limit"];
            $list = $this->okUserModel->getList(['user_no','id','groups'],$condition);
        }
        //总条数
        unset($condition['page']);
        unset($condition['limit']);*/

        $res['list'] = $list;
        $res['count'] = $count;
        $res['msg'] = "查询成功";

        return $res;
    }
    //指定数组以$keys键值排序
    function array_sort(array $array,string $name = ""):array
    {
        for ($i = 0; $i < count($array) ; $i++) {
            // 第二层为从$i+1的地方循环到数组最后
            for ($j = $i+1; $j < count($array); $j++) {
                // 比较数组中两个相邻值的大小
                if ($array[$i][$name] < $array[$j][$name]) {
                    $tem = $array[$i]; // 这里临时变量，存贮$i的值
                    $array[$i] = $array[$j]; // 第一次更换位置
                    $array[$j] = $tem; // 完成位置互换
                }
            }
        }
        return $array;
    }
    public function groupsList()
    {
        $condition['GROUP'] = "groups";
        $list = $this->okUserModel->getAll(['groups'],$condition);
        $result = [];
        $result['list'] = $list;
        $result['msg'] = "查询成功";
        return $result;
    }

}