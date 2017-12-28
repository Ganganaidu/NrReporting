package nextradio.nranalytics.web;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gkondati
 */
public class RestAPIRequest {
    private static final String TAG = "RestAPIRequest";

    public static String BASE_URL = "http://dev-api.tagstation.com/sdk/v1.0/";
    private static Retrofit retrofit;
    private static RestAPIRequest instance;

    public static RestAPIRequest getInstance() {
        if (instance == null) {
            instance = new RestAPIRequest();
        }
        return instance;
    }

    public RestAPIRequest withAPIHost(String hostURL) {
        BASE_URL = hostURL;
        return this;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getXmlRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void removeRetroObjStack() {
        retrofit = null;
    }

    /**
     * Generic method for doing all API requests in the application
     */
    public <T> void doRequest(Call<T> call, final RequestListener<T> requestListener) {
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.code() == 200 || response.code() == 204 || response.code() == 201) {
                    requestListener.onResponse(response.body());
                } else {
                    requestListener.onDisplayError(response.message());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                requestListener.onDisplayError(t.getMessage());
            }
        });
    }
}
