package com.billion_dollor_company.accessorystore.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.billion_dollor_company.accessorystore.R
import com.billion_dollor_company.accessorystore.databinding.ActivityMainBinding
import com.billion_dollor_company.accessorystore.databinding.ActivityWatchInfoBinding
import com.billion_dollor_company.accessorystore.misc.FinalVariables.Companion.DEBUG
import com.billion_dollor_company.accessorystore.misc.FinalVariables.Companion.WATCH_REFERENCE
import com.billion_dollor_company.accessorystore.models.WatchInfo
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class WatchInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatchInfoBinding

    private lateinit var curWatch : WatchInfo

    private fun fetchData(path: String){
        val ref = FirebaseFirestore.getInstance().document(path)
        Log.d(DEBUG, "The path is $path")
        ref.get().addOnCompleteListener {
            if(it.isSuccessful){
                val document = it.result
                Log.d(DEBUG, "The document is "+document.toString())
                val name = document.getString("name") ?: ""
                val price = document.getLong("price")?.toInt() ?: 0
                val discount = document.getLong("discount")?.toInt() ?: 0
                val imageURL = document.getString("imageURL") ?: ""
                val company = document.getString("company") ?: ""
                val description = document.getString("description") ?: ""
                val type = document.getString("type") ?: ""
                curWatch = WatchInfo(
                    name = name, price = price, discount = discount,
                    imageURL = imageURL,
                    company = company, description = description, type = type
                )
                Log.d(DEBUG, "The current watch is "+curWatch)
                setTheContent()
            }else{
                Log.d(DEBUG, "Load failed")
            }

        }
    }

    //this function gets the watch intent from the other activities
    private fun getWatchIntent(){
        val path:String? = intent.getStringExtra(WATCH_REFERENCE)
        if(path == null){
            Toast.makeText(this, "The received intent was empty!", Toast.LENGTH_SHORT).show()
        }else{
            fetchData(path)
        }
    }


    private fun setTheContent() {
        Glide.with(this).load(Uri.parse(curWatch.imageURL)).into(binding.ivImage)
        binding.tvName.text = curWatch.name
        binding.tvPrice.text = (curWatch.price - ((curWatch.discount.toFloat()/100.0)*curWatch.price).toInt()).toString()
        binding.tvDescription.text = curWatch.description
        binding.tvCompanyName.text = curWatch.company.capitalize()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWatchIntent()
    }
}