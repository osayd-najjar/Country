package com.osayd.country.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.osayd.country.databinding.ListItemCountryBinding
import com.osayd.country.di.model.Country

class CountryListAdapter(val onAddToFavoriteCountries: (Int) -> Unit) :
    ListAdapter<Country, CountryListViewHolder>(object :
        DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Country, newItem: Country) =
            oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CountryListViewHolder(
            ListItemCountryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onAddToFavoriteCountries = { position ->
                onAddToFavoriteCountries(position)
            }
        )

    override fun onBindViewHolder(holder: CountryListViewHolder, position: Int) =
        holder.bind(getItem(position))
}