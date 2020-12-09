package com.esprit.jobhunter.Entity

class Offer {
    var id = 0
    var label: String? = null
    var cmp_pic: String? = null
    var cmp_name: String? = null
    var type: String? = null

    override fun toString(): String {
        return "Offer{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", cmp_pic='" + cmp_pic + '\'' +
                ", cmp_name='" + cmp_name + '\'' +
                ", type='" + type + '\'' +
                '}'
    }
}