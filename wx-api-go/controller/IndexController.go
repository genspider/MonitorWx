package controller

import (
	"crm/library/response"
	"crm/model"
	"github.com/gin-gonic/gin"
	"time"
)

type IndexController struct {

}
var err error
var res response.Response

func(this IndexController) IndexAction(c *gin.Context)  {
	res.Success(c,"HelloWorld",nil)
}

func(this IndexController) Login(c *gin.Context)  {
	c.Writer.Header().Set("Access-Control-Allow-Origin", "*")
	userNo := c.Query("userNo")
	data:=make(map[string]interface{})
	data["is_online"] = 1
	data["last_login_time"] = time.Now().Format("2006-01-02 15:04:05")
	var okuser model.OkUser
	okuser.Update(userNo,data)
	res.Success(c,"登录成功",nil)
}

func(this IndexController) LoginOut(c *gin.Context)  {
	c.Writer.Header().Set("Access-Control-Allow-Origin", "*")
	userNo := c.Query("userNo")
	data:=make(map[string]interface{})
	data["is_online"] = 0
	data["last_login_time"] = time.Now().Format("2006-01-02 15:04:05")
	var okuser model.OkUser
	okuser.Update(userNo,data)
	res.Success(c,"注销成功",nil)
}

