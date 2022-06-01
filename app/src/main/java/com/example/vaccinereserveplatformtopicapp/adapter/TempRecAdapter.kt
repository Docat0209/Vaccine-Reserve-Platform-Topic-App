package com.example.vaccinereserveplatformtopicapp.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.SeekBar
import android.widget.TimePicker
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinereserveplatformtopicapp.LocalSql
import com.example.vaccinereserveplatformtopicapp.R
import com.example.vaccinereserveplatformtopicapp.databinding.DialogTempRecDeleteBinding
import com.example.vaccinereserveplatformtopicapp.databinding.DialogTempRecUpdateBinding
import com.example.vaccinereserveplatformtopicapp.databinding.ListItemTempRecBinding
import com.example.vaccinereserveplatformtopicapp.model.TempRec
import com.example.vaccinereserveplatformtopicapp.ui.temp_rec.TempRecFragment
import java.text.SimpleDateFormat
import java.util.*

class TempRecAdapter(val context: Context , val tempRecList :MutableList<TempRec>):RecyclerView.Adapter<TempRecAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ListItemTempRecBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(tempRec: TempRec){
            binding.apply {
                itemTemp.text = tempRec.temp.toString() + "°C"
                if (tempRec.temp > 37.5){
                    itemTemp.setTextColor(Color.parseColor("#ea5a5a"))
                }
                itemTime.text = tempRec.time
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_temp_rec , parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tempRecList[position])
        holder.binding.item.setOnClickListener{
            dialogUpdate(context,tempRecList[position])
        }
        holder.binding.item.setOnLongClickListener{
            dialogDelete(context,tempRecList[position])
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return tempRecList.count()
    }

    @SuppressLint("SetTextI18n")
    fun dialogDelete(context: Context, tempRec: TempRec){
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_temp_rec_delete, null , false)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogBinding = DialogTempRecDeleteBinding.bind(dialogView)

        dialogBinding.apply {

            textView3.text = "是否刪除\""+tempRec.time+" "+tempRec.temp.toString()+"\" 該筆紀錄"

            buttonAdd.setOnClickListener{
                LocalSql(context).run("delete from TempRec where id = '${tempRec.id}'")
                TempRecFragment().update(context)
                dialog.dismiss()
            }

            buttonCancel.setOnClickListener{
                dialog.dismiss()
            }
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n", "SimpleDateFormat")
    fun dialogUpdate(context: Context , tempRec: TempRec){
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_temp_rec_update, null , false)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogBinding = DialogTempRecUpdateBinding.bind(dialogView)

        dialogBinding.apply {

            seekBar.progress = (tempRec.temp*10).toInt()

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

            val set = SimpleDateFormat("yyyy年MM月dd日 EE aa hh:mm", Locale.TAIWAN).parse(tempRec.time)
            if (set != null) {
                c.time = set
            }

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
                LocalSql(context).run("update TempRec set timeData = '$time' , tempData = '$temp' where id = '${tempRec.id}'")
                TempRecFragment().update(context)
                dialog.dismiss()
            }

            buttonCancel.setOnClickListener{
                dialog.dismiss()
            }
        }

    }
}