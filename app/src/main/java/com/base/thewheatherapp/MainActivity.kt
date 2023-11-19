package com.base.thewheatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import com.base.thewheatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//e73078d533e5cf2df19f73785029d739


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("delhi")

        SearchCity();


    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData("$query")

                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
             return true
            }
        })
    }

    private fun fetchWeatherData(cityName:String) {
       val retrofit = Retrofit.Builder()
           .addConverterFactory(GsonConverterFactory.create())
           .baseUrl("https://api.openweathermap.org/data/2.5/")
           .build().create(ApiInterface::class.java)


        val response = retrofit.getWeatherData(cityName,"9b4b26bb9679522ed93fbba5c9a13bc6","metric")

        response.enqueue(object : Callback<WheatherApp>{

            override fun onResponse(call: Call<WheatherApp>, response: Response<WheatherApp>) {
                val responseBody = response.body()

                if(!response.isSuccessful){
                    Toast.makeText(applicationContext,"this is ",Toast.LENGTH_SHORT).show()
                }
                if(response.isSuccessful && responseBody!=null){

                    val temperature =  responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windspeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toString()
                    val sunSet =  responseBody.sys.sunset.toString()
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min


                    binding.temprature.text="$temperature °C"
                    binding.Wheather.text = "$condition"
                    binding.maxTemp.text = "Max Temp: $maxTemp °C"
                    binding.minTemp.text = "Min Temp: $minTemp °C"
                    binding.Humidity.text = "$humidity %"
                    binding.windSpeed.text = "$windspeed m/s"
                    binding.sunrise.text = "$sunRise"
                    binding.issunset.text = "$sunSet"
                    binding.sea.text="$seaLevel hpa"
                    binding.condtion.text  = condition
                    binding.day.text=dayName(System.currentTimeMillis())
                    binding.date.text=date()
                    binding.cityname.text="$cityName"



                    changeImageAccorodingToWheatherConditon(condition)



                }
            }

            private fun changeImageAccorodingToWheatherConditon(condition:String) {

                when(condition){

                    "Haze" , "Partly Clouds", "Clouds","Overcast","Mist","Foggy" ->{
                        binding.root.setBackgroundResource(R.drawable.colud_background)
                        binding.lottieAnimationView.setAnimation(R.raw.cloud)
                    }

                    "Clear Sky" , "Sunny", "Clear" ->{
                        binding.root.setBackgroundResource(R.drawable.sunny_background)
                        binding.lottieAnimationView.setAnimation(R.raw.sun)
                    }

                    "Light Rain" , "Drizzle", "Moderate Rain","Showers","Heavy Rain" ->{
                        binding.root.setBackgroundResource(R.drawable.rain_background)
                        binding.lottieAnimationView.setAnimation(R.raw.rain)
                    }

                    "Light Snow" , "Moderate Snow", "Heavy Snow","Blizzard" ->{
                        binding.root.setBackgroundResource(R.drawable.snow_background)
                        binding.lottieAnimationView.setAnimation(R.raw.snow)
                    }
                    else ->{
                        binding.root.setBackgroundResource(R.drawable.sunny_background)
                        binding.lottieAnimationView.setAnimation(R.raw.sun)
                    }

                }

                binding.lottieAnimationView.playAnimation()

            }

            override fun onFailure(call: Call<WheatherApp>, t: Throwable) {
                Log.d("TAGo","onResponse: $t")
            }

        })


    }

    private fun date(): String{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }



    fun dayName(timestamp: Long): String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}