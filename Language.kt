package com.esprit.jobhunter.Entity

class Language {
    var id = 0
    var label: String? = null
    var level: String? = null
    var cv_id = 0

    constructor() {}
    constructor(id: Int, label: String?, level: String?, cv_id: Int) {
        this.id = id
        this.label = label
        this.level = level
        this.cv_id = cv_id
    }

    override fun toString(): String {
        return "Language{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", level='" + level + '\'' +
                ", cv_id=" + cv_id +
                '}'
    }
}