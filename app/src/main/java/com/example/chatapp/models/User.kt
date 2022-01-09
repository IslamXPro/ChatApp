package com.example.chatapp.models

import android.net.Uri
import java.io.Serializable

class User : Serializable{
    var idToken:String? = null
    var displayName:String? = null
    var email:String? = null
    var data:String? = null
    var img:String? = null



    constructor()
    constructor(
        idToken: String?,
        displayName: String?,
        email: String?,
        img: Uri?
    ) {
        this.idToken = idToken
        this.displayName = displayName
        this.email = email
        this.img = img.toString()
    }

    override fun toString(): String {
        return "User(idToken=$idToken, displayName=$displayName, email=$email, data=$data, img=$img)"
    }


}