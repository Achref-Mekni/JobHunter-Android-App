package com.esprit.jobhunter.Entity

import java.io.Serializable

class Job : Serializable {
    var id = 0
    var name: String? = null
    var label: String? = null
    var description: String? = null
    var start_date: String? = null
    var contract_type: String? = null
    var career_req: String? = null
    var user_id: String? = null
    var company_name: String? = null
    var company_pic: String? = null
    val type = "j"
    var inserted_sq_date: String? = null
    var skills: String? = null

    constructor() {}
    constructor(id: Int, name: String?, label: String?, description: String?, start_date: String?, contract_type: String?, career_req: String?, user_id: String?, skills: String?, company_name: String?, company_pic: String?, inserted_sq_date: String?) {
        this.id = id
        this.name = name
        this.label = label
        this.description = description
        this.start_date = start_date
        this.contract_type = contract_type
        this.career_req = career_req
        this.user_id = user_id
        this.skills = skills
        this.company_name = company_name
        this.company_pic = company_pic
        this.inserted_sq_date = inserted_sq_date
    }

    constructor(id: Int, name: String?, label: String?, description: String?, start_date: String?, contract_type: String?, career_req: String?, user_id: String?, skills: String?, company_name: String?, company_pic: String?) {
        this.id = id
        this.name = name
        this.label = label
        this.description = description
        this.start_date = start_date
        this.contract_type = contract_type
        this.career_req = career_req
        this.user_id = user_id
        this.skills = skills
        this.company_name = company_name
        this.company_pic = company_pic
    }

    override fun toString(): String {
        return "Job{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", start_date='" + start_date + '\'' +
                ", contract_type='" + contract_type + '\'' +
                ", career_req='" + career_req + '\'' +
                ", user_id='" + user_id + '\'' +
                ", skills='" + skills + '\'' +
                ", company_name='" + company_name + '\'' +
                ", company_pic='" + company_pic + '\'' +
                '}'
    }

    companion object {
        const val TABLE_NAME = "jobs"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_LABEL = "label"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_START_DATE = "start_date"
        const val COLUMN_CONTRACT_TYPE = "contract_type"
        const val COLUMN_CAREER_REQ = "career_req"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_SKILLS = "skills"
        const val COLUMN_COMPANY_NAME = "company_name"
        const val COLUMN_COMPANY_PIC = "company_pic"
        const val COLUMN_TYPE = "type"
        const val COLUMN_TIMESTAMP = "timestamp"

        // Create table SQL query
        const val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_LABEL + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_START_DATE + " TEXT,"
                + COLUMN_CONTRACT_TYPE + " TEXT,"
                + COLUMN_CAREER_REQ + " TEXT,"
                + COLUMN_USER_ID + " TEXT,"
                + COLUMN_SKILLS + " TEXT,"
                + COLUMN_COMPANY_NAME + " TEXT,"
                + COLUMN_COMPANY_PIC + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")")
    }
}