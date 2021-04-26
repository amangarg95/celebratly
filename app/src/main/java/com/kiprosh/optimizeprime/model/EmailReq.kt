package com.example.optimizeprimeandroidapp.model

import com.google.gson.annotations.SerializedName

class EmailReq {

    @SerializedName("user[email]")
    var email: String = ""
}
