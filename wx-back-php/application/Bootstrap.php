<?php

use Log\LogFactory;
use Response\OutputHelper;

class Bootstrap extends Yaf_Bootstrap_Abstract {
    private $_config;

    function _initMedoo(Yaf_Dispatcher $dispatcher) {
        // echo "1st called\n";
        $this->_config = Yaf_Application::app()->getConfig();
    }
    /**
     * @param Yaf_Dispatcher $dispatcher
     *
     * @access public
     * @return void
     */
    public function _initSession(Yaf_Dispatcher $dispatcher)
    {
        Yaf_Session::getInstance()->start();
    }

        /**
     * @param Yaf_Dispatcher $dispatcher
     *
     * @access public
     * @return void
     */
    public function _initDB(Yaf_Dispatcher $dispatcher)
    {
        //Yaf_Loader::import('db/Medoo.php');
//        var_dump($this->_config->medoo->type);
        $db = $database = new Medoo([
            'database_type' => $this->_config->medoo->type,
            'database_name' => $this->_config->medoo->mysql->name,
            'server'        => $this->_config->medoo->mysql->server,
            'username'      => $this->_config->medoo->mysql->username,
            'password'      => $this->_config->medoo->mysql->password,
            // 'charset'       => $this->_config->medoo->mysql->charset,
            // 'port'          => $this->_config->medoo->mysql->port,
            // 'prefix'        => $this->_config->medoo->mysql->prefix,
            // 'logging'       => $this->_config->medoo->log,
            // 'option'        => [PDO::ATTR_CASE => PDO::CASE_NATURAL],
        ]);
        Yaf_Registry::set('_db', $db);

        //Yaf_Loader::import('cache/cache.php');
        //$cache= new cache();
        //Yaf_Registry::set('_cache', $cache);
    }

    public function _initCommonFunctions(){  
        Yaf_Loader::import(Yaf_Application::app()->getConfig()->application->directory . '/common/Functions.php');  
    }

    public function _initConfig(){
        Yaf_Dispatcher::getInstance()->autoRender(true);
    }

    public function _initRoute(){

        //        //捕获异常
        set_error_handler([$this,"appErrorHandler"]);
        //记录日志
        register_shutdown_function([$this,"appRegisterShutdown"]);

    }

    /***
     * redis 初始化
     */
    public function _initRedis()
    {
        $option = [
            'host'=>$this->_config->application->redis->host,
            'port'=>$this->_config->application->redis->port,
            'password'=>$this->_config->application->redis->password,
            'timeout'=>$this->_config->application->redis->timeout
        ];
        $redis = new \Cache\Redis($option);
        Yaf_Registry::set('redis', $redis);
    }

    /***
     * 记录日志
     * @throws \Exception\MyException
     */
    function appRegisterShutdown()
    {

        $error = error_get_last();

        if($error['type'])
        {
            $errstr = "错误文件:".$error['file'].",行号:".$error['line'].",错误原因:".$error['message'];
            //throw new \Exception\MyException($errstr,$error['type']);
            echo json_encode(["msg"=>$errstr,"code"=>500],true);
        }


        $req_type = $_SERVER['REQUEST_METHOD'];

        switch ($req_type){
            case "GET":
                $parmas = $_GET;
                break;
            case "POST":
                $parmas = $_POST;
                break;
        }

        $data = [
            'ip'=>$_SERVER['REMOTE_ADDR'],
            'time'=>date('Y-m-d H:i:s'),
            'parmas'=>$parmas,
            'req_type'=>$req_type,
            'url'=>'http://'.$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI'],
            'result'=>json_decode(ob_get_contents(),true),
            'config'=>$this->_config
        ];
        LogFactory::set("Log")->write("project",$data);
    }

    /***
     *  自定义捕获异常
     * @param $errno
     * @param $errstr
     * @param $errfile
     * @param $errline
     * @return bool
     * @throws \Exception\MyException
     */
    function appErrorHandler($errno, $errstr ,$errfile, $errline)
    {
        $errstr = "错误文件:$errfile,行号:$errline,错误原因:$errstr";
        switch($errno)
        {
            case E_ERROR :
            case E_WARNING:
           /* case E_PARSE:
            case E_CORE_ERROR :
            case E_COMPILE_ERROR:
            case E_PARSE:*/
                throw new \Exception\MyException($errstr,$errno);
                break;
            default:

                //不显示Notice级的错误
                break;
        }
        return true;
    }

}