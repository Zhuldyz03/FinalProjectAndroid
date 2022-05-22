package com.example.final_project.Activities

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.final_project.R
import com.example.final_project.Util.ConnectionManager
import com.example.final_project.Util.Constraints
import com.example.final_project.Util.Preferences
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

class LoginActivity : AppCompatActivity() {
   private lateinit var  edtLogMobileNumber: EditText
   private lateinit var  edtLogPassword: EditText
   private lateinit var  btnLogin: Button


   private lateinit var txtSignUp: TextView
    private lateinit var txtForgotPassword: TextView

   private lateinit var preferences: Preferences
   private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in)

        preferences = Preferences(this)
        sharedPreferences =
            this.getSharedPreferences(preferences.PREF_NAME, preferences.PRIVATE_MODE)

        txtSignUp = findViewById(R.id.txtSignUp)


        txtSignUp.setOnClickListener {
            val intent = Intent(baseContext, RegistrationActivity::class.java)
            startActivity(intent)
        }
        txtForgotPassword = findViewById(R.id.txtForgotPassword)



        txtForgotPassword.setOnClickListener {
            val intent = Intent(baseContext, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        edtLogMobileNumber = findViewById(R.id.edtLogMobileNumber)
        edtLogPassword = findViewById(R.id.edtLogPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {

            logInUser()
        }
    }
    private fun logInUser(){

        val txtLogMobileNumber :String= edtLogMobileNumber.getText().toString()
        val txtLogPassword: String= edtLogPassword.getText().toString()

        if (Constraints.validateMobile(edtLogMobileNumber.text.toString()) && Constraints.validatePasswordLength(edtLogPassword.text.toString())) {


            if (ConnectionManager().isNetworkAvailable(this@LoginActivity)) {

            val queue = Volley.newRequestQueue(this@LoginActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result "
            val jsonParams = JSONObject()



            jsonParams.put("mobile_number", txtLogMobileNumber);
            jsonParams.put("password", txtLogPassword)

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val LoginJsonObject = it.getJSONObject("data")
                        val success = LoginJsonObject.getBoolean("success")
                        if (success) {

                            //  if success then we save the credentials in sharedpreferences

                            val response = LoginJsonObject.getJSONObject("data")
                            sharedPreferences.edit()
                                .putString("user_id", response.getString("user_id")).apply()
                            sharedPreferences.edit()
                                .putString("user_name", response.getString("name")).apply()
                            sharedPreferences.edit()
                                .putString(
                                    "user_mobile_number",
                                    response.getString("mobile_number")
                                )
                                .apply()
                            sharedPreferences.edit()
                                .putString("user_address", response.getString("address"))
                                .apply()
                            sharedPreferences.edit()
                                .putString("user_email", response.getString("email")).apply()
                            preferences.setLogin(true)


                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    MainActivity::class.java
                                )
                            )
                            finish()
                        } else {
                            val message: String = LoginJsonObject.getString("errorMessage")
                            Toast.makeText(
                                this@LoginActivity,
                                "$message ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
                    println("error is $it")

                    Toast.makeText(
                        this@LoginActivity,
                        "Some error has occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {

                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "b312a26fe9bf61"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }else{


            val dialog = AlertDialog.Builder(this)
            dialog.setTitle(" Error ")
            dialog.setMessage(" Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text,listener->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit"){text, listener->

                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
            }
    }else{

            btnLogin.visibility = View.VISIBLE
            txtForgotPassword.visibility = View.VISIBLE
            txtSignUp.visibility = View.VISIBLE
            Toast.makeText(this@LoginActivity, "Invalid Number or Password", Toast.LENGTH_SHORT)
                .show()

        }
}}
