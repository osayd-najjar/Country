package com.osayd.country.ui

import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.osayd.country.di.country.CountryManager
import com.osayd.country.di.model.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CountryActivityViewModel @Inject constructor(countryManager: CountryManager) :
    ViewModel() {

    private var _countryListLiveData = MutableLiveData<List<Country>>()
    val countryListLiveData: LiveData<List<Country>> = _countryListLiveData

    private var _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    private var _isEmptyLiveData = MutableLiveData<Boolean>()
    val isEmptyLiveData: LiveData<Boolean> = _isEmptyLiveData

    private lateinit var countryList: List<Country>
    var favoriteCountriesList = mutableListOf<Country>()

    init {
        countryManager.getCountryList().enqueue(
            object : Callback<List<Country>> {
                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    _errorLiveData.value = t
                }

                override fun onResponse(
                    call: Call<List<Country>>, response: Response<List<Country>>
                ) {
                    if (response.isSuccessful) {
                        _countryListLiveData.value = response.body()
                        countryList = _countryListLiveData.value!!
                    }
                }
            }
        )
    }

    fun addToFavoriteList(position: Int) {
        val country = countryList[position]
        val newList = countryList.toMutableList()
        if (!country.isFavorite) {
            newList[position] = newList[position].copy(isFavorite = true)
            favoriteCountriesList.add(country.copy(isFavorite = true))
        } else {
            newList[position] = newList[position].copy(isFavorite = false)
            favoriteCountriesList.remove(country)
        }
        countryList = newList
        _countryListLiveData.value = countryList
    }

    fun removeFromFavorite(position: Int) {
        val country = favoriteCountriesList[position]
        val newList = favoriteCountriesList.toMutableList()
        newList.removeAt(position)

        for (i in countryList.indices) {
            if (countryList[i].id == country.id)
                (countryList as MutableList<Country>)[i] = country.copy(isFavorite = false)
        }
        favoriteCountriesList = newList
        _countryListLiveData.value = favoriteCountriesList
    }


    fun changeToFavoriteList() {
        _countryListLiveData.value = favoriteCountriesList
        if (favoriteCountriesList.isEmpty())
            _isEmptyLiveData.value = true
    }

    fun changeToCountriesList() {
        _countryListLiveData.value = countryList
        _isEmptyLiveData.value = false
    }

    fun showEmptyContentMessage(textView: TextView, recyclerView: RecyclerView) {
        if (_isEmptyLiveData.value == true) {
            textView.isVisible = true
            recyclerView.isVisible = false
        } else {
            textView.isVisible = false
            recyclerView.isVisible = true
        }
    }

    fun hideLoading(progressBar: ProgressBar, recyclerView: RecyclerView) {
        progressBar.isVisible = false
        recyclerView.isVisible = true
    }

    fun showFailureMessage(progressBar: ProgressBar, textView: TextView) {
        progressBar.isVisible = false
        textView.isVisible = true
    }
}