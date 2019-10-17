<?php

use Exception\MyException;
use Log\Log;
use Log\LogFactory;
use Log\LogImpl;
use Response\OutputHelper;
use Seasx\SeasLogger\Logger;
use Test\FtpTest;
use Yaf\Application;

class IndexController extends Controller
{
   public function indexAction() //默认Action
   {
      $api = new \Api\Api();
      $data =  $api->sendReptile("http://ads.zcjian.cn/ads.php?cid=2&timestamp=1565341919005&page=1&pagesize=10&return_data=json&callback=");
      $data = json_decode($data,true);
      var_dump($data);

     // echo 'json1';
   }
}

