package ini

import (
	"fmt"
	"github.com/go-ini/ini"
	"log"
	"os"
)

var (
	Cfg *ini.File

	HTTPPort string

	LOGADDR string

	ACTION []string
)

func init() {
	var err error
	goEnv := os.Getenv("GO_ENV");
	env := "conf/"+goEnv+".ini"
	Cfg, err = ini.ShadowLoad(env)
	if err != nil {
		log.Fatalf("Fail to parse 'conf/app.ini': %v", err)
	}
	LoadServer()
	Log()
	LoadAllowaccess(env)
}

func Log(){
	LOGADDR = Cfg.Section("").Key("LOG_ADDR").String()
}

func LoadServer(){
	HTTPPort = Cfg.Section("server").Key("HTTP_PORT").MustString(":8090")

}
func LoadAllowaccess(env string){
	ACTION = Cfg.Section(`allowaction`).Key("action").ValueWithShadows()
	fmt.Println("ROUTER")
	fmt.Println(ACTION)
}