package com.esprit.jobhunter.Entity

import java.io.Serializable

class Internship : Serializable {
    var id = 0
    var name: String? = null
    var label: String? = null
    var description: String? = null
    var start_date: String? = null
    var educ_req: String? = null
    var duration: String? = null
    var user_id: String? = null
    var company_name: String? = null
    var company_pic: String? = null
    var skills: String? = null
    var inserted_sq_date: String? = null

    constructor() {}
    constructor(id: Int, name: String?, label: String?, description: String?, duration: String?, start_date: String?, educ_req: String?, user_id: String?, skills: String?, company_name: String?, company_pic: String?, inserted_sq_date: String?) {
        this.id = id
        this.name = name
        this.label = label
        this.description = description
        this.start_date = start_date
        this.duration = duration
        this.educ_req = educ_req
        this.user_id = user_id
        this.skills = skills
        this.company_name = company_name
        this.company_pic = company_pic
        this.inserted_sq_date = inserted_sq_date
    }

    constructor(id: Int, name: String?, label: String?, description: String?, start_date: String?, educ_req: String?, duration: String?, user_id: String?, skills: String?, company_name: String?, company_pic: String?) {
        this.id = id
        this.name = name
        this.label = label
        this.description = description
        this.start_date = start_date
        this.educ_req = educ_req
        this.duration = duration
        this.user_id = user_id
        this.skills = skills
        this.company_name = company_name
        this.company_pic = company_pic
    }

    override fun toString(): String {
        return "Internship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", start_date='" + start_date + '\'' +
                ", educ_req='" + educ_req + '\'' +
                ", duration='" + duration + '\'' +
                ", user_id='" + user_id + '\'' +
                ", skills='" + skills + '\'' +
                ", company_name='" + company_name + '\'' +
                ", company_pic='" + company_pic + '\'' +
                '}'
    }

    companion object {
        const val TABLE_NAME = "internships"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_LABEL = "label"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_START_DATE = "start_date"
        const val COLUMN_EDUC_REC = "educ_req"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_SKILLS = "skills"
        const val COLUMN_COMPANY_NAME = "company_name"
        const val COLUMN_COMPANY_PIC = "company_pic"
        const val COLUMN_TIMESTAMP = "timestamp"

        // Create table SQL query
        const val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_LABEL + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_START_DATE + " TEXT,"
                + COLUMN_EDUC_REC + " TEXT,"
                + COLUMN_DURATION + " TEXT,"
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_SKILLS + " TEXT,"
                + COLUMN_COMPANY_NAME + " TEXT,"
                + COLUMN_COMPANY_PIC + " TEXT,"
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")")
    }
}