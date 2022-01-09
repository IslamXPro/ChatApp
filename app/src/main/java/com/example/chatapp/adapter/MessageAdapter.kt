package com.example.chatapp.adapter

import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ItemFromBinding
import com.example.chatapp.databinding.ItemToBinding
import com.example.chatapp.models.MessageUser
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*

class MessageAdapter(val list: List<MessageUser>, var uid:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var auth : FirebaseAuth

    inner class FromVh(var itemRv:ItemFromBinding):RecyclerView.ViewHolder(itemRv.root){

        fun onBind(message: MessageUser){
            val nowTime = "${Date().hours}:${Date().minutes}"
            var isChecked = false
            auth = FirebaseAuth.getInstance()
            itemRv.txtSms.text = message.message
            itemRv.txtData.text = message.date
            Picasso.get().load(auth.currentUser?.photoUrl).into(itemRv.userImg)
            //sms vaqtning hozir vaqtga teng bo'lsa ko'rinadi, kichik bo'lsa GONE bo'ladi
            if (itemRv.txtData.text == nowTime){
                itemRv.txtData.visibility = VISIBLE
                itemRv.userImg.visibility = VISIBLE
            } else{
                itemRv.txtData.visibility = GONE
                itemRv.userImg.visibility = GONE
            }

            //smsni bosganda sms datasi VISIBLE bo'ladi yana bosganda GONE bo'ladi
            itemRv.txtSms.setOnClickListener {
                if (isChecked){
                    itemRv.txtData.visibility = GONE
                    isChecked = false
                } else{
                    itemRv.txtData.visibility = VISIBLE
                    isChecked = true
                }
            }
            itemRv.txtSms.setOnLongClickListener {
                if (isChecked){
                    itemRv.userImg.visibility = GONE
                    isChecked = false
                }else{
                    itemRv.userImg.visibility = VISIBLE
                    isChecked = true
                }
                true
            }
        }
    }

    inner class ToVh(var itemRv: ItemToBinding):RecyclerView.ViewHolder(itemRv.root){
        fun onBind(message: MessageUser){
            val nowTime = "${Date().hours}:${Date().minutes}"
            var isChecked = false
            val user = User()
            itemRv.txtSms.text = message.message
            itemRv.txtData.text = message.date
            Picasso.get().load(user.img).into(itemRv.userImg)

            //sms vaqtning hozir vaqtga teng bo'lsa ko'rinadi, kichik bo'lsa GONE bo'ladi
            if (itemRv.txtData.text == nowTime){
                itemRv.txtData.visibility = VISIBLE
                itemRv.userImg.visibility = VISIBLE
            } else{
                itemRv.txtData.visibility = GONE
                itemRv.userImg.visibility = GONE
            }

            //smsni bosganda sms datasi VISIBLE bo'ladi yana bosganda GONE bo'ladi
            itemRv.txtSms.setOnClickListener {
                if (isChecked){
                    itemRv.txtData.visibility = GONE
                    isChecked = false
                } else{
                    itemRv.txtData.visibility = VISIBLE
                    isChecked = true
                }
            }

            itemRv.txtSms.setOnLongClickListener {
                if (isChecked){
                    itemRv.userImg.visibility = GONE
                    isChecked = false
                }else{
                    itemRv.userImg.visibility = VISIBLE
                    isChecked = true
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            return FromVh(ItemFromBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else{
            return ToVh(ItemToBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == 1){
            val fromVh = holder as FromVh
            fromVh.onBind(list[position])
        }else{
            val toVh = holder as ToVh
            toVh.onBind(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].fromUid == uid){
            return 1
        }else{
            return 2
        }
    }

    override fun getItemCount(): Int = list.size
}