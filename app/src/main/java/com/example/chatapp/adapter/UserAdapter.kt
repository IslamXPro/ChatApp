package com.example.chatapp.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ItemRvBinding
import com.example.chatapp.models.User
import com.squareup.picasso.Picasso

var isCheked = false

class UserAdapter(private val list: List<User>, val onClick: OnClick)
    : RecyclerView.Adapter<UserAdapter.Vh>() {
    inner class Vh(private var itemRv: ItemRvBinding): RecyclerView.ViewHolder(itemRv.root){
        @SuppressLint("SetTextI18n")
        fun onBind(user: User){
            itemRv.userDisName.text = user.displayName
            itemRv.userDisName.setSingleLine()
            itemRv.userDisName.ellipsize = TextUtils.TruncateAt.MARQUEE
            itemRv.userDisName.marqueeRepeatLimit = -1
            itemRv.userDisName.isSelected = true

            itemRv.userEmail.text = user.email
            itemRv.userEmail.setSingleLine()
            itemRv.userEmail.ellipsize = TextUtils.TruncateAt.MARQUEE
            itemRv.userEmail.marqueeRepeatLimit = -1
            itemRv.userEmail.isSelected = true

            Picasso.get().load(user.img).into(itemRv.userImg)

            itemRv.root.setOnClickListener {
                onClick.onClick(user)
            }
            itemRv.animBookmark.setOnClickListener {
                onClick.animClick(itemRv)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnClick{
        fun onClick(user: User)
        fun animClick(itemRv: ItemRvBinding)
    }
}