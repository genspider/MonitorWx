"# MonitorWx" 
<div align=center><img src="http://test.zcjian.cn/pc1.png" /></div>
<div align=center><img src="http://test.zcjian.cn/pc2.png" /></div>
<div align=center><img src="http://test.zcjian.cn/m.jpg" /></div>
该项目用于监控客服人员的微信好友添加记录并记录下来，供运营人员核对信息，该项目独立开发完成，安卓端客服通过各自的微信号登陆，app会监听他们的通知栏，并过滤拿到微信的添加好友通知里面的好友名字，安卓通过okhttp发送给后端并记录下来，并在安卓表现层提示和展示，客服人员如果下线则会被发手机短信通知并且在后台管理能查看得到，后台管理平台则有用户管理，微信好友添加详情列表，微信好友添加统计，版本更新管理。安卓端可自动版本更新，应用保活(9.0改成了进程保活)，后台管理接口是由php的c扩展框架yaf开发，app端接口用golang的gin+gorm开发
