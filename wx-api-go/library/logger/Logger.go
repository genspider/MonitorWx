package logger

import (
	"crm/library/ini"
	"fmt"
	"log"
	"os"
)


var err error
var file *os.File

func GetLog()(logger *log.Logger){

	fmt.Println("设置日志啦")
	fmt.Println(ini.LOGADDR)
	file, err = os.OpenFile(ini.LOGADDR, os.O_APPEND|os.O_CREATE|os.O_WRONLY, 666)
	if err != nil {

		fmt.Println(err.Error())
	}
	logger = log.New(file, "", log.LstdFlags)
	logger.SetPrefix("crm") // 设置日志前缀
	return logger
}