<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2019/7/11
 * Time: 17:48
 */

namespace Log;

use Exception\MyException;

class LogFactory
{
    /***
     * 制造一个工厂
     * @param $class
     * @return mixed
     * @throws MyException
     */
    public static function set($class)
    {
        $classname = ucfirst(strtolower($class));

        require_once __DIR__.'/'.$class."Impl.php";

        $classname = __NAMESPACE__ . '\\' . $classname.'Impl';

        if (!class_exists($classname))
        {
            throw new MyException('不存在这个类');
        }

        return new $classname;
    }
}