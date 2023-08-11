package com.billion_dollor_company.accessorystore.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.billion_dollor_company.accessorystore.adapters.HomeRVAdapter
import com.billion_dollor_company.accessorystore.databinding.ActivityMainBinding
import com.billion_dollor_company.accessorystore.misc.FinalVariables.Companion.DB_WATCH_ROOT
import com.billion_dollor_company.accessorystore.misc.FinalVariables.Companion.WATCH_IMAGE_URL
import com.billion_dollor_company.accessorystore.misc.FinalVariables.Companion.WATCH_NAME
import com.billion_dollor_company.accessorystore.misc.FinalVariables.Companion.WATCH_PRICE
import com.billion_dollor_company.accessorystore.models.WatchInfo
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), HomeRVAdapter.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    //list of all the watches fetched from the FireStore database
    private lateinit var watchList: ArrayList<WatchInfo>

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        getData()
        buttonClick()
    }

    //to initialise the variables and data structures.
    private fun init() {
        watchList = ArrayList()
        db = FirebaseFirestore.getInstance()
    }

    //it retrieves the data from the FireStore and then calls the setRecycler() function.
    private fun getData() {
        val collectionRef = db.collection(DB_WATCH_ROOT)
        collectionRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val name = document.getString("name") ?: ""
                        val price = document.getLong("price")?.toInt() ?: 0
                        val discount = document.getLong("discount")?.toInt() ?: 0
                        val imageURL = document.getString("imageURL") ?: ""
                        val company = document.getString("company") ?: ""
                        val description = document.getString("description") ?: ""
                        val watch = WatchInfo(name = name, price = price, discount = discount,imageURL = imageURL,company = company, description = description)
                        watchList.add(watch)
                    }
                    setRecycler()
                }
            }
    }

    //it set the rv and sets it in a grid of 2 columns.
    private fun setRecycler() {
        binding.rvWatches.layoutManager = GridLayoutManager(this, 2)
        binding.rvWatches.adapter = HomeRVAdapter(this, watchList, this)
    }

    private fun buttonClick(){
        binding.btGetStarted.setOnClickListener{
            val intent = Intent(this, WatchInfoActivity::class.java)
            // start your next activity
            startActivity(intent)
        }
    }

    override fun onNoteClick(pos: Int) {
        Toast.makeText(this, "Position $pos clicked", Toast.LENGTH_SHORT).show()
    }
}