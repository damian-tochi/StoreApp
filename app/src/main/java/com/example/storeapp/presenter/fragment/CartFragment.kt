package com.example.storeapp.presenter.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeapp.R
import com.example.storeapp.config.SessionState
import com.example.storeapp.data.models.CartItemData
import com.example.storeapp.data.models.OnMarketCartItemSelection
import com.example.storeapp.databinding.FragmentCartBinding
import com.example.storeapp.databinding.FragmentDashboardBinding
import com.example.storeapp.presenter.MainActivity
import com.example.storeapp.presenter.adapter.marketPlaceEpoxy.MarketBasketCheckoutAdapter
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private var basketItems: ArrayList<CartItemData>? = arrayListOf()
    private var restorationItems: ArrayList<CartItemData>? = arrayListOf()
    private lateinit var adapter: MarketBasketCheckoutAdapter
    private var globalSum: String = "0";

    /** Init & Bind view **/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        val view = binding.root
        initView()
        return view
    }

    /** Init layout components **/
    private fun initView() {
        loadData()
    }

    /** Parse data to UI **/
    private fun loadData() {
        binding.cartItemsRecycler.layoutManager = LinearLayoutManager(context)
        binding.cartItemsRecycler.setHasFixedSize(false)
        binding.cartItemsRecycler.isNestedScrollingEnabled = false
        binding.proceed.setOnClickListener {
            checkoutCart()
        }

        basketItems = (activity as MainActivity).cartItems

        var sum: Double = 0.0

        if (basketItems?.size!! > 0) {
            hideEmpty()

            for (i in 0 until basketItems?.size!!) {
                val priceDouble = basketItems!![i].price!!.toDouble()
                sum += priceDouble
            }

            if (binding.cartItemsRecycler.adapter != null) {
                adapter.refresh(basketItems!!)
            } else {
                adapter = MarketBasketCheckoutAdapter(basketItems, object : OnMarketCartItemSelection{
                    override fun onMarketItemSelected(action: String, position: Int, dataList: CartItemData) {
                        if (TextUtils.equals("remove_item", action)) {
                            if (basketItems?.size!! >= position) {
                                basketItems?.removeAt(position)
                                loadData()
                            }
                        }
                    }
                })
                binding.cartItemsRecycler.adapter = adapter
            }

        } else {
            showEmpty()
        }

        globalSum = "₦${formatNumber(sum)}"
        binding.textTotalCost.text = globalSum

    }

    private fun checkStoredCart() {
        SessionState.instance.loadUserCart(requireContext())
        SessionState.instance.loadUserCartRestore(requireContext())
        val prevCart = SessionState.instance.restorationItems
        if (prevCart.size > 0) {
            restorationItems?.clear()
            restorationItems?.addAll(prevCart)
            binding.restoreBtn.visibility = View.VISIBLE
            binding.restoreBtn.setOnClickListener {
                restorePrevBasket()
            }
        } else {
            binding.restoreBtn.visibility = View.GONE
        }
    }

    /** No cart value state **/
    private fun showEmpty() {
        checkStoredCart()
        binding.marketItemsUnavailable.visibility = View.VISIBLE
        binding.marketItemsAvailable.visibility = View.GONE
        binding.storeBasketEmpty.progress = 0.0F
        binding.storeBasketEmpty.pauseAnimation()
        binding.storeBasketEmpty.playAnimation()
        binding.textPrice.visibility = View.GONE
        binding.textTotalCost.visibility = View.GONE
    }

    /** Cart not empty state **/
    private fun hideEmpty() {
        if (binding.storeBasketEmpty.isAnimating) {
            binding.storeBasketEmpty.cancelAnimation()
        }
        binding.marketItemsUnavailable.visibility = View.GONE
        binding.marketItemsAvailable.visibility = View.VISIBLE
        binding.textPrice.visibility = View.VISIBLE
        binding.textTotalCost.visibility = View.VISIBLE
    }

    /** Restore previous cart **/
    private fun restorePrevBasket() {
        try {
            if (restorationItems?.size!! > 0) {
                basketItems?.clear()
                basketItems?.addAll(restorationItems!!)
                loadData()
            } else {
                Toast.makeText(
                    activity,
                    "Restoration Failed " + restorationItems?.size + " | " + (activity as MainActivity).cartItems.size,
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (_: Exception) {}
    }

    /** Currency number format **/
    private fun formatNumber(amount: Double): String {
        val formatter: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 2
        return formatter.format(amount)
    }

    /** Complete checkout **/
    private fun checkoutCart() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Pay $globalSum for selected items")
            .setPositiveButton("Yes") { _, _ ->
                basketItems?.clear()
                restorationItems?.clear()
                (activity as MainActivity).checkoutComplete()
                loadData()
            }
            .setNegativeButton("No", null)
            .show()
    }


}