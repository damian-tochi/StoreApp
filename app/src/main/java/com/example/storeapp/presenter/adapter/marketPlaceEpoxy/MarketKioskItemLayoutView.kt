package com.grynd.gryndstaging.adapters.marketPlaceEpoxy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.bumptech.glide.Glide
import com.example.storeapp.R
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MarketItemLayoutView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?= null,
    defStyle:Int = 0

): LinearLayout(context,attributeSet,defStyle) {
    private val rootLayout:LinearLayout
    private val nameTextView:TextView
    private val sizeTextView:TextView
    private val priceTextView:TextView
    private val oldPriceTextView:TextView
    private val imageView:PorterShapeImageView
    private val addCartImg:ImageButton
    private val noAddCartImg:ImageButton
    private val outOfStockCover:View
    private val outOfStockTxt:TextView

    init {
        View.inflate(context,R.layout.market_item_adapter,this)
        rootLayout = findViewById(R.id.market_item_parent_layout)
        nameTextView = findViewById(R.id.market_item_name_text)
        sizeTextView = findViewById(R.id.market_item_sizeText)
        priceTextView = findViewById(R.id.market_item_price_text)
        oldPriceTextView = findViewById(R.id.market_item_discount_text)
        imageView = findViewById(R.id.market_item_icon)
        addCartImg = findViewById(R.id.market_add_cart)
        noAddCartImg = findViewById(R.id.market_add_cart_inactive)
        outOfStockCover = findViewById(R.id.out_of_stock_view)
        outOfStockTxt = findViewById(R.id.out_of_stock_text)
    }

    @TextProp
    fun setName(name:CharSequence) {
        nameTextView.text = name
    }

    @SuppressLint("SetTextI18n")
    @TextProp
    fun setSize(status:CharSequence) {
        val str = status.toString().toInt()
        if (str < 1) {
            outOfStockCover.visibility = View.VISIBLE
            outOfStockCover.setBackgroundResource(R.color.translucent_white)
            outOfStockTxt.visibility = View.VISIBLE
            addCartImg.visibility = View.GONE
            noAddCartImg.visibility = View.VISIBLE
            sizeTextView.text = "0 Pcs"
            sizeTextView.visibility = View.GONE
        } else {
            outOfStockCover.visibility = View.GONE
            outOfStockCover.setBackgroundResource(R.color.full_transparent)
            outOfStockTxt.visibility = View.GONE
            addCartImg.visibility = View.VISIBLE
            noAddCartImg.visibility = View.GONE
            sizeTextView.text = status.toString() + " Pcs"
            sizeTextView.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    @TextProp
    fun setPrice(price:CharSequence) {
        priceTextView.text = "₦$price"
    }

    @TextProp
    fun setDiscountPrice(regularPrice:CharSequence) {
        if (TextUtils.equals(regularPrice, "none")) {
            oldPriceTextView.visibility = View.GONE
        } else {
            oldPriceTextView.visibility = View.VISIBLE
            oldPriceTextView.text = "₦$regularPrice"
            oldPriceTextView.showStrikeThrough(true)
        }
    }

    @CallbackProp
    fun setItemClickListener(listener: OnClickListener?) {
        rootLayout.setOnClickListener(listener)
    }

    @CallbackProp
    fun addCartClickListener(listener: OnClickListener?) {
        addCartImg.setOnClickListener(listener)
    }

    @ModelProp
    fun setImage(img:String) {
        if (img.isNotEmpty()) {
            Glide.with(context).load(img).placeholder(R.drawable.image_not_available).into(imageView)
        } else {
            Glide.with(context).load(R.drawable.image_not_available).into(imageView)
        }
    }

    fun TextView.showStrikeThrough(show: Boolean) {
        paintFlags =
            if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}