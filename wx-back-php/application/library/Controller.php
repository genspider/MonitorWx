<?php

class Controller extends Yaf_Controller_Abstract
{
    protected $_session;        //session
    protected $_config;         //配置文件
    protected $_controller;     //类名
    protected $_module;         //模型
    protected $_action;         //类方法
    protected $_cache;          //缓存
    private static $instance = array();

    public function init()
    {
        header('Access-Control-Allow-Origin:*');
        Yaf_Dispatcher::getInstance()->disableView();//关闭自动渲染，改为主动输出视图;
        // 检测access——token有效性
        $this->_session = Yaf_Session::getInstance();
        $this->_session->start();
        $this->_config  = Yaf_Registry::get('config');
        $this->_cache   = Yaf_Registry::get('cache');

        $this->_module     = strtolower($this->_module);
        $this->_controller = strtolower($this->_request->controller);
        $this->_action     = strtolower($this->_request->action);
    }

}