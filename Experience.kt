package com.esprit.jobhunter.Entity

class Experience {
    var id = 0
    var label: String? = null
    var start_date: String? = null
    var end_date: String? = null
    var description: String? = null
    var still_going = 0
    var establishmentName: String? = null
    var cv_id = 0

    constructor() {}
    constructor(id: Int, label: String?, start_date: String?, end_date: String?, description: String?, still_going: Int, establishmentName: String?, cv_id: Int) {
        this.id = id
        this.label = label
        this.start_date = start_date
        this.end_date = end_date
        this.description = description
        this.still_going = still_going
        this.establishmentName = establishmentName
        this.cv_id = cv_id
    }

    override fun toString(): String {
        return "Experience{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", description='" + description + '\'' +
                ", still_going=" + still_going +
                ", establishmentName='" + establishmentName + '\'' +
                ", cv_id=" + cv_id +
                '}'
    }
}