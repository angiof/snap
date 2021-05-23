package com.example

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.example.snap.AddFragment
import com.example.snap.HomeFragment
import com.example.snap.ProfileFragment
import com.example.snap.R
import com.example.snap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinder: ActivityMainBinding
    private lateinit var mFfragrmentManager: FragmentManager
    private lateinit var mActiveFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinder.root)
        setNav()
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
                    mFfragrmentManager.beginTransaction().hide(mActiveFragment).show(profileFragment)
                        .commit()
                    mActiveFragment = profileFragment
                    true
                }
                else -> false
            }
        }

    }


}