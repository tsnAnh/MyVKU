package dev.tsnanh.vku.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appbarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        navController = findNavController(R.id.fragment)
        appbarConfig = AppBarConfiguration(
            setOf(
                R.id.navigation_news,
                R.id.navigation_forum,
                R.id.navigation_options
            )
        )
        setupActionBarWithNavController(navController, appbarConfig)
        binding.bottomNavView.setupWithNavController(navController)
        binding.bottomNavView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_forum -> navController.navigate(
                R.id.navigation_forum
            )
            R.id.navigation_news -> navController.navigate(
                R.id.navigation_news
            )
            R.id.navigation_options -> navController.navigate(
                R.id.navigation_options
            )
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}