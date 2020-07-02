package dev.tsnanh.vku.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityMainElearningBinding

/**
 * Elearning Activity
 * @author tsnAnh
 */
class ElearningMainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainElearningBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_elearning)

        navController = findNavController(R.id.fragment_elearning)

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_elearning_home ->
                navController.navigate(R.id.navigation_elearning_home)
            R.id.navigation_elearning_calendar ->
                navController.navigate(R.id.navigation_elearning_calendar)
            R.id.navigation_elearning_message ->
                navController.navigate(R.id.navigation_elearning_message)
            R.id.navigation_elearning_notification ->
                navController.navigate(R.id.navigation_elearning_notification)
            R.id.navigation_elearning_option ->
                navController.navigate(R.id.navigation_elearning_option)
        }

        return true
    }
}