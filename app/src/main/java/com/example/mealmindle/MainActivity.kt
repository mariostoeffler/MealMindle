package com.example.mealmindle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mealmindle.data.ProductResponse
import com.example.mealmindle.ui.theme.MealMindleTheme
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealMindleTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                    Button(onClick = { startScan() }) { Text("Scan") }
                }
            }
        }
    }

    private fun startScan() {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Scan a barcode")
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Scan was cancelled", Toast.LENGTH_LONG).show()
            } else {
                val barcode = result.contents

                val call: Call<ProductResponse> = ApiService.instance.getProductDetails(barcode, "876d1460", "25c045fdf1878cb805b586a364171b21")
                call.enqueue(object : Callback<ProductResponse> {
                    override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            val productDetails = response.body() // Hier bekommst du die Antwort als ProductResponse-Objekt
                            // Verarbeite die Antwort hier weiter
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Product not found.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                        runOnUiThread {
                            val errorMessage = "An unexpected error occurred: ${t.localizedMessage}"
                            Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                            Log.e("API_RESPONSE_ERROR", errorMessage)
                            t.printStackTrace()
                        }
                    }
                })
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text(
            text = "Hello $name!",
            modifier = Modifier.fillMaxSize()
        )
    }
}
