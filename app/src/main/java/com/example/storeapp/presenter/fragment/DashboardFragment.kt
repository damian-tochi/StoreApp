package com.example.storeapp.presenter.fragment

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.example.storeapp.config.CircleAnimationUtil
import com.example.storeapp.data.RetrofitController
import com.example.storeapp.data.models.CartItemData
import com.example.storeapp.data.models.Product
import com.example.storeapp.databinding.FragmentDashboardBinding
import com.example.storeapp.presenter.MainActivity
import com.grynd.gryndstaging.adapters.marketPlaceEpoxy.marketItemLayoutView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private var cartItemCount by Delegates.notNull<Int>()

    /** Init & Bind view **/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root
        initView()
        return view
    }

    /** Retrieve network data
     * NOT advisable here tho.
     **/
    private fun initView() {
        refreshCount()
        loadMarketProducts()
    }

    /** Network call function to get product list **/
    private fun loadMarketProducts() {
        try {
            binding.marketLoading.visibility = View.VISIBLE
            RetrofitController.getAllProducts(object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    if (isAdded && isVisible) {
                        binding.marketLoading.visibility = View.GONE
                    }
                    Toast.makeText(requireContext(), "Failure: $t", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (isAdded && isVisible) {
                        binding.marketLoading.visibility = View.GONE
                        if (response.body() != null) {
                            val mResponse: List<Product> = response.body()!!
                            loadProducts(mResponse)
                        } else {
                            val errorObject = try { JSONObject(response.errorBody()!!.charStream().readText()).get("message").toString()
                            } catch (e: Exception) {
                                java.lang.StringBuilder(response.errorBody()!!.charStream().readText())
                            }
                        }
                    }
                }
            }, requireContext())
        } catch (e: Exception) {
            if (isAdded && isVisible) {
                binding.marketLoading.visibility = View.GONE
            }
            Toast.makeText(requireContext(), "Caught: $e", Toast.LENGTH_SHORT).show()
        }

    }

    /** Parse response payload and update UI **/
    private fun loadProducts(resp: List<Product>) {
        if (binding.shimmerLayoutListings.isVisible) {
            binding.shimmerLayoutListings.stopShimmer()
            binding.shimmerLayoutListings.visibility = View.GONE
        }

        if (resp.size > 0) {
            val productList: ArrayList<CartItemData> = arrayListOf()

            for (i in 0 until resp.size) {
                val exclusiveItem3 = CartItemData()
                exclusiveItem3.id = resp[i].id
                exclusiveItem3.image = resp[i].image.toString()
                exclusiveItem3.name = resp[i].title
                exclusiveItem3.price = resp[i].price.toString()
                exclusiveItem3.quantity = "20"
                exclusiveItem3.orderPrice = resp[i].price.toString()
                exclusiveItem3.orderQuantity = "1"
                exclusiveItem3.unitType = "N"
                exclusiveItem3.subject = resp[i].category
                exclusiveItem3.description = resp[i].description
                exclusiveItem3.regularPrice = resp[i].price
                exclusiveItem3.color = resp[i].title
                productList.add(exclusiveItem3)
            }
            if (productList.size > 0) {
                binding.marketItemsRecycler.buildModelsWith(object : EpoxyRecyclerView.ModelBuilderCallback {
                override fun buildModels(controller: EpoxyController) {
                        controller.apply {
                            if (productList.size > 0) {
                                productList.forEachIndexed { index, data ->
                                    context?.let {
                                        controller.marketItemLayoutView {
                                            id(data.toString())
                                            name("${data.name}")
                                            image(data.image)
                                            name(data.name!!)
                                            size(data.quantity!!)
                                            price(data.price!!)
                                            (if (!TextUtils.isEmpty(data.regularPrice)) {
                                                if (TextUtils.equals(data.regularPrice, data.price)) {
                                                    "none"
                                                } else {
                                                    data.regularPrice
                                                }
                                            } else { "none" })?.let { it1 ->
                                                discountPrice(it1)
                                            }
                                            itemClickListener { _ -> viewItem(data) }
                                            addCartClickListener { v -> makeFlyAnimation(v as ImageView, data) }

                                            spanSizeOverride { totalSpanCount, position, itemCount -> totalSpanCount / 2 }
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    /** Function to view item **/
    private fun viewItem(item: CartItemData) {}

    /** Add-to-cart animation **/
    private fun makeFlyAnimation(targetView: ImageView, dataList: CartItemData) {
        val destView = binding.basketImage
        CircleAnimationUtil().attachActivity(requireActivity()).setTargetView(targetView).setMoveDuration(800)
            .setDestView(destView).setAnimationListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    addItemToCart(dataList)
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            }).startAnimation()
    }

    /** Add to cart function **/
    private fun addItemToCart(data : CartItemData) {
        try {
                cartItemCount = (activity as MainActivity).cartItemCount

                cartItemCount += 1
                if (isAdded && isVisible) {
                    binding.itemCountTxt.text = cartItemCount.toString()
                }
                (activity as MainActivity).cartItemCount = cartItemCount
                (activity as MainActivity).cartItems.add(data)
                (activity as MainActivity).addItemToCart()
            } catch (_ : Exception) { }

    }

    private fun refreshCount() {
        try {
            cartItemCount = (activity as MainActivity).cartItems.size
            binding.itemCountTxt.text = cartItemCount.toString()
        } catch (_: Exception) {}
    }


}