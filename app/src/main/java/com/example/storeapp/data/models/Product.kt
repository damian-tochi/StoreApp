package com.example.storeapp.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Product {

    @SerializedName("id")
    @Expose
    var id: String? = ""

    @SerializedName("title")
    @Expose
    var title: String? = ""

    @SerializedName("price")
    @Expose
    var price: String? = ""

    @SerializedName("category")
    @Expose
    var category: String? = ""

    @SerializedName("description")
    @Expose
    var description: String? = ""

    @SerializedName("image")
    @Expose
    var image: String? = ""

}