package com.osayd.country.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.osayd.country.R
import com.osayd.country.databinding.ActivityCountryBinding
import com.osayd.country.ui.list.CountryListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountryBinding
    private lateinit var countryListAdapter: CountryListAdapter
    private val viewModel: CountryActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_country)

        setupRecyclerView()
        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return true
    }

    @SuppressLint("NewApi")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.countryFavoriteIcon -> {
                if (countryListAdapter.currentList != viewModel.favoriteCountriesList) {
                    item.icon.setTint(ContextCompat.getColor(this, R.color.red))
                    viewModel.changeToFavoriteList()
                } else if (countryListAdapter.currentList == viewModel.favoriteCountriesList) {
                    item.icon.setTint(ContextCompat.getColor(this, R.color.gray2))
                    viewModel.changeToCountriesList()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.countryRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        countryListAdapter =
            CountryListAdapter(onAddToFavoriteCountries = { addToFavoriteCountries(it) })
        recyclerView.adapter = countryListAdapter
    }

    private fun setupViewModel() {
        binding.viewModel = this@CountryActivity.viewModel
        binding.lifecycleOwner = this@CountryActivity

        viewModel.countryListLiveData.observe(this) {
            viewModel.hideLoading(binding.progressbar, binding.countryRecyclerview)
            countryListAdapter.submitList(it)
        }

        viewModel.errorLiveData.observe(this) {
            viewModel.showFailureMessage(binding.progressbar, binding.failureMessage)
        }

        viewModel.isEmptyLiveData.observe(this) {
            viewModel.showEmptyContentMessage(
                binding.emptyContentMessage,
                binding.countryRecyclerview
            )
        }
    }

    private fun addToFavoriteCountries(position: Int) {
        if (countryListAdapter.currentList == viewModel.favoriteCountriesList) {
            viewModel.removeFromFavorite(position)
        } else {
            viewModel.addToFavoriteList(position)
        }
    }
}