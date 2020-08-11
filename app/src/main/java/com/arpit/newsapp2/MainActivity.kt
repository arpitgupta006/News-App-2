package com.arpit.newsapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val base_url = "https://newsapi.org/v2/"
class MainActivity : AppCompatActivity() {

    lateinit var  countDownTimer: CountDownTimer

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeAPIRequest()
    }
    private fun fadeInFromBlack(){
        viewScreen.animate().apply {
            alpha(0f)
            duration=3000
        }.start()
    }

    private fun setRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = RecyclerAdapter(titlesList , descList , imagesList , linksList)
    }
    private fun addToList(title:String , description:String , image:String , link:String){
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)
        linksList.add(link)

    }

    private fun makeAPIRequest() {
        progressBar.visibility = View.VISIBLE
        val api = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getNews()

                for (article in response.articles!!){
                    Log.i("Main Activity" , "Result = $article")
                    addToList(article.title.toString(), article.description.toString() , article.urlToImage.toString() , article.url.toString())
                }
                withContext(Dispatchers.Main){
                    setRecyclerView()
                    fadeInFromBlack()
                    progressBar.visibility = View.GONE
                }
            }catch (e :Exception){
                Log.e("Main Activity" , e.toString())
                withContext(Dispatchers.Main){
                    attemptRequestAgain()
                }
            }
        }
    }

    private fun attemptRequestAgain() {
        countDownTimer = object :CountDownTimer(5*1000, 1000){
            override fun onFinish() {
                makeAPIRequest()
                countDownTimer.cancel()
            }

            override fun onTick(millisUntilFinished: Long) {
               Log.i("Main Activity" , "Could not retrieve data.... Trying Again in ${millisUntilFinished/1000} seconds")
            }

        }
        countDownTimer.start()
    }
}

//https://api.currentsapi.services/v1/latest-news?language=us&apiKey=EqiNHkPfbjHx_57YxLru4AjCmGQmbr7qX74IIlRdvjfesvua