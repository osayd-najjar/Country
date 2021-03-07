package com.osayd.country.di.model

import com.google.gson.annotations.SerializedName

data class Country(
    val name: String,
    @SerializedName("alpha2Code")
    val id: String,
    val population: String,
    val flags: List<String>,
    var isFavorite: Boolean
)
