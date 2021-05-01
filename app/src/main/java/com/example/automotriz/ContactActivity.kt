package com.example.automotriz

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.automotriz.Constants.ApplicationPermissions
import com.example.automotriz.Constants.Constants
import com.example.automotriz.Data.ClientDB
import com.example.automotriz.Entity.EntityCar
import com.example.automotriz.Entity.EntityClient
import com.example.automotriz.Entity.EntityNews
import com.example.automotriz.databinding.ActivityContactBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.json.JSONObject


class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    private val permission = ApplicationPermissions(this@ContactActivity)
    private lateinit var queue: RequestQueue
    private val url= Constants.URL_API + "Client"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContactBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setTitle(R.string.txt_contact)
        val idCar = intent.getLongExtra(Constants.ID_CAR, -1)
        queue = Volley.newRequestQueue(this)

        val db = ClientDB(this)
        val idClient = db.getOne()
        val stringRequest = StringRequest(Request.Method.GET, url + "/" + idClient,
                Response.Listener<String> { response ->
                    val jsonObject = JSONObject(response)
                    if(jsonObject["code"] == 1){
                        val array = jsonObject.getJSONArray("valuesClient")
                        binding.txtFullName.setText(array.getJSONObject(0).getString("name") + " " + array.getJSONObject(0).getString("lastName") + " " + array.getJSONObject(0).getString("surName"))
                        binding.txtEmail.setText(array.getJSONObject(0).getString("email"))
                        binding.txtPhone.setText(array.getJSONObject(0).getString("phone"))
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

        if(idCar > 0){
            //getOne
            val stringRequest = StringRequest(Request.Method.GET, Constants.URL_API + "Car/" + idCar + "/" + idClient,
                    Response.Listener<String> { response ->
                        val jsonObject = JSONObject(response)
                        if(jsonObject["code"].toString().toInt() >= 1){
                            val array = jsonObject.getJSONArray("valuesCars")
                            val car = EntityCar()
                            car.id = idCar
                            car.cost =  array.getJSONObject(0).getDouble("cost")
                            car.brand = "${array.getJSONObject(0).getString("brandName")} ${array.getJSONObject(0).getString("name")}"
                            car.year = array.getJSONObject(0).getInt("year")
                            car.image = array.getJSONObject(0).getString("imageURL")
                            Picasso.get().load(car.image).fit()
                                    .placeholder(R.drawable.imgplaceholder)
                                    .error(R.drawable.imgplaceholder)
                                    .into(binding.imgCar)
                            supportActionBar?.setTitle(car.brand + ' ' + car.year)


                            binding.txtIntrested.setText("${getString(R.string.txt_interested)}  ${car.brand} ${car.year} $${car.cost}")

                        }else{
                            Toast.makeText(this, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
                        }

                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
                    })
            queue.add(stringRequest)



            binding.btnSend.setOnClickListener {
                //Mandar info
                val jsonBody = JSONObject()
                jsonBody.put("idClient", idClient)
                jsonBody.put("idCar", idCar)
                val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, Constants.URL_API + "Contact",jsonBody,
                        Response.Listener { response ->
                            if(response["code"].toString().toInt() >= 1){
                                Toast.makeText(this, R.string.txt_rquest_send, Toast.LENGTH_SHORT).show()
                            }else if(response["code"].toString().toInt() == -1){
                                Toast.makeText(this, R.string.txt_already_request_data, Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(this, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                            }

                        },
                        Response.ErrorListener { error ->
                            if(error.networkResponse.statusCode == 406){
                                Toast.makeText(this, R.string.txt_already_request_data, Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(this, R.string.txt_could_not_send_data, Toast.LENGTH_SHORT).show()
                            }



                        })
                queue.add(jsonObjectRequest)
            }

            binding.btnCall.setOnClickListener {
                if(!permission.hasPermissions(Constants.CALL_PHONE)){
                    permission.acceptPermission(Constants.CALL_PHONE, 1)
                }else{
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "5521976745"))
                    startActivity(intent)
                }
            }

        }else{
            Toast.makeText(this@ContactActivity, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 -> {
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Snackbar.make(findViewById(android.R.id.content), R.string.txt_phone_call_permission, Snackbar.LENGTH_LONG).show()
                }else{
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "5521976745"))
                    startActivity(intent)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itmNews -> {
                val intent = Intent(this@ContactActivity, NewsActivity::class.java)
                startActivity(intent)
            }
            R.id.itmSearch -> {
                val intent = Intent(this@ContactActivity, FilterActivity::class.java)
                startActivity(intent)
            }
            R.id.itmSaved -> {
                val intent = Intent(this@ContactActivity, SavedCarsActivity::class.java)
                startActivity(intent)
            }
            R.id.itmMyAccount -> {
                val intent = Intent(this@ContactActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}