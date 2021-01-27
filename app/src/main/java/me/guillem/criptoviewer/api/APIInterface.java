package me.guillem.criptoviewer.api;

import io.reactivex.Single;
import me.guillem.criptoviewer.retrofit.CryptoList;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * * Created by Guillem on 27/01/21.
 */
public interface APIInterface {

    //https://pro.coinmarketcap.com/api/v1#section/Quick-Start-Guide
    //@Headers(apikey)

    @GET("/v1/cryptocurrency/listings/latest?")
    Single<CryptoList> getMarketPairsLatest(@Query("limit") String limit);

    //@GET("/v1/cryptocurrency/info")
    //Single<Info> getCryptocurrencyInfo(@Query("symbol") String symbol);

}