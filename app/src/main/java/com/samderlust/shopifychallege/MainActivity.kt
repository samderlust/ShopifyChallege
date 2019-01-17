package com.samderlust.shopifychallege

import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import com.google.gson.GsonBuilder
import java.io.InputStream
import java.net.URL


class MainActivity : AppCompatActivity() {
    lateinit var  shopCollection: ShopCollection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fecthJson()

        collectionList.setOnItemClickListener { parent, view, position, id ->
            val collectionDetail = Intent(applicationContext, CollectionDetail::class.java)

            collectionDetail.putExtra("cTitle", shopCollection.custom_collections.get(position).title)
            collectionDetail.putExtra("imgUrl", shopCollection.custom_collections.get(position).image.src)
            collectionDetail.putExtra("id", shopCollection.custom_collections.get(position).id.toString())

            startActivity(collectionDetail)
        }

    }

    fun fecthJson(){

        val url = "https://shopicruit.myshopify.com/admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6"
        val client = OkHttpClient()

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                shopCollection= gson.fromJson(body, ShopCollection::class.java)

                runOnUiThread {
                    collectionList.adapter = ColAdapter(shopCollection)
                    mainProogressBar.visibility= View.GONE
                }

            }

        })
    }
}

class ShopCollection(val custom_collections: List<Collection>){}
class Collection(val title: String, val id: Long, val image: Image){}
class Image(val src: String){}



class DownloadImage(val imgV: ImageView): AsyncTask<String,Void,Bitmap>() {
    override fun doInBackground(vararg urls: String?): Bitmap {
        val urlOfImage = urls[0]
        var image: Bitmap? = null
        try {
            var input = URL(urlOfImage).openStream() as InputStream
            image = BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return image as Bitmap
    }

    override fun onPostExecute(result: Bitmap) {
        imgV.setImageBitmap(result)
    }

}