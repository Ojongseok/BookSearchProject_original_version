package com.example.practice2.ui.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practice2.R
import com.example.practice2.data.db.BookSearchDatabase
import com.example.practice2.data.repository.BookSearchRepositoryImpl
import com.example.practice2.databinding.ActivityMainBinding
import com.example.practice2.ui.viewmodel.BookSearchViewModel
import com.example.practice2.ui.viewmodel.BookSearchViewModelFactory
import com.example.practice2.util.Constants.DATASTORE_NAME

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var viewModel: BookSearchViewModel

    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    // dataStore 선언 방법
    private val Context.dataStroe by preferencesDataStore(DATASTORE_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val database = BookSearchDatabase.getInstance(this)
        val bookSearchRepository = BookSearchRepositoryImpl(database,dataStroe)
        val factory = BookSearchViewModelFactory(bookSearchRepository)
        viewModel = ViewModelProvider(this,factory) [BookSearchViewModel::class.java]

        setupJetpackNavigation()
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
}