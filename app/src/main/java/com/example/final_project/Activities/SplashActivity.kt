package com.example.final_project.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.final_project.R
import java.lang.Exception

class SplashActivity : AppCompatActivity() {
 private  lateinit var preferences: com.example.final_project.Util.Preferences
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        preferences = com.example.final_project.Util.Preferences(this)
        imageView= findViewById(R.id.imageView)


        val background= object: Thread(){

            override fun run() {
                try {
                    sleep(5000)


                    if (preferences.isLoggedIn()) {
                        val intent = Intent(baseContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(baseContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    background.start()
    }
    override fun onPause() {
        super.onPause()
        finish()

    }

}
