package main

import (
	"crm/library/ini"
	"crm/model"
	. "crm/router"
	_ "github.com/Unknwon/goconfig"
	_ "github.com/jinzhu/gorm"
)

func main(){
	defer model.Db.Close()
	router := InitRouter()
	router.Run(ini.HTTPPort)
}
