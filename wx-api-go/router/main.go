package router

import (
	. "crm/controller"
	"crm/library/array"
	"crm/library/ini"
	"crm/library/response"
	"github.com/gin-gonic/gin"
)

var res response.Response

func InitRouter() *gin.Engine {
	router := gin.Default()
	//router.Use(AuthMiddleWare())       //用于登录拦截 本应用暂时不做登录拦截
	router.GET("/", IndexController{}.IndexAction)
	router.GET("/okUser/login", IndexController{}.Login)
	router.GET("/okUser/loginOut", IndexController{}.LoginOut)
	return router
}

func AuthMiddleWare() gin.HandlerFunc {
	return func(c *gin.Context){
		action := c.Request.URL.String()
		if(!array.InArr(ini.ACTION,action)){
			res.AuthError(c,"不允许访问",nil)
			c.Abort()
			return
		}
		res.AuthError(c,"认证失败",nil)
		c.Abort()
		return
	}
}
