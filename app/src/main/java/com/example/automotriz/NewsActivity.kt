package com.example.automotriz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.automotriz.Adapters.NewsAdapter
import com.example.automotriz.Constants.Constants
import com.example.automotriz.Constants.NetworkConnectionState
import com.example.automotriz.Data.ClientDB
import com.example.automotriz.Entity.EntityNews
import com.example.automotriz.databinding.ActivityNewsBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var queue: RequestQueue
    private val url= Constants.URL_API + "News"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNewsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setTitle(R.string.txt_news)
        queue = Volley.newRequestQueue(this)

        val netwokr = NetworkConnectionState(this)
        if(netwokr.checkNetwork()){
            loadNewsList()

        }else{
            actionDialog().show()
        }
    }

    fun loadNewsList() {
        val list = arrayListOf<EntityNews>()
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    val jsonObject = JSONObject(response)
                    if(jsonObject["code"] == 1) {
                        val array = jsonObject.getJSONArray("valuesNews")
                        for(i in 0 until array.length()){
                            val myNew = EntityNews();
                            myNew.id = array.getJSONObject(i).getLong("id")
                            myNew.date = array.getJSONObject(i).getString("systemDate")
                            myNew.title = array.getJSONObject(i).getString("title")
                            myNew.imageUrl = array.getJSONObject(i).getString("imageURL")
                            list.add(myNew)
                        }
                        val adapter = NewsAdapter(list, this@NewsActivity)
                        val linearLayout = LinearLayoutManager(
                                this@NewsActivity, LinearLayoutManager.VERTICAL,
                                false
                        )
                        binding.rwsNews.layoutManager = linearLayout
                        binding.rwsNews.adapter = adapter
                    }


                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
                    Log.d("UdelP", "Error is $error")
                })
        queue.add(stringRequest)

    }



    fun actionDialog(): AlertDialog {
        val alert = AlertDialog.Builder(this@NewsActivity)
        alert.setTitle(R.string.app_name)
        alert.setMessage(R.string.txt_you_need_internet)

        alert.setPositiveButton(R.string.txt_ok){_,_ ->
            finish()
        }
        return  alert.create()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itmNews -> {
                val intent = Intent(this@NewsActivity, NewsActivity::class.java)
                startActivity(intent)
            }
            R.id.itmSearch -> {
                val intent = Intent(this@NewsActivity, FilterActivity::class.java)
                startActivity(intent)
            }
            R.id.itmSaved -> {
                val intent = Intent(this@NewsActivity, SavedCarsActivity::class.java)
                startActivity(intent)
            }
            R.id.itmMyAccount -> {
                val intent = Intent(this@NewsActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}