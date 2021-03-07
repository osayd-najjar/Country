package com.osayd.country.di.net

import com.osayd.country.di.model.Country
import retrofit2.Call
import retrofit2.http.GET

interface CountryApi {

    @GET("v2/all")
    fun getCountryList(): Call<List<Country>>
}