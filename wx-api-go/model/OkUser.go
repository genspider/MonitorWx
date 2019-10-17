package model

type OkUser struct {
	Model
	UserNo string `json:"user_no"`
	Ip string `json:"ip"`
	Groups string `json:"groups"`
	IsOnline int32 `json:"is_online"`
	LastLoginTime string `json:"last_login_time"`
	LastOutTime string `json:"last_out_time"`
	Cellphone string `json:"cellphone"`
	Power int32 `json:"power"`
}

func(this *OkUser)Update(username string,data map[string]interface{}){
	Db.Model(OkUser{}).Where("user_no = ?", username).Updates(data)
}
