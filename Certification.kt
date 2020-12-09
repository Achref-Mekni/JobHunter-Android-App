package com.esprit.jobhunter.Entity

class Certification {
    var id = 0
    var label: String? = null
    var licence_num: String? = null
    var cert_date: String? = null
    var expire_date: String? = null
    var cert_authority: String? = null
    var if_expire = 0
    var cv_id = 0

    constructor() {}
    constructor(id: Int, label: String?, licence_num: String?, cert_date: String?, expire_date: String?, cert_authority: String?, if_expire: Int, cv_id: Int) {
        this.id = id
        this.label = label
        this.licence_num = licence_num
        this.cert_date = cert_date
        this.expire_date = expire_date
        this.cert_authority = cert_authority
        this.if_expire = if_expire
        this.cv_id = cv_id
    }

    override fun toString(): String {
        return "Certification{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", licence_num='" + licence_num + '\'' +
                ", cert_date='" + cert_date + '\'' +
                ", expire_date='" + expire_date + '\'' +
                ", cert_authority='" + cert_authority + '\'' +
                ", if_expire=" + if_expire +
                ", cv_id=" + cv_id +
                '}'
    }
}