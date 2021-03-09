package com.osayd.country.di.country

import com.osayd.country.di.net.CountryApi
import javax.inject.Inject

class CountryManager @Inject constructor() {

    @Inject
    lateinit var countryApi: CountryApi

    fun getCountryList() = countryApi.getCountryList()
}