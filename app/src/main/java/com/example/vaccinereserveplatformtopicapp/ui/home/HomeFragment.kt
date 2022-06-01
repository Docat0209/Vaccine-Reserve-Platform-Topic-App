package com.example.vaccinereserveplatformtopicapp.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vaccinereserveplatformtopicapp.HttpRequest
import com.example.vaccinereserveplatformtopicapp.R
import com.example.vaccinereserveplatformtopicapp.adapter.EpidemicInfoAdapter
import com.example.vaccinereserveplatformtopicapp.databinding.FragmentHomeBinding
import com.example.vaccinereserveplatformtopicapp.model.EpidemicInfo
import java.util.*
import android.content.Intent
import android.net.Uri
import org.json.JSONArray


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val httpRequest = HttpRequest("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=PLSHckwvN6OpLwyhWMzh63BIXrOnRjDin6&key=AIzaSyCn-mBkp7b1nPNhSYIcBZV3cYl64sO59H0")
        httpRequest.httpGet {
            requireActivity().runOnUiThread {
                val random = Random().nextInt(it.getJSONObject("pageInfo").getInt("totalResults"))
                binding.textViewEduInfo.text = it.getJSONArray("items").getJSONObject(random).getJSONObject("snippet").getString("title")
                binding.imageViewEduInfo.load(it.getJSONArray("items").getJSONObject(random).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url"))
                val videoId = it.getJSONArray("items").getJSONObject(random).getJSONObject("snippet").getJSONObject("resourceId").getString("videoId")
                binding.layoutEduInfo.setOnClickListener{

                    val uri= Uri.parse("https://www.youtube.com/watch?v=$videoId")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("InflateParams")
    fun update(context: Context){

        val httpRequest = HttpRequest("http://106.107.216.9/api/news")
        httpRequest.httpGetString {
            val eduInfoList = ArrayList<EpidemicInfo>()
            for(i in 1..20){
                eduInfoList.add(EpidemicInfo(JSONArray(it).getJSONObject(i).getString("Title"),JSONArray(it).getJSONObject(i).getString("Date"),JSONArray(it).getJSONObject(i).getString("Text")))
            }

            requireActivity().runOnUiThread{
                val adapter = EpidemicInfoAdapter(context,eduInfoList)
                val listViewEpidemicInfo = (context as Activity).findViewById<RecyclerView>(R.id.recyclerView_news)
                listViewEpidemicInfo.adapter = adapter
                listViewEpidemicInfo.layoutManager = LinearLayoutManager(context)
            }
        }
    }
}