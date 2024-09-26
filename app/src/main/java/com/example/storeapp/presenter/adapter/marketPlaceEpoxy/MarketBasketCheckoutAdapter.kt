package com.example.storeapp.presenter.adapter.marketPlaceEpoxy

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.storeapp.data.models.CartItemData
import com.example.storeapp.R
import com.example.storeapp.data.models.OnMarketCartItemSelection
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView
import java.text.NumberFormat
import java.util.Locale


class MarketBasketCheckoutAdapter(items : List<CartItemData>?, private val onMomentSelected: OnMarketCartItemSelection) : RecyclerView.Adapter<MarketBasketCheckoutViewHolder>() {

    private val localData = mutableListOf<CartItemData>()
    init {
        localData.addAll(items!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MarketBasketCheckoutViewHolder {
        return MarketBasketCheckoutViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.market_basket_checkout_adapter, parent, false))
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: MarketBasketCheckoutViewHolder, position: Int) {
        val info: CartItemData = localData[position]

        holder.title.text = info.name
        holder.weight.text = info.description

        val orderPrice = info.orderPrice?.toDouble()

        holder.itemCost.text = "₦" + orderPrice?.let { formatNumber(it) }

        if (info.image.isNotEmpty()) {
            Glide.with(holder.image.context).load(info.image).placeholder(R.drawable.image_not_available).into(holder.image)
        } else {
            Glide.with(holder.image.context).load(R.drawable.image_not_available).into(holder.image)
        }

        holder.cropProdInfoLay.setOnClickListener {
           onMomentSelected.onMarketItemSelected("view_detail", position, info)
        }

        holder.remove.setOnClickListener {
            onMomentSelected.onMarketItemSelected("remove_item", position, info)
        }

    }

    override fun getItemCount(): Int {
        return localData.size
    }

    fun refresh(localData: List<CartItemData>) {
        val diffCallback = MarketBasketCheckoutDiffCall(this.localData, localData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.localData.clear()
        this.localData.addAll(localData)
        diffResult.dispatchUpdatesTo(this)
    }

}

class MarketBasketCheckoutDiffCall(
    private val oldList: List<CartItemData>,
    private val newList: List<CartItemData>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}

class MarketBasketCheckoutViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val cropProdInfoLay = view.findViewById<LinearLayout>(R.id.crop_block_parent_layout)
    val remove = view.findViewById<ImageButton>(R.id.remove_item)
    val title = view.findViewById<TextView>(R.id.item_title_text)
    val weight = view.findViewById<TextView>(R.id.weight_text)
    val image = view.findViewById<PorterShapeImageView>(R.id.market_item_icon)
    val itemCost = view.findViewById<TextView>(R.id.item_cost_text)
}

fun formatNumber(amount: Double): String {
    val formatter: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
    formatter.maximumFractionDigits = 2
    formatter.minimumFractionDigits = 2
    return formatter.format(amount)
}

