package com.example.storeapp.presenter.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.storeapp.databinding.FragmentUserBinding
import com.example.storeapp.presenter.MainActivity
import com.example.storeapp.config.SessionState
import com.example.storeapp.presenter.OnboardActivity


class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    /** Init & Bind view **/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root
        getUser()
        return view
    }

    /** Prepare UI components **/
    private fun getUser() {
        SessionState.instance.readValuesFromPreferences(requireContext())
        val fullName = SessionState.instance.fullname
        val email = SessionState.instance.emailAddress

        binding.userName.text = fullName
        binding.userEmail.text = email
        binding.walletBalance.text = "Lagos, Nigeria"

        binding.logoutButton.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    /** Confirm logout **/
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                (activity as MainActivity).logout()
            }
            .setNegativeButton("No", null)
            .show()
    }

}