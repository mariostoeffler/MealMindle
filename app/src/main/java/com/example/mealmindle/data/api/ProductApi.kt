import com.example.mealmindle.data.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {
    @GET("api/food-database/v2/parser")
    fun getProductDetails(
        @Query("upc") upc: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String
    ): Call<ProductResponse>
}