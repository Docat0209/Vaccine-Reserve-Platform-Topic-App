package com.example.vaccinereserveplatformtopicapp.ui.reserve

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.vaccinereserveplatformtopicapp.HttpRequest
import com.example.vaccinereserveplatformtopicapp.MainActivity
import com.example.vaccinereserveplatformtopicapp.databinding.FragmentCheckBinding
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class CheckFragment : Fragment() {

    private var _binding: FragmentCheckBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCheckBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            buttonEasyCheckSend.setOnClickListener{
                val jsonObject = JSONObject()
                jsonObject.put("ROCId",editTextEasyCheckRocId.text)
                jsonObject.put("Year",editTextEasyCheckYear.text)

                HttpRequest("http://106.107.216.9/api/easycheck").httpPost(jsonObject) {

                    requireActivity().runOnUiThread{

                        if (it.getString("Result") == "Access") {

                            Toast.makeText(context, "驗證成功", Toast.LENGTH_SHORT).show()
                            HttpRequest("http://106.107.216.9/api/reserve?rocId=${editTextEasyCheckRocId.text}").httpGetString {
                                val check = it.substring(0,1)
                                if (check == "["){
                                    val jsonArray = JSONArray(it)

                                    val intent = Intent(context , ResultActivity::class.java)
                                    intent.putExtra("Name",jsonArray.getJSONObject(0).getString("Name"))
                                    intent.putExtra("Phone",jsonArray.getJSONObject(0).getString("Phone"))
                                    intent.putExtra("ROCId",jsonArray.getJSONObject(0).getString("ROCId"))
                                    intent.putExtra("VaccineType",jsonArray.getJSONObject(0).getString("VaccineType"))
                                    intent.putExtra("City",jsonArray.getJSONObject(0).getString("City"))
                                    intent.putExtra("Dist",jsonArray.getJSONObject(0).getString("Dist"))
                                    intent.putExtra("HospName",jsonArray.getJSONObject(0).getString("HospName"))

                                    startActivity(intent)
                                }
                                else{
                                    val intent = Intent(context , ReserveActivity::class.java)
                                    intent.putExtra("ROCId",editTextEasyCheckRocId.text.toString())

                                    startActivity(intent)
                                }
                            }


                        }else{

                            Toast.makeText(context, "驗證失敗", Toast.LENGTH_SHORT).show()
                            editTextEasyCheckRocId.text.clear()
                            editTextEasyCheckYear.text.clear()

                        }
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}