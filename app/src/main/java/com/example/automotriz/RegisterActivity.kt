package com.example.automotriz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.automotriz.Constants.Constants
import com.example.automotriz.Data.ClientDB
import com.example.automotriz.Entity.EntityClient
import com.example.automotriz.Entity.EntityNews
import com.example.automotriz.databinding.ActivityFilterBinding
import com.example.automotriz.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val url= Constants.URL_API + "Client"
    private lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        queue = Volley.newRequestQueue(this)

        val db = ClientDB(this@RegisterActivity)
        var idClient = db.getOne()
        if(idClient > 0){
            val stringRequest = StringRequest(Request.Method.GET, url + "/" + idClient,
                    Response.Listener<String> { response ->
                        val jsonObject = JSONObject(response)
                        if(jsonObject["code"] == 1){
                            val array = jsonObject.getJSONArray("valuesClient")
                            binding.edtName.setText(array.getJSONObject(0).getString("name"))
                            binding.edtLastName.setText(array.getJSONObject(0).getString("lastName"))
                            binding.edtSurName.setText(array.getJSONObject(0).getString("surName"))
                            binding.edtEmail.setText(array.getJSONObject(0).getString("email"))
                            binding.edtPhone.setText(array.getJSONObject(0).getString("phone"))
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

        }

        binding.btnRegister.setOnClickListener {
            val user = EntityClient()
            if (binding.edtEmail.text.trim().isNotEmpty()
                    && binding.edtPhone.text.trim().isNotEmpty()
                    && binding.edtName.text.trim().isNotEmpty()
                    && binding.edtLastName.text.trim().isNotEmpty()
                    && binding.edtSurName.text.trim().isNotEmpty()) {
                user.id = idClient
                user.name = binding.edtName.text.toString()
                user.lastName = binding.edtLastName.text.toString()
                user.surName = binding.edtSurName.text.toString()
                user.email = binding.edtEmail.text.toString()
                user.phone = binding.edtPhone.text.toString()
                idClient = db.getOne()
                if(idClient> 0){
                    //Actualizar
                    val jsonBody = JSONObject()
                    jsonBody.put("id", user.id)
                    jsonBody.put("name", user.name)
                    jsonBody.put("lastName", user.lastName)
                    jsonBody.put("surName", user.surName)
                    jsonBody.put("email", user.email)
                    jsonBody.put("phone", user.phone)
                    val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url,jsonBody,
                            Response.Listener {response ->
                                if(response["code"].toString().toInt() > 0){
                                    Toast.makeText(this, R.string.txt_contact_data_updated, Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                                }

                            },
                            Response.ErrorListener {error ->
                                Toast.makeText(this, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                            })
                    queue.add(jsonObjectRequest)

                }else{
                    //Insertar
                    val jsonBody = JSONObject()
                    jsonBody.put("name", user.name)
                    jsonBody.put("lastName", user.lastName)
                    jsonBody.put("surName", user.surName)
                    jsonBody.put("email", user.email)
                    jsonBody.put("phone", user.phone)
                    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url,jsonBody,
                            Response.Listener {response ->
                                if(response["code"].toString().toInt() > 0){
                                    db.add(response["code"].toString().toLong())
                                    Toast.makeText(this, R.string.txt_contact_data_registered, Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                                }

                            },
                            Response.ErrorListener {error ->
                                Toast.makeText(this, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                            })
                    queue.add(jsonObjectRequest)
                }
            }else{
                Snackbar.make(it, R.string.txt_required_files_register,
                        Snackbar.LENGTH_SHORT).show()
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
                val intent = Intent(this@RegisterActivity, NewsActivity::class.java)
                startActivity(intent)
            }
            R.id.itmSearch -> {
                val intent = Intent(this@RegisterActivity, FilterActivity::class.java)
                startActivity(intent)
            }
            R.id.itmSaved -> {
                val intent = Intent(this@RegisterActivity, SavedCarsActivity::class.java)
                startActivity(intent)
            }
            R.id.itmMyAccount -> {
                val intent = Intent(this@RegisterActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}