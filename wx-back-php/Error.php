<?php

use Log\LogFactory;

/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2019/8/7
 * Time: 13:27
 */

class Error
{
    public function __construct()
    {
        //        //捕获异常
        set_error_handler([$this,"appErrorHandler"]);
        //记录日志
        register_shutdown_function([$this,"appRegisterShutdown"]);
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
            'result'=>json_decode(ob_get_contents(),true)
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