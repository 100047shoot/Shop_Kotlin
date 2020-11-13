package com.d.shop_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_bus.*
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.row_bus.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class BusActivity : AppCompatActivity(),AnkoLogger {

    var buses: Buses? = null
    val retrofit = Retrofit.Builder()
            .baseUrl("https://data.tycg.gov.tw/opendata/datalist/datasetMeta/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)
//      get json
        doAsync {
            val busService = retrofit.create(BusService::class.java)
            buses = busService.listBuses()
                    .execute()
                    .body()

            buses?.datas?.forEach {
                info {"${it.BusID},${it.RouteID},${it.Speed}"}
            }

            uiThread {
                recycler_bus.layoutManager = LinearLayoutManager(this@BusActivity)
                recycler_bus.setHasFixedSize(true)
                recycler_bus.adapter = BusAdapter()
            }
        }

    }

    inner class BusAdapter(): RecyclerView.Adapter<BusHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_bus,parent,false)
            return BusHolder(view)
        }

        override fun onBindViewHolder(holder: BusHolder, position: Int) {
            var bus = buses?.datas?.get(position)
            holder.bindBus(bus!!)
        }

        override fun getItemCount(): Int {
            return buses?.datas?.size?:0
        }

    }

    inner class BusHolder(view: View): RecyclerView.ViewHolder(view){
        val busIdText = view.bus_id
        val busRouteIdText = view.bus_routeid
        val speedText = view.bus_speed
        fun bindBus(bus: Bus){
            busIdText.text = bus.BusID
            busRouteIdText.text = bus.RouteID
            speedText.text = bus.Speed
        }
    }
}
data class Buses(
    val datas: List<Bus>
)

data class Bus(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val ledstate: String,
    val sections: String
)

interface BusService{
    @GET("download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed")
    fun listBuses(): Call<Buses>
}

// {
//  "datas" :
//    [
//        {
//            "BusID" : "883-FU",
//            "ProviderID" : "45",
//            "DutyStatus" : "2",
//            "BusStatus" : "0",
//            "RouteID" : "65325",
//            "GoBack" : "1",
//            "Longitude" : "121.24326",
//            "Latitude" : "24.962755",
//            "Speed" : "0",
//            "Azimuth" : "0",
//            "DataTime" : "2016-08-29 10:30:54",
//            "ledstate" : "0",
//            "sections" : "1"
//        }
//    ]
//}