<?php

namespace Exception;

/* 自定义的一个异常处理类，但必须是扩展内异常处理类的子类 */

use Response\OutputHelper;

class MyException extends \Exception
{

    //重定义构造器使第一个参数 message 变为必须被指定的属性
    public function __construct($message, $code=500){
        //可以在这里定义一些自己的代码
        //建议同时调用 parent::construct()来检查所有的变量是否已被赋值
        parent::__construct($message, $code);
    }
    public function __toString()
    {
        $data = [];
        $msg = "[".$this->code."]:".$this->message;
        $data = $this->getTrace();
        $data['file'] = $this->getFile();
        $data['line'] = $this->getLine();
        $data["msg"] = $msg;
        $data["code"] = $this->code;

        //重写父类方法，自定义字符串输出的样式
        OutputHelper::output($data);
    }

    public function getMsg()
    {
        return $this->message;
    }

    public function setMsg(string $msg = '')
    {
        $this->message = $msg;
    }
}
