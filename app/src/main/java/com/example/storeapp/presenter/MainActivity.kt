package com.example.storeapp.presenter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.example.storeapp.R
import com.example.storeapp.config.AppConstants
import com.example.storeapp.config.SessionState
import com.example.storeapp.data.models.CartItemData
import com.example.storeapp.databinding.ActivityMainBinding
import com.example.storeapp.presenter.fragment.CartFragment
import com.example.storeapp.presenter.fragment.DashboardFragment
import com.example.storeapp.presenter.fragment.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private var dashboardFragment = DashboardFragment()
    private var cartFragment = CartFragment()
    private var userFragment = UserFragment()
    private var mChatBadgeTextView: AppCompatTextView? = null
    var cartItemCount = 0
    var cartItems: ArrayList<CartItemData> = arrayListOf()

    /** Init & Bind view **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
    }

    /** Init layout contents **/
    private fun initView() {
        navView = binding.navView

        commitFragment(0)

        navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_dashboard -> {
                    commitFragment(0)
                }
                R.id.navigation_cart -> {
                    removeBadgeFromIcon()
                    commitFragment(1)
                }
                R.id.navigation_user -> {
                    commitFragment(2)
                }
            }
            true
        }


    }

    /** Navigate to bottom nav item **/
    private fun commitFragment(tabPosition: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when (tabPosition) {
            0 -> fragmentTransaction.replace(
                R.id.nav_host_fragment,
                dashboardFragment
            )

            1 -> fragmentTransaction.replace(
                R.id.nav_host_fragment,
                cartFragment
            )

            2 -> fragmentTransaction.replace(
                R.id.nav_host_fragment,
                userFragment
            )

            else -> {
            }
        }
        fragmentTransaction.commit()
    }

    /** Add cart item count to bottom nav item **/
    @SuppressLint("RestrictedApi")
    private fun showBadgeOnIcon(count: String) {
        val bottomNavigationMenuView = binding.navView.getChildAt(0) as BottomNavigationMenuView
        val itemView = bottomNavigationMenuView.getChildAt(1) as BottomNavigationItemView
        val badge = LayoutInflater.from(this).inflate(R.layout.badge_layout, itemView, true)
        val badgeTextView = badge.findViewById<AppCompatTextView>(R.id.notifications_badge)
        if (badgeTextView != null) {
            mChatBadgeTextView = badgeTextView
            mChatBadgeTextView?.visibility = View.VISIBLE
            mChatBadgeTextView?.text = count
        }
    }

    /** Remove cart item count to bottom nav item **/
    private fun removeBadgeFromIcon() {
        mChatBadgeTextView?.visibility = View.GONE
    }

    /** Add cart item Fragment Listener **/
    fun addItemToCart() {
        showBadgeOnIcon(cartItems.size.toString())
        SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.USER_CART.toString(), Gson().toJson(cartItems))
        SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.USER_CART_RESTORE.toString(), Gson().toJson(cartItems))
    }

    /** Logout user **/
    fun logout() {
        SessionState.instance.clearSession(this)
        val intent = Intent(this, OnboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun checkoutComplete() {
        cartItems.clear()
        SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.USER_CART.toString(), Gson().toJson(cartItems))
        SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.USER_CART_RESTORE.toString(), Gson().toJson(cartItems))

    }

}

