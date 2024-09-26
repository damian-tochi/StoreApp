package com.example.storeapp.data.models

interface OnMarketCartItemSelection {
    fun onMarketItemSelected(action: String, position: Int, dataList: CartItemData)
}