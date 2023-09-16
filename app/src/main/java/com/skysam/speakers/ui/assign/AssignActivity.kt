package com.skysam.speakers.ui.assign

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.skysam.speakers.R
import com.skysam.speakers.databinding.ActivityAssignBinding

class AssignActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAssignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_assign) as NavHostFragment
        navHostFragment.navController
    }
}