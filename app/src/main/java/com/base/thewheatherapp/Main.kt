package com.base.thewheatherapp

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp")
    val temp: Double,
    val feels_like: Double,

@SerializedName("temp_min")
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)