package com.example.chatapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils.*
import android.view.View
import android.view.View.GONE
import androidx.annotation.RequiresApi
import androidx.core.view.size
import com.example.chatapp.adapter.MessageAdapter
import com.example.chatapp.databinding.ActivityMessageBinding
import com.example.chatapp.models.MessageUser
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class MessageActivity : AppCompatActivity() {
    lateinit var binding : ActivityMessageBinding
    lateinit var firebaseAuth : FirebaseAuth
    lateinit var firebaseDatabase : FirebaseDatabase
    lateinit var referenceMessage : DatabaseReference
    lateinit var messageAdapter : MessageAdapter
    lateinit var user : User
    lateinit var handler: Handler
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        user = intent.getSerializableExtra("keyUser") as User

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        referenceMessage = firebaseDatabase.getReference("messages")
        handler = Handler()

        binding.itUserName.text = user.displayName
        binding.itUserName.setSingleLine()
        binding.itUserName.ellipsize = TruncateAt.MARQUEE
        binding.itUserName.marqueeRepeatLimit = -1
        binding.itUserName.isSelected = true
        Picasso.get().load(user.img).into(binding.itUserImg)
        binding.itUserImg.setOnClickListener {
            if (user.idToken == firebaseAuth.uid){
            Picasso.get().load(user.img).into(binding.itUserImg)
            binding.bigUserImg.visibility = View.VISIBLE
            binding.recycle.alpha = 0.8f
            binding.line1.alpha = 0.8f
            }
        }
        binding.recycle.setOnClickListener {
            binding.bigUserImg.visibility = GONE
        }
        handler.postDelayed({binding.itUserData.text},10000)
        binding.itUserData.text = user.data

        val nowTime = "${java.util.Date().hours}:${java.util.Date().minutes}"

        binding.sendBtn.setOnClickListener {
            val key = referenceMessage.push().key
            val messageuser = MessageUser(key,binding.edtSms.text.toString().trim(),firebaseAuth.uid,user.idToken)
            referenceMessage.child(key!!).setValue(messageuser)
            binding.edtSms.text.clear()
            binding.sentAnim.visibility = View.VISIBLE
            binding.sentAnim.playAnimation()
            binding.sentAnim.speed = 1.2f
            handler.postDelayed({binding.sentAnim.visibility = GONE},1000)
        }

        referenceMessage.addValueEventListener(object : ValueEventListener{
            @SuppressLint("SetTextI18n", "ResourceType")
            override fun onDataChange(snapshot: DataSnapshot) {
                val listM = ArrayList<MessageUser>()
                val children = snapshot.children
                for (child in children){
                    val value = child.getValue(MessageUser::class.java)
                    if (value!=null){
                        /*if (value.toUID == user.idToken && value.date == nowTime){
                            binding.itUserData.text = "Online"
                            binding.online.setImageResource(R.drawable.online)
                            Toast.makeText(this@MessageActivity, "online", Toast.LENGTH_SHORT).show()
                        }else if (value.toUID == user.idToken && value.date < nowTime){
                            binding.itUserData.text = "Last seen ${value.date}"
                            binding.online.setImageResource(R.drawable.offline)
                            Toast.makeText(this@MessageActivity, "offline", Toast.LENGTH_SHORT).show()
                        }*/

                        if (value.date == nowTime){
                            if ((value.fromUid == firebaseAuth.uid && value.toUid == user.idToken) || (value.fromUid == user.idToken && value.toUid == firebaseAuth.uid)){
                                binding.itUserData.text = "Online"
                                binding.online.setImageResource(R.drawable.online)
                            }
                        }else if (value.date < nowTime){
                            binding.itUserData.text = "Last seen ${value.date}"
                            binding.online.setImageResource(R.drawable.offline)
                        }
                        if ((value.fromUid == firebaseAuth.uid && value.toUid == user.idToken) ||(value.fromUid == user.idToken && value.toUid == firebaseAuth.uid))
                        listM.add(value)
                    }
                }
                messageAdapter = MessageAdapter(listM,firebaseAuth.uid!!)
                binding.recycle.adapter = messageAdapter
                binding.recycle.scrollToPosition(listM.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}