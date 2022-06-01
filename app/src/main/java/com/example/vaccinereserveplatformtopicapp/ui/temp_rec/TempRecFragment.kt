package com.example.vaccinereserveplatformtopicapp.ui.temp_rec

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.SeekBar
import android.widget.TimePicker
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinereserveplatformtopicapp.LocalSql
import com.example.vaccinereserveplatformtopicapp.R
import com.example.vaccinereserveplatformtopicapp.adapter.TempRecAdapter
import com.example.vaccinereserveplatformtopicapp.databinding.DialogTempRecAddBinding
import com.example.vaccinereserveplatformtopicapp.databinding.FragmentTempRecBinding
import com.example.vaccinereserveplatformtopicapp.model.TempRec
import java.text.SimpleDateFormat
import java.util.*

class TempRecFragment : Fragment() {

    private var _binding: FragmentTempRecBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTempRecBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.apply {

            buttonAddTempRec.setOnClickListener{
                dialogAdd(requireContext())
            }

        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update(requireContext())
        requireActivity().findViewById<RecyclerView>(R.id.listView_temp_rec) .layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("InflateParams")
    fun update(context: Context){
        val tempRecList = ArrayList<TempRec>()
        val c = LocalSql(context).read("select * from TempRec")
        c.moveToFirst()
        while (c.moveToNext()){
            tempRecList.add(TempRec(c.getInt(0),c.getString(1),c.getFloat(2)))
        }
        val adapter = TempRecAdapter(context,tempRecList)
        val listViewTempRec = (context as Activity).findViewById<RecyclerView>(R.id.listView_temp_rec)
        listViewTempRec.adapter = adapter

    }

    @SuppressLint("InflateParams", "SetTextI18n", "SimpleDateFormat")
    fun dialogAdd(context: Context){
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_temp_rec_add , null , false)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogBinding = DialogTempRecAddBinding.bind(dialogView)

        dialogBinding.apply {
            tempHint.x = seekBar.thumb.bounds.exactCenterX()
            tempHint.text = (seekBar.progress/10.0f).toString()+"°C"
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    tempHint.x = seekBar.thumb.bounds.exactCenterX()
                    tempHint.text = (seekBar.progress/10.0f).toString()+"°C"
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            })

            val c = Calendar.getInstance(Locale.TAIWAN)

            date.text = SimpleDateFormat("MMM dd,20 yy", Locale.US).format(c.time)
            if (c.get(Calendar.HOUR_OF_DAY)>12){
                textHour.text = (c.get(Calendar.HOUR_OF_DAY)-12).toString()
                pm.isChecked = true
            } else {
                textHour.text = c.get(Calendar.HOUR_OF_DAY).toString()
                am.isChecked = true
            }
            textMinute.text = c.get(Calendar.MINUTE).toString()

            am.setOnClickListener{
                c.set(Calendar.AM_PM , Calendar.AM)
            }

            pm.setOnClickListener{
                c.set(Calendar.AM_PM , Calendar.PM)
            }

            datePicker.setOnClickListener{
                DatePickerDialog(context,{ _: DatePicker, i: Int, i1: Int, i2: Int ->
                    c.set(i ,i1 ,i2)
                    date.text = SimpleDateFormat("MMM dd,20 yy", Locale.US).format(c.time)
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show()
            }

            timePicker.setOnClickListener{
                TimePickerDialog(context,2,{ _: TimePicker, i: Int, i1: Int ->

                    c.set(Calendar.HOUR , i)
                    c.set(Calendar.MINUTE , i1)
                    if (i>12){
                        textHour.text = (i-12).toString()
                        pm.isChecked = true
                    } else {
                        textHour.text = i.toString()
                        am.isChecked = true
                    }
                    textMinute.text = i1.toString()
                },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),false).show()
            }

            buttonAdd.setOnClickListener{
                val temp = seekBar.progress/10.0f
                val time = SimpleDateFormat("yyyy年MM月dd日 EE aa hh:mm", Locale.TAIWAN).format(c.time)
                LocalSql(context).run("insert into TempRec values(null,'$time','$temp')")
                update(requireContext())
                dialog.dismiss()
            }

            buttonCancel.setOnClickListener{
                dialog.dismiss()
            }
        }

    }
}