package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import android.view.View.VISIBLE
import com.example.chatapp.adapter.UserAdapter
import com.example.chatapp.adapter.isCheked
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.databinding.ItemRvBinding
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var list:ArrayList<User>
    lateinit var auth: FirebaseAuth
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")
        handler = Handler()

        binding.search.setOnClickListener {
            binding.search.visibility = GONE
            binding.more.visibility = GONE
            binding.kilogram.visibility = GONE
            binding.mainCard1.visibility = VISIBLE
        }
        binding.search1.setOnClickListener {
            binding.mainCard1.visibility = GONE
            binding.search.visibility = VISIBLE
            binding.more.visibility = VISIBLE
            binding.kilogram.visibility = VISIBLE
        }

            val user = User(auth.uid,auth.currentUser?.displayName,auth.currentUser?.email,auth.currentUser?.photoUrl)
            reference.child(auth.uid!!).setValue(user)

            reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                list = ArrayList()
                for (child in children) {
                    val value = child.getValue(User::class.java)
                    if (value!=null){
                        if (value.idToken != auth.uid){
                        list.add(value)
                    }
                    }
                }
                val userAdapter = UserAdapter(list, object : UserAdapter.OnClick{
                    override fun onClick(user: User) {
                        val intent = Intent(this@MainActivity,MessageActivity::class.java)
                        intent.putExtra("keyUser",user)
                        startActivity(intent)
                    }

                    override fun animClick(itemRv: ItemRvBinding) {
                            if (isCheked){
                                itemRv.animBookmark.speed = -1f
                                itemRv.animBookmark.playAnimation()
                                isCheked = false
                                handler.postDelayed({itemRv.animBookmark.cancelAnimation()},500)

                            }else{
                                itemRv.animBookmark.speed = 1f
                                itemRv.animBookmark.playAnimation()
                                isCheked = true
                                handler.postDelayed({itemRv.animBookmark.cancelAnimation()},550)
                            }
                        }
                })
                binding.recycle.adapter = userAdapter
            }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}