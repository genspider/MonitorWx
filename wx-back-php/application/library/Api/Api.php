<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2019/7/12
 * Time: 14:52
 */

namespace Api;


use Cache\Redis;
use Exception\MyException;
use Log\LogFactory;
use Yaf_Registry;

/***
 * curl 封装类
 * Class Api
 * @package Api
 */
class Api
{

    private $curl;

    private $timeout = 3000;

    private $cache = false;

    private $redis;

    private static $apis;

    public function __construct()
    {
        $this->curl = curl_init();
        $this->redis = Yaf_Registry::get('redis');

        //得到对应环境的api列表
        $env = get_cfg_var("yaf.environ");
        self::$apis = require "apis/api".ucfirst($env).'.php';
    }

    public function setCache($cache = true):void
    {
        $this->cache = $cache;
    }

    public function getCache():bool
    {
        return $this->cache;
    }

    /***
     * get
     * @param string $url
     * @param array $data
     * @param null $timeout
     * @return bool|mixed|string
     * @throws MyException
     */
    public function sendGet(string $url,array $data = array(),int $timeout = null)
    {
        $url = self::findUrl($url);                                     //从字典查找
        return $this->getData($data,$url,'get',$timeout,false);
    }

    /***
     * post
     * @param string $url
     * @param array $data
     * @param null $timeout
     * @return bool|mixed|string
     * @throws MyException
     */
    public function sendPost(string $url,array $data = array(),$timeout = null)
    {
        $url = self::findUrl($url);                                     //从字典查找
        return $this->getData($data,$url,'post',$timeout,false);
    }

    public function sendReptile(string $url,array $data = array(),$timeout = null)
    {
        return $this->getData($data,$url,'get',$timeout,true);
    }

    /***
     * 查找字典
     * @param $url
     * @return mixed
     * @throws MyException
     */
    protected static function findUrl(string $url)
    {
        $falg = false;
        foreach (self::$apis as $key =>$val)
        {
            if($val['alias'] == $url)
            {
                $falg = true;
                $url = $val['url'];
                break;
            }
        }
        if (!$falg) throw new MyException("别名未在接口字典找到");
        return $url;
    }

    /***
     * curl 调取api
     * @param array $data
     * @param string $url
     * @param string $type
     * @param null $timeout
     * @return bool|mixed|string
     * @throws MyException
     */
    protected function getData(array $data = array(),string $url = "", string $type,int $timeout = null,bool $reptile = false)
    {

        $key = md5($url.json_encode($data,true));        //缓存key

        $startTime = microtime ( true );                   //计算接口返回时间

        if($this->redis->has($key) && $this->getCache())                //判断是否设置缓存
        {

            $output = $this->redis->get($key);                          //直接从缓存拿

        }
        else {
            $ch = $this->curl;

            if (!$timeout) $timeout = $this->timeout;

            switch ($type){

                case 'post':

                    curl_setopt($ch, CURLOPT_POST, true);

                    curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

                    curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, $timeout);

                    break;

                case 'get':

                    if(!empty($data)) $url = self::url($url,$data);

                    break;

            }
            curl_setopt($ch,CURLOPT_HEADER,0);

            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); //不验证证书

            curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false); //不验证证书

            curl_setopt($ch,CURLOPT_RETURNTRANSFER,1 );

            curl_setopt($ch,CURLOPT_CONNECTTIMEOUT,10);

            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

            curl_setopt($ch, CURLOPT_URL, $url);

            $output = curl_exec($ch);

            curl_close($ch);

            if($output && $this->getCache()) $this->redis->set($key,$output);    //判断是否需要设置缓存

        }

        $endTime = microtime ( true );

        $runTime = $endTime - $startTime;                   //计算返回时间

        if(!$reptile) $output = json_decode($output,true);

        $data = [
            'time'=>date('Y-m-d H:i:s'),
            'parmas'=>$data,
            'req_type'=>$type,
            'url'=>$url,
            'runtime'=>$runTime,
            'result'=>$output,
            "cache"=>$this->getCache()
        ];
        //写入日志
        LogFactory::set("Log")->write("api",$data);

        return $output;
    }

    /***
     * 将参数和url生成一个url
     * @param $url
     * @param array $data
     * @return string
     */
    protected static function url(string $url,array $data):string
    {
        $str = '?';
        foreach ($data as $key => $val)
        {
            $str == '?' ? $str = $str . $key . '=' . $val : $str = $str . '&' . $key . '=' . $val;
        }
        return $url . $str;
    }

}