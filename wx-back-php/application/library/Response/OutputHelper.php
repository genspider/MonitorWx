<?php
/**
 * Created by PhpStorm.
 * User: admin
 * Date: 2019/7/11
 * Time: 14:17
 */

namespace Response;


use function PHPSTORM_META\type;

class OutputHelper
{

    /***
     * 输出函数
     * @param array $data
     * @param int $code
     * @param string $msg
     * @param null $callback
     * @param bool $formate
     */
    public static function outPut($data,  int $code = 0,  string $msg = "")
    {
        //触发格式化前事件
        $re = self::format($data, $code, $msg);
        exit($re) ;
    }

    /***
     * @param $data
     * @param $code
     * @param $msg
     * @return string
     */
    protected static function format($data ,$code, $msg):string
    {
        //返回成功的第一种方式  不传数据 只传 消息和状态
        if($code != null){

            $result = self::errorJson2($data);

        }else{
            //返回成功的第二种方式 信息 包含在data里
            if(is_array($data)){
                if(isset($data['code'])){
                    $result = self::errorJson2($data);
                }else{
                    $result = self::successJson2($data,$msg);
                }
            }else{
                $result = self::errorJson($data);
            }
        }
        self::changeEncodingForArray($result);
        return json_encode($result,true);
    }

    /***
     * @param $code
     * @param $msg
     * @return array
     */
    protected static function successJson1($code,$msg):array
    {

        $result['code'] = $code;

        $result['msg'] = $msg;

        return $result;
    }

    /***
     * @param $data
     * @param $msg
     * @return array
     */
    protected static function successJson2(array $data,string $msg):array
    {
        if(isset($data['count']))$result['count'] = $data['count'];
        
        isset($data['msg'])?$result['msg'] = $data['msg']:$result['msg'] = $msg;

        $result['code'] = 0;

        unset($data['msg']);

        unset($data['count']);

        $result['data'] = $data;

        return $result;
    }

    /***
     * @param $data
     * @return array
     */
    protected static function errorJson($data):array
    {

        $result['msg'] = $data;

        $result['code'] = 500;

        $result['data'] = null;

        return $result;
    }

    /***
     * @param array $data
     * @return array
     */
    protected static function errorJson2(array $data = array()):array
    {
        $result = [];

        if(array_key_exists('msg',$data))
        {
            $result['msg'] = $data['msg'];
            unset($data['msg']);
        }
        if(array_key_exists('code',$data)){
            $result['code'] = $data['code'];
            unset($data['code']);
        }
        if(array_key_exists('data',$data)){
            $result['data'] = $data['data'];
        }else{
            $result['data'] = $data;
        }

        return $result;
    }

    static function changeEncodingForArray(&$array = [])
    {
        $result = [];
        foreach ($array as $key=>$value) {
            if (is_array($value)) {
                self::changeEncodingForArray($value);
                $result[$key] = $value;
            } else {
                if(gettype($value) == "resource"){
                    $value = get_resource_type($value);
                }
                $result[$key] = $value;
            }
            unset($key);
            unset($value);
        }
        $array = $result;
        unset($result);
    }
}