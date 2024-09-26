package com.example.storeapp.config


/** Global constants **/
class AppConstants {

    companion object {
        const val BASE_URL = "https://fakestoreapi.com/"
        const val PRODUCTS = "products"
        const val PREF_FILE = "pref_cache"

        enum class PREFERENCES(private val value: String) {
            USER_ID("user_id"),
            USER_EMAIL("user_email"),
            USER_FULL_NAME("user_fullname"),
            USER_CART("cart_file"),
            USER_CART_RESTORE("cart_file_restore"),
            RESP_FILE("resp_file"),
            LOGIN_STATUS("LoginStatus"),
            IS_FIRST_HOME("is_first_home");

            override fun toString(): String {
                return this.value
            }
        }
    }
}