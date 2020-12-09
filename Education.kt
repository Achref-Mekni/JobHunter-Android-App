package com.esprit.jobhunter.Entity

class Education {
    var id = 0
    var inst_name: String? = null
    var degree: String? = null
    var domain: String? = null
    var start_date: String? = null
    var end_date: String? = null
    var result: String? = null
    var description: String? = null
    var cv_id = 0

    override fun toString(): String {
        return "Education{" +
                "id=" + id +
                ", inst_name='" + inst_name + '\'' +
                ", degree='" + degree + '\'' +
                ", domain='" + domain + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", result='" + result + '\'' +
                ", cv_id=" + cv_id +
                '}'
    }
}