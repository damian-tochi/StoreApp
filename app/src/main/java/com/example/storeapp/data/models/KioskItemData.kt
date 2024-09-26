package com.example.storeapp.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CartItemData {

    @SerializedName("type")
    @Expose
    var name: String? = ""

    @SerializedName("id")
    @Expose
    var id: String? = ""

    @SerializedName("subject")
    @Expose
    var subject: String? = ""

    @SerializedName("description")
    @Expose
    var description: String? = ""

    @SerializedName("image")
    @Expose
    var image: String = ""

    @SerializedName("quantity")
    @Expose
    var quantity: String? = ""

    @SerializedName("price")
    @Expose
    var price: String? = ""

    @SerializedName("orderQuantity")
    @Expose
    var orderQuantity: String? = ""

    @SerializedName("orderPrice")
    @Expose
    var orderPrice: String? = ""

    @SerializedName("regularPrice")
    @Expose
    var regularPrice: String? = ""

    @SerializedName("color")
    @Expose
    var color: String? = ""

    @SerializedName("unit")
    @Expose
    var unitType: String? = ""


}