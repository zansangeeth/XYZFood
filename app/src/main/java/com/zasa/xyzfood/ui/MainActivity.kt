package com.zasa.xyzfood.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.zasa.xyzfood.R
import com.zasa.xyzfood.adapter.BranchAdapter
import com.zasa.xyzfood.response.BranchResult
import com.zasa.xyzfood.api.RestaurantApi
import com.zasa.xyzfood.utlis.Utils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val branches = mutableListOf<BranchResult>()
        val branchAdapter = BranchAdapter(this, branches)

        val retrofit =
            Retrofit.Builder().baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val retrofitService = retrofit.create(RestaurantApi::class.java)

        retrofitService.getBranches().enqueue(object : Callback<List<BranchResult>> {
            override fun onResponse(
                call: Call<List<BranchResult>>,
                response: Response<List<BranchResult>>
            ) {
                val body = response.body()
                Log.i(TAG, "responce : $body")
                rvBraches.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = branchAdapter
                    branches.addAll(body!!)
                    branchAdapter.notifyDataSetChanged()
                }

                branchAdapter.setOnItemClickListener(object : BranchAdapter.onItemClickListner{
                    override fun onItemClick(position: Int) {
                        val branchIntent = Intent(this@MainActivity, BranchActivity::class.java)
                        branchIntent.putExtra("image", branches[position].image_url)
                        branchIntent.putExtra("city", branches[position].name)
                        branchIntent.putExtra("description", branches[position].description)
                        startActivity(branchIntent)
                    }

                })
            }

            override fun onFailure(call: Call<List<BranchResult>>, t: Throwable) {
                Log.i(TAG, "failure : $t")
            }

        })


    }
}