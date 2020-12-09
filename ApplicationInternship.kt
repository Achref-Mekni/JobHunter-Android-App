package com.esprit.jobhunter.Entity

class ApplicationInternship {
    var id_applicant = 0
    var id_job = 0
    var creation_date: String? = null
    var label: String? = null
    var description: String? = null
    var start_date: String? = null
    var educ_req: String? = null
    var duration: String? = null
    var id_company = 0
    var name_company: String? = null
    var picture_company: String? = null
    var is_bookmarked = false
    val type = "i"

    constructor() {}
    constructor(id_applicant: Int, id_job: Int, creation_date: String?, label: String?, description: String?, start_date: String?, educ_req: String?, duration: String?, id_company: Int, name_company: String?, picture_company: String?) {
        this.id_applicant = id_applicant
        this.id_job = id_job
        this.creation_date = creation_date
        this.label = label
        this.description = description
        this.start_date = start_date
        this.educ_req = educ_req
        this.duration = duration
        this.id_company = id_company
        this.name_company = name_company
        this.picture_company = picture_company
    }

    override fun toString(): String {
        return "ApplicationInternship{" +
                "id_applicant=" + id_applicant +
                ", id_job=" + id_job +
                ", creation_date='" + creation_date + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", start_date='" + start_date + '\'' +
                ", educ_req='" + educ_req + '\'' +
                ", duration='" + duration + '\'' +
                ", id_company=" + id_company +
                ", name_company='" + name_company + '\'' +
                ", picture_company='" + picture_company + '\'' +
                '}'
    }
}