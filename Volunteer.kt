package com.esprit.jobhunter.Entity

class Volunteer {
    var id = 0
    var organisation: String? = null
    var start_date: String? = null
    var end_date: String? = null
    var role: String? = null
    var still_going = 0
    var cv_id = 0

    constructor() {}
    constructor(id: Int, organisation: String?, start_date: String?, end_date: String?, role: String?, still_going: Int, cv_id: Int) {
        this.id = id
        this.organisation = organisation
        this.start_date = start_date
        this.end_date = end_date
        this.role = role
        this.still_going = still_going
        this.cv_id = cv_id
    }

    override fun toString(): String {
        return "Volunteer{" +
                "id=" + id +
                ", organisation='" + organisation + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", role='" + role + '\'' +
                ", still_going=" + still_going +
                ", cv_id=" + cv_id +
                '}'
    }
}