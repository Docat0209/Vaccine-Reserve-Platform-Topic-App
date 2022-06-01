package com.example.vaccinereserveplatformtopicapp.ui.reserve

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vaccinereserveplatformtopicapp.HttpRequest
import com.example.vaccinereserveplatformtopicapp.R
import com.example.vaccinereserveplatformtopicapp.databinding.ActivityResultBinding
import org.json.JSONObject

class ResultActivity : AppCompatActivity() {
    private var _binding: ActivityResultBinding? = null

    private val binding get() = _binding!!
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonClose.setOnClickListener{
                finish()
            }

            textViewResultArea.text = intent.extras?.getString("City") +  intent.extras?.getString("Dist")
            textViewResultHospName.text = intent.extras?.getString("HospName")
            textViewResultName.text = intent.extras?.getString("Name")
            textViewResultPhone.text = intent.extras?.getString("Phone")
            textViewResultRocId.text = intent.extras?.getString("ROCId")
            textViewResultVaccine.text = intent.extras?.getString("VaccineType")

            val jsonObject = JSONObject()
            jsonObject.put("ROCId",textViewResultRocId.text)
            buttonResultCancel.setOnClickListener{
                HttpRequest("http://106.107.216.9/api/reserve").httpDelete(jsonObject) {
                    if (it.getString("Result") == "Access") {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "成功取消預約", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }else{
                        runOnUiThread {
                            Toast.makeText(applicationContext, "取消預約失敗", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                
            }
        }
    }
}