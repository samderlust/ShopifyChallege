package com.samderlust.shopifychallege

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_collection_detail.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class CollectionDetail : AppCompatActivity() {
    lateinit var id : String
    lateinit var productList: ArrayList<Product>
    var productString = ""
    lateinit var productAdapter: ProductAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_detail)

        productList = ArrayList()

        val title = intent.getStringExtra("cTitle")
        val imgUrl = intent.getStringExtra("imgUrl")
        val body_html = intent.getStringExtra("body_html")
        id = intent.getStringExtra("id")

        DownloadImage(detailCollectionImg).execute(imgUrl)

        detailCollectionName.text = title
        htmlText.text = body_html

        fetchProductList()
    }

    fun fetchProductList(){
        val url = "https://shopicruit.myshopify.com/admin/collects.json?collection_id=" + id +"&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                val productList = gson.fromJson(body, ProductList::class.java)

                productList.collects.forEach { str -> productString = productString + str.product_id +","}
                println(productString)
                fetchProductInfo(productString)
            }
        })
    }

    fun fetchProductInfo(productString: String){
        val url = "https://shopicruit.myshopify.com/admin/products.json?ids=" + productString + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                val productList = gson.fromJson(body, ListProduct::class.java)

                productAdapter= ProductAdapter(productList)


                runOnUiThread{
                    progressBar.visibility = View.GONE

                    detailProductList.adapter = productAdapter

                }
            }

        })
    }
}

class ProductList(val collects: List<Item>){}
class Item(val product_id : String){}

class ListProduct(val products:List<Product>)
class Product(val title: String, val variants: List<Stock>){}
class Stock(val inventory_quantity: Int){}