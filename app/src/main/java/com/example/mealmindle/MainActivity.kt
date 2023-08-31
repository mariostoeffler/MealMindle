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
import androidx.lifecycle.lifecycleScope
import com.example.mealmindle.api.ApiService
import com.example.mealmindle.ui.theme.MealMindleTheme
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.mealmindle.data.ProductResponse
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import java.lang.reflect.Type

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

                lifecycleScope.launch {
                    try {
                        val response = ApiService.productApi.getProductDetails(barcode)
                        if (response.isSuccessful) {
                            val responseBody = response.body()?.string()

                            // Debugging-Ausgabe hinzufügen, um den Inhalt der Antwort anzuzeigen
                            Log.d("API_RESPONSE", "Response Body: $responseBody")

                            val gson = GsonBuilder().setLenient().create()
                            val productType: Type = object : TypeToken<ProductResponse>() {}.type
                            val productResponse = gson.fromJson<ProductResponse>(responseBody, productType)

                            withContext(Dispatchers.Main) {
                                if (productResponse != null) {
                                    val productName = productResponse.product.product_name
                                    val quantity = productResponse.product.quantity
                                    // ... access other properties as needed ...

                                    val message = "Product Name: $productName\nQuantity: $quantity"
                                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this@MainActivity, "Error parsing API response", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@MainActivity, "An error occurred.", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            val errorMessage = "An unexpected error occurred: ${e.localizedMessage}"
                            Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                            Log.e("API_RESPONSE_ERROR", errorMessage)
                            e.printStackTrace()
                        }
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
}
