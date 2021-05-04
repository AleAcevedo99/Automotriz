package com.example.automotriz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.automotriz.Adapters.NewsAdapter
import com.example.automotriz.Constants.Constants
import com.example.automotriz.Entity.EntityNews
import com.example.automotriz.databinding.ActivityDetailNewsBinding
import com.example.automotriz.databinding.ActivityNewsBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DetailNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsBinding
    private val url= Constants.URL_API + "News"
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        queue = Volley.newRequestQueue(this)


        val idNew = intent.getLongExtra(Constants.ID_NEW, -1)



        if(idNew > 0){
            //getOne
            val stringRequest = StringRequest(Request.Method.GET, url + "/" + idNew,
                Response.Listener<String> { response ->
                    val jsonObject = JSONObject(response)
                    if(jsonObject["code"] == 1){
                        val array = jsonObject.getJSONArray("valuesNews")
                        val news = EntityNews();
                        news.id = array.getJSONObject(0).getLong("id")
                        news.date = array.getJSONObject(0).getString("systemDate")
                        news.title = array.getJSONObject(0).getString("title")
                        news.content = array.getJSONObject(0).getString("description")
                        news.imageUrl = array.getJSONObject(0).getString("imageURL")
                        supportActionBar?.setTitle(news.title)
                        binding.txtTitle.setText(news.title)
                        binding.txtDate.setText(news.date)
                        binding.txtContent.setText(news.content)
                        Picasso.get().load(news.imageUrl).fit()
                                .placeholder(R.drawable.imgplaceholder)
                                .error(R.drawable.imgplaceholder)
                                .into(binding.imgNews)
                    }else{
                        Toast.makeText(this, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
                    finish()
                })
                queue.add(stringRequest)

        }else{
            Toast.makeText(this@DetailNewsActivity, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}