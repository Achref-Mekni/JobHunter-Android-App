package com.esprit.jobhunter.Entity

class Message {
    var id = 0
    var sender = 0
    var receiver = 0
    var content: String? = null
    var sent_at: String? = null
    var sender_name: String? = null
    var receiver_name: String? = null
    var test = 0
    var nickname: String? = null
    var message: String? = null

    constructor() {}
    constructor(id: Int, sender: Int, receiver: Int, content: String?, sent_at: String?, sender_name: String?, receiver_name: String?, nickname: String?, message: String?) {
        this.id = id
        this.sender = sender
        this.receiver = receiver
        this.content = content
        this.sent_at = sent_at
        this.sender_name = sender_name
        this.receiver_name = receiver_name
        this.nickname = nickname
        this.message = message
    }

    constructor(nickname: String?, message: String?) {
        this.nickname = nickname
        this.message = message
    }

    override fun toString(): String {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", content='" + content + '\'' +
                ", sent_at=" + sent_at +
                ", sender_name='" + sender_name + '\'' +
                ", receiver_name='" + receiver_name + '\'' +
                ", test=" + test +
                ", nickname='" + nickname + '\'' +
                ", message='" + message + '\'' +
                '}'
    }
}