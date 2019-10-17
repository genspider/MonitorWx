<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2019/8/30
 * Time: 14:11
 */

namespace Apk;

use ApkInfoModel;

//app安装包
class Apk
{
    private $_model;

    public function __construct()
    {
        $this->_model = new ApkInfoModel();
    }

    public function insert(array $parmas = array()):array
    {
        $this->_model->insert($parmas);
        $res['msg'] = "新增成功";
        return $res;
    }

    public function update(array $parmas = array()):array
    {
        $where['id'] = $parmas['id'];
        unset($parmas['id']);

        $this->_model->update($parmas,['id'=>$where]);
        $res['msg'] = "修改成功";
        return $res;
    }

    public function list(array $parmas = array()):array
    {

        $condition = [];
        if($parmas["name"])$condition["name[~]"] = $parmas["name"];

        $list = $this->_model->getList([],$condition);
        $res['list'] = $list;
        $res['msg'] = "查询成功";
        $res['count'] = $this->_model->count($parmas);
        return $res;
    }
    public function getByName(array $parmas = array()):array
    {
        $list = $this->_model->searchOne([],$parmas);
        $list['msg'] = "查询成功";
        return $list;
    }
}