package network;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

/**
 * created by Mikey 12/16/15
 *
 */
public class ApiManager {
    private static final String ENDPOINT = "https://www.paypal.com/ph/";

    private static ApiManager sInstance;

    private PayPalApiServices mCityCardApi;

    private Gson mGson;

    public static ApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new ApiManager();
        }

        return sInstance;
    }

    public static PayPalApiServices getApi() {
        return getInstance().getStreamUpAPI();
    }
    private ApiManager() {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().build();
//                        .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36")
//                        .addHeader(ApiConstants.HEADER_AUTHORIZATION, ApiConstants.USER_AGENT)
//                        .build();
                return chain.proceed(request);
            }
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.interceptors().add(interceptor);

//
//        File cacheDir = new File(UIUtils.getAppContext().getCacheDir().getAbsolutePath(),
//                UIUtils.getAppContext().getPackageName());
//        Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);
//        okHttpClient.setCache(cache);

        mGson = buildGson();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        mCityCardApi = retrofit.create(PayPalApiServices.class);
    }

    private PayPalApiServices getStreamUpAPI() {
        return mCityCardApi;
    }

    public String getApiUrl() {
        return ENDPOINT;
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return clazz.getName().equals("io.realm.Realm")
                                || clazz.getName().equals("io.realm.internal.Row");
                    }
                })
                .create();
    }

    public Gson getGson() {
        return mGson;
    }
}
