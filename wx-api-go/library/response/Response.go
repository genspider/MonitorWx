package response

import (
	"crm/library/logger"
	"encoding/json"
	"github.com/gin-gonic/gin"
	"log"
	"net/http"
	"strings"
)

type Response struct {

}

const (
	SUCCESS int = 0
	ERROR int = 500
	AuthError int = 401
)

func(this *Response) Success(c *gin.Context,msg string,data interface{}){
	this.send(c,msg,SUCCESS,data)
}

func(this *Response) Error(c *gin.Context,msg string,data interface{}){
	this.send(c,msg,ERROR,data)
}

func(this *Response) AuthError(c *gin.Context,msg string,data interface{}){
	this.send(c,msg,AuthError,data)
}

func(this *Response) send(c *gin.Context,msg string,code int,data interface{}){

	res := map[string]interface{}{
		"code":  code,
		"msg":   msg,
		"data": data,
		"url":this.getUrl(c.Request),
	}

	output,err:= json.Marshal(res)
 	logger.GetLog().Println(string(output))

	if err!=nil{
		c.JSON(http.StatusOK,gin.H{
			"code":ERROR,
			"msg":err.Error(),
			"data":data,
		})
	}else{
		log.Println(output)
		c.JSON(http.StatusOK,gin.H{
			"code":code,
			"msg":msg,
			"data":data,
		})
	}
}

func(this *Response) getUrl(r *http.Request)(url string){
	scheme := "http://"
	if r.TLS != nil {
		scheme = "https://"
	}
	return strings.Join([]string{scheme, r.Host, r.RequestURI}, "")
}