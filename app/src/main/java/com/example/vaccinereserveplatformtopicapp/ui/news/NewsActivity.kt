package com.example.vaccinereserveplatformtopicapp.ui.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vaccinereserveplatformtopicapp.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonClose.setOnClickListener{
            finish()
        }

        binding.newsTitle.text = intent.extras?.getString("title");
        binding.newsText.text = intent.extras?.getString("text");

    }


}