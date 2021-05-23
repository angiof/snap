package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.snap.AddFragment
import com.example.snap.HomeFragment
import com.example.snap.ProfileFragment
import com.example.snap.R
import com.example.snap.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mBinder: ActivityMainBinding
    private lateinit var mFfragrmentManager: FragmentManager
    private lateinit var mActiveFragment: Fragment
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private var mFireAuth: FirebaseAuth? = null
    private val RC_SIGN_IN=21


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinder.root)
        setNav()

        setUth()
    }

    private fun setUth() {
        mFireAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user == null)
                startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(
                            Arrays.asList(
                                AuthUI.IdpConfig.EmailBuilder().build(),
                                AuthUI.IdpConfig.GoogleBuilder().build())
                        ).build(),RC_SIGN_IN)
        }
    }

    private fun setNav() {

        mFfragrmentManager = supportFragmentManager

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val addFragment = AddFragment()

        mActiveFragment = homeFragment

        mFfragrmentManager = supportFragmentManager.also {
            it.beginTransaction().add(
                R.id.nav_host_fragment_container,
                profileFragment,
                ProfileFragment::javaClass.name
            ).hide(profileFragment).commit()
        }

        mFfragrmentManager = supportFragmentManager.also {
            it.beginTransaction()
                .add(R.id.nav_host_fragment_container, addFragment, AddFragment::javaClass.name)
                .hide(addFragment).commit()
        }

        mFfragrmentManager = supportFragmentManager.also {
            it.beginTransaction()
                .add(R.id.nav_host_fragment_container, homeFragment, HomeFragment::javaClass.name)
                .commit()
        }

        mBinder.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    mFfragrmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment)
                        .commit()
                    mActiveFragment = homeFragment
                    true
                }
                R.id.navigation_adder -> {
                    mFfragrmentManager.beginTransaction().hide(mActiveFragment).show(addFragment)
                        .commit()
                    mActiveFragment = addFragment
                    true
                }
                R.id.navigation_profile -> {
                    mFfragrmentManager.beginTransaction().hide(mActiveFragment)
                        .show(profileFragment)
                        .commit()
                    mActiveFragment = profileFragment
                    true
                }
                else -> false
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mFireAuth?.addAuthStateListener(mAuthListener)
    }

    override fun onPause() {
        super.onPause()
        mFireAuth?.removeAuthStateListener(mAuthListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==RC_SIGN_IN){
            if (resultCode== RESULT_OK){
                Toast.makeText(this, "welcome amico mio", Toast.LENGTH_SHORT).show()
            }else{

                if (IdpResponse.fromResultIntent(data)==null){
                    finish()
                }

            }
        }
    }

}