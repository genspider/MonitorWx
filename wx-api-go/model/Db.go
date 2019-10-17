package model

import (
	"crm/library/ini"
	"fmt"
	"github.com/jinzhu/gorm"
	_ "github.com/jinzhu/gorm/dialects/mysql"
	"log"
)

type Model struct {
	ID int `gorm:"primary_key" json:"id"`
}
var Db *gorm.DB

func init(){
	Db = GetDb()
}
//连接数据库
func GetDb() (db *gorm.DB){

	fmt.Println("hello go");
	var (
		err error
		dbType, dbName, user, password, host string
	)

	sec, err := ini.Cfg.GetSection("database")

	if err != nil {
		log.Fatal(2, "Fail to get section 'database': %v", err)
	}

	dbType = sec.Key("TYPE").String()
	dbName = sec.Key("NAME").String()
	user = sec.Key("USER").String()
	password = sec.Key("PASSWORD").String()
	host = sec.Key("HOST").String()

	db, err = gorm.Open(dbType, fmt.Sprintf("%s:%s@tcp(%s)/%s?charset=utf8&parseTime=True&loc=Local",
		user,
		password,
		host,
		dbName))

	//defer db.Close()

	if err != nil {
		panic(err)
	}
	// 全局禁用表名复数
	db.SingularTable(true)
	return
}

func Close(){
	Db.Close()
}
