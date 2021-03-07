package com.osayd.country.ui.list

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.osayd.country.R
import com.osayd.country.databinding.ListItemCountryBinding
import com.osayd.country.di.model.Country

class CountryListViewHolder(
    private val binding: ListItemCountryBinding,
    val onAddToFavoriteCountries: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val context: Context = binding.root.context

    init {
        binding.addToFavoriteIcon.setOnClickListener { onAddToFavoriteCountries(adapterPosition) }
    }

    fun bind(country: Country) {
        val countryFlag = binding.countryFlagImageView
        binding.country = country
        Glide.with(context)
            .load(country.flags[1])
            .into(countryFlag)

        if (country.isFavorite) {
            binding.addToFavoriteIcon.setColorFilter(ContextCompat.getColor(context, R.color.red))
        } else {
            binding.addToFavoriteIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray2))
        }
    }
}