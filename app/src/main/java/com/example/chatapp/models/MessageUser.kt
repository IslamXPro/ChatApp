package com.example.chatapp.models

import java.util.*

class MessageUser {
    var id:String? = null
    var message:String? = null
    var fromUid:String? = null
    var toUid:String? = null
    var date = "${Date().hours}:${Date().minutes}"



    constructor()
    constructor(id: String?, message: String?, fromUid: String?, toUid: String?) {
        this.id = id
        this.message = message
        this.fromUid = fromUid
        this.toUid = toUid
    }


}