package com.example.practice2.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practice2.R
import com.example.practice2.data.repository.BookSearchRepositoryImpl
import com.example.practice2.databinding.ActivityMainBinding
import com.example.practice2.ui.viewmodel.BookSearchViewModel
import com.example.practice2.ui.viewmodel.BookSearchViewModelFactory

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val bookSearchRepository = BookSearchRepositoryImpl()
    private val factory = BookSearchViewModelFactory(bookSearchRepository)
    val viewModel : BookSearchViewModel by viewModels() {factory}

    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupJetpackNavigation()

//        setupBottomNavigationView()
//        if (savedInstanceState == null) {
//            binding.bottomNavigationView.selectedItemId = R.id.fragment_search
//        }
    }
    private fun setupJetpackNavigation() {
        val host = supportFragmentManager.findFragmentById(R.id.booksearch_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_favorite, R.id.fragment_search, R.id.fragment_settings
            )
        )

        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    fun setupBottomNavigationView() {
//        binding.bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.fragment_search -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, SearchFragment()).commit()
//                    true
//                }
//                R.id.fragment_favorite -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, FavoriteFragment()).commit()
//                    true
//                }
//                R.id.fragment_settings -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, SettingsFragment()).commit()
//                    true
//                }
//                else -> false
//            }
//        }
//    }
}