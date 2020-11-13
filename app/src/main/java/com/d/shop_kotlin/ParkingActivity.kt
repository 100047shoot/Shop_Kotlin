package com.d.shop_kotlin

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_parking.*
import org.jetbrains.anko.*
import java.net.URL

class ParkingActivity : AppCompatActivity(),AnkoLogger {
    private  val TAG = ParkingActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)
        val parking = "http://odws.hccg.gov.tw/001/Upload/25/OpenData/9059/59/67e94b18-b8ba-4c6a-9a2a-e66638a31895.json"
        //ParkingTask().execute(parking)

        //Anko implement
        doAsync {
            var url = URL(parking)
            var json = url.readText()
            info(json)
            uiThread {
                toast("Got it")
                info.text = json
                alert("Got it", "ALERT"){
                    okButton {
                        parseGson(json)
                    }
                }.show()
            }
        }

    }

    private fun parseGson(json: String) {
        val Bikes = Gson().fromJson<Bike>(json,Bike::class.java)
        info(Bikes.size)
        Bikes.forEach {
            info { "${it.站點位置},${it.站點名稱},${it.圖片URL}" }
        }
    }

    inner class ParkingTask : AsyncTask<String, Void, String>(){
        override fun doInBackground(vararg params: String?): String {
            var url = URL(params[0])
            var json = url.readText()
            Log.d(TAG, "doInBackground: $json")
            return json
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Toast.makeText(this@ParkingActivity,"Got it",Toast.LENGTH_LONG).show()
            info.text = result
        }
    }
}

/*
[
{
 "站點名稱":"新竹火車站(前站)",
 "經度":"120.971459",
 "緯度":"24.801815",
 "圖片URL":"http://opendata-manage.hccg.gov.tw/img/24/youbike01.jpg",
 "站點位置":"中華路二段445號(火車站前廣場)",
 "F6":"",
 "F7":""
}
]
 */
class Bike : ArrayList<BikeItem>()

data class BikeItem(
    val F6: String,
    val F7: String,
    val 圖片URL: String,
    val 站點位置: String,
    val 站點名稱: String,
    val 經度: String,
    val 緯度: String
)