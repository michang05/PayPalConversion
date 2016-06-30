package network;

import network.response.ConvertResponse;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Mikey on 12/16/2015.
 */
public interface PayPalApiServices {

//    @GET("cgi-bin/webscr")
//    Call<ConvertResponse> getPayPalConvertedAmount(@Body ConvertRequest request);

    @GET("cgi-bin/webscr")
    Call<ConvertResponse> getPayPalConvertedAmount(@Query("cmd")String cmd
            ,@Query("amount_in") String amountIn
    ,@Query("currency_in") String currencyIn
    ,@Query("currency_out") String currencyOut
    ,@Query("currency_conversion_type") String converstionType);

}
