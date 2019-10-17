<?php

class Utils {


    // 验证Token
    public function verification($jwt)
	{
		$key = '8aa5d45da81813a62a2bca12b62568d5'; //key要和签发的时候一样

		try {
            JWT_JWT::$leeway = 60;//当前时间减去60，把时间留点余地
	       		$decoded = JWT_JWT::decode($jwt, $key, ['HS256']); //HS256方式，这里要和签发的时候对应
	       		$arr = (array)$decoded;
                if($arr['sid'] != session_id()) //!=session_id登陆失败直接返回退出
                {
                    // 回退登录处
                    $iData=array(
                        'code'   => 1001,
                        'msg'   => '登陆失败',
                        'data'    => array(
                           
                        )
                     );
                    $resp = json_encode($iData,JSON_UNESCAPED_UNICODE);
                    die($resp);
                }else {
                    return $arr;
                }
                //    var_dump($arr);
	    	} catch(JWT_SignatureInvalidException $e) {  //签名不正确
	    		echo $e->getMessage();
	    	}catch(JWT_BeforeValidException $e) {  // 签名在某个时间点之后才能用
	    		echo $e->getMessage();
	    	}catch(JWT_ExpiredException $e) {  // token过期
	    		echo $e->getMessage();
	   	}catch(Exception $e) {  //其他错误
	    		echo $e->getMessage();
	    	}
	    //Firebase定义了多个 throw new，我们可以捕获多个catch来定义问题，catch加入自己的业务，比如token过期可以用当前Token刷新一个新Token
	}
	//签发Token
	public function lssue($userid,$username,$autologin = 0)
	{
		$key = '8aa5d45da81813a62a2bca12b62568d5'; //key
		$time = time(); //当前时间
       		$token = [
        	'iss' => 'http://www.helloweba.net', //签发者 可选
           	'aud' => 'http://www.helloweba.net', //接收该JWT的一方，可选
           	'iat' => $time, //签发时间
           	'nbf' => $time, //(Not Before)：某个时间点后才能访问，比如设置time+30，表示当前时间30秒后才能使用
            'exp' => $time+7200, //过期时间,这里设置2个小时 24小时是86400秒
            'sid' => session_id(),
            	'data' => [ //自定义信息，不要定义敏感信息
             		'userid'    => $userid,
                    'username'  => $username,
                    'autologin' => $autologin
                    
            ]
        ];
        return JWT_JWT::encode($token, $key); //输出Token
	}
}