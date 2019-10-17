<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2019/7/11
 * Time: 15:35
 */

namespace Log;

use Yaf_Application;

/***
 * 日志
 * Class LogImpl
 * @package Log
 */
class LogImpl implements Log
{

    private $_logPath;

    public function __construct()
    {
        $config = Yaf_Application::app()->getConfig();
        $this->_logPath = $config->application->logPath;
    }

    public function write($fileName,$data):void
    {
        $fileName = $this->_logPath . '/' . $fileName.date('Y-m-d').'.log';

        $dir_name = dirname( $fileName);

        if (!file_exists($dir_name))
        {
            $res = mkdir(iconv("UTF-8", "GBK", $dir_name), 0777, true);
        }

        $fp = fopen($fileName, "a+");//打开文件资源通道 不存在则自动创建

        fwrite($fp, json_encode($data,true)."\r\n----------------------------------------------------------------------------------------------------------\r\n");//写入文件

        fclose($fp);
    }
}