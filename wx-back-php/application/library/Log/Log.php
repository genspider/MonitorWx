<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2019/7/11
 * Time: 17:47
 */

namespace Log;


interface Log
{
    function write($fileName,$fileContent);
}