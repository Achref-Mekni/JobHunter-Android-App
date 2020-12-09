package com.esprit.jobhunter.Entity

class User_Job {
    var id_job = 0
    var id_user = 0
    var creation_date: String? = null


    constructor() {}
    constructor(id_job: Int, id_user: Int, creation_date: String?) {
        this.id_job = id_job
        this.id_user = id_user
        this.creation_date = creation_date

    }

}