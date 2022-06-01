package com.example.vaccinereserveplatformtopicapp.ui.reserve

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.vaccinereserveplatformtopicapp.HttpRequest
import com.example.vaccinereserveplatformtopicapp.databinding.ActivityReserveBinding
import org.json.JSONArray
import org.json.JSONObject

class ReserveActivity : AppCompatActivity() {
    private var _binding: ActivityReserveBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReserveBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            buttonClose.setOnClickListener{
                finish()
            }

        }

        binding.editTextReserveRocId.text = intent.extras!!.getString("ROCId")

        reload(binding,0)

        binding.apply {
            buttonReserve.setOnClickListener{
                var flag = true
                if (TextUtils.isEmpty(editTextReserveName.text)){
                    flag = false
                }
                if (TextUtils.isEmpty(editTextReservePhone.text)){
                    flag = false
                }
                if (TextUtils.isEmpty(editTextReserveRocId.text)){
                    flag = false
                }
                if (TextUtils.isEmpty(spinnerReserveCity.selectedItem.toString())){
                    flag = false
                }
                if (TextUtils.isEmpty(spinnerReserveDist.selectedItem.toString())){
                    flag = false
                }
                if (TextUtils.isEmpty(spinnerReserveVaccine.selectedItem.toString())){
                    flag = false
                }
                if (TextUtils.isEmpty(spinnerReserveHospName.selectedItem.toString())){
                    flag = false
                }
                if (!checkboxReserve.isChecked){
                    Toast.makeText(applicationContext, "請勾選同意隱私權公告", Toast.LENGTH_SHORT).show()
                }else if(flag){
                    val jsonObject = JSONObject()

                    jsonObject.put("ROCId",editTextReserveRocId.text)
                    jsonObject.put("Name",editTextReserveName.text)
                    jsonObject.put("Phone",editTextReservePhone.text)
                    jsonObject.put("VaccineType",spinnerReserveVaccine.selectedItem.toString())
                    jsonObject.put("City",spinnerReserveCity.selectedItem.toString())
                    jsonObject.put("Dist",spinnerReserveDist.selectedItem.toString())
                    jsonObject.put("HospName",spinnerReserveHospName.selectedItem.toString())
                    HttpRequest("http://106.107.216.9/api/reserve").httpPut(jsonObject){
                        if (it.getString("Result") == "Access") {
                            runOnUiThread {
                                Toast.makeText(applicationContext, "預約成功", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }else{
                            runOnUiThread {
                                Toast.makeText(applicationContext, "預約失敗", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }else{
                    Toast.makeText(applicationContext, "請確實填寫完資料", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun reload(binding:ActivityReserveBinding,int: Int){

        // vaccine

        if (int <= 0){
            val arrayList = listOf("AstraZeneca","BioNTech","高端","Moderna")
            binding.spinnerReserveVaccine.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayList)
            binding.spinnerReserveVaccine.onItemSelectedListener =
                object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long
                    ) {
                        reload(binding,2)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }
        }

        // city

        if (int == 0){
            val arrayList = ArrayList<String>()
            HttpRequest("http://106.107.216.9/api/vachosp").httpGetString {
                val jsonArray = JSONArray(it)
                for (i in 0 until jsonArray.length()) {
                    arrayList.add(jsonArray.getString(i))
                }
                runOnUiThread{
                    binding.spinnerReserveCity.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayList)
                    binding.spinnerReserveCity.onItemSelectedListener =
                        object : OnItemSelectedListener {
                            override fun onItemSelected(
                                parentView: AdapterView<*>?,
                                selectedItemView: View?,
                                position: Int,
                                id: Long
                            ) {
                                reload(binding,1)
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }
                        }
                }
            }
        }

        // dist

        if (int <= 1){
            val arrayList = ArrayList<String>()
            HttpRequest("http://106.107.216.9/api/vachosp?city=${binding.spinnerReserveCity.selectedItem}").httpGetString {
                val jsonArray = JSONArray(it)
                for (i in 0 until jsonArray.length()) {
                    arrayList.add(jsonArray.getString(i))
                }
                runOnUiThread{
                    binding.spinnerReserveDist.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayList)
                    binding.spinnerReserveDist.onItemSelectedListener =
                        object : OnItemSelectedListener {
                            override fun onItemSelected(
                                parentView: AdapterView<*>?,
                                selectedItemView: View?,
                                position: Int,
                                id: Long
                            ) {
                                reload(binding,2)
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }
                        }
                }
            }
        }

        // hosp

        if (int <= 2){
            val arrayList = ArrayList<String>()
            HttpRequest("http://106.107.216.9/api/vachosp?city=${binding.spinnerReserveCity.selectedItem}&dist=${binding.spinnerReserveDist.selectedItem}&vaccine=${binding.spinnerReserveVaccine.selectedItem}").httpGetString {
                val jsonArray = JSONArray(it)
                for (i in 0 until jsonArray.length()) {
                    arrayList.add(jsonArray.getString(i))
                }
                runOnUiThread{
                    binding.spinnerReserveHospName.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayList)
                }
            }
        }

    }
}