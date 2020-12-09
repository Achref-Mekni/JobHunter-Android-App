package com.esprit.jobhunter.Entity

class ApplicationJob {
    var id_applicant = 0
    var id_job = 0
    var creation_date: String? = null
    var label: String? = null
    var description: String? = null
    var start_date: String? = null
    var contract_type: String? = null
    var career_req: String? = null
    var id_company = 0
    var name_company: String? = null
    var picture_company: String? = null
    var is_bookmarked = false
    val type = "j"

    constructor() {}
    constructor(id_user: Int, id_job: Int, creation_date: String?, label: String?, description: String?, start_date: String?, contract_type: String?, career_req: String?, id_company: Int, name_company: String?, picture_company: String?) {
        id_applicant = id_user
        this.id_job = id_job
        this.creation_date = creation_date
        this.label = label
        this.description = description
        this.start_date = start_date
        this.contract_type = contract_type
        this.career_req = career_req
        this.id_company = id_company
        this.name_company = name_company
        this.picture_company = picture_company
    }

    override fun toString(): String {
        return "ApplicationJob{" +
                "id_user=" + id_applicant +
                ", id_job=" + id_job +
                ", creation_date='" + creation_date + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", start_date='" + start_date + '\'' +
                ", contract_type='" + contract_type + '\'' +
                ", career_req='" + career_req + '\'' +
                ", id_company=" + id_company +
                ", name_company='" + name_company + '\'' +
                ", picture_company='" + picture_company + '\'' +
                '}'
    }
}