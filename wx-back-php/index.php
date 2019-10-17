<?php


error_reporting(0);

define("APP_PATH",  realpath(dirname(__FILE__) . '/')); /* 指向public的上一级 */

define("BASE_URL",  "http://www.local.yaf.com"); /* 指向public的上一级 */

define("BASE_STATIC",  BASE_URL."/static"); /* 指向public的上一级 */

$error = new Error();

//yaf.environ 为 product 和 develop
$app  = new Yaf_Application(APP_PATH . "/conf/application.ini",ini_get("yaf.environ"));

$loader = Yaf_Loader::getInstance();

$loader->registerLocalNamespace(array("local"));

$app->bootstrap()->run();
