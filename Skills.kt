package com.esprit.jobhunter.Entity

class Skills {
    var id = 0
    var label: String? = null
    var cv_id = 0

    constructor() {}
    constructor(id: Int, label: String?, cv_id: Int) {
        this.id = id
        this.label = label
        this.cv_id = cv_id
    }

    override fun toString(): String {
        return "Skills{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", cv_id=" + cv_id +
                '}'
    }
}