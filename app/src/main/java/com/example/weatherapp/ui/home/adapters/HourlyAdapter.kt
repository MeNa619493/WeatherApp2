package com.example.weatherapp.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemHourlyBinding
import com.example.weatherapp.model.pojo.Hourly
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.utils.Constants.getTemperatureUnit


class HourlyAdapter(private val context: Context, private val language: String) :
    ListAdapter<Hourly, HourlyAdapter.MyViewHolder>(
        HourlyDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), context, language)
    }

    class MyViewHolder(private val binding: ItemHourlyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hourly: Hourly, context: Context, language: String) {
            binding.apply {
                tvHour.text = hourly.dt?.let { Constants.convertLongToTime(it, language) }
                val strFormat: String = context.getString(
                    R.string.hourly,
                    hourly.temp?.toInt(),
                    getTemperatureUnit(context)
                )
                tvTemp.text = strFormat

                Glide
                    .with(binding.root)
                    .load("https://openweathermap.org/img/wn/${hourly.weather?.get(0)?.icon ?: ""}.png?fbclid=IwAR2Nk0UQ5anrxUCLubc6bRZTqN65qD2TE2Rk0EvU6-609jRf_HuHPAnP-YE")
                    .into(ivHourDesc)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHourlyBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    class HourlyDiffCallback : DiffUtil.ItemCallback<Hourly>() {
        override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem == newItem
        }
    }
}