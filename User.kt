package com.esprit.jobhunter.Entity

import java.io.Serializable

public class User : Serializable {
    var id = 0
    var name: String? = null
    var last_name: String? = null
    var birth_date: String? = null
    var gender: String? = null
    var email: String? = null
    var adress: String? = null
    var tel1: String? = null
    var tel2: String? = null
    var fax: String? = null
    var nationality: String? = null
    var description: String? = null
    var picture: String? = null
    var type: String? = null
    var password: String? = null
    var cv_id: String? = null


    constructor(id: Int, name: String?, last_name: String?, birth_date: String?, gender: String?, email: String?, adress: String?, tel1: String?, tel2: String?, fax: String?, nationality: String?, description: String?, picture: String?, type: String?, password: String?) {
        this.id = id
        this.name = name
        this.last_name = last_name
        this.birth_date = birth_date
        this.gender = gender
        this.email = email
        this.adress = adress
        this.tel1 = tel1
        this.tel2 = tel2
        this.fax = fax
        this.nationality = nationality
        this.description = description
        this.picture = picture
        this.type = type
        this.password = password

    }

    constructor() {}

    override fun toString(): String {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", adress='" + adress + '\'' +
                ", tel1='" + tel1 + '\'' +
                ", tel2='" + tel2 + '\'' +
                ", fax='" + fax + '\'' +
                ", nationality='" + nationality + '\'' +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", type='" + type + '\'' +
                ", password='" + password + '\'' +
                '}'
    }
}