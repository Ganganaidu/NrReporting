package nextradio.nranalytics.controllers;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gkondati
 */
class RestAPIRequest {
    private static final String TAG = "RestAPIRequest";

    private static String BASE_URL;
    private static Retrofit retrofit;
    private static RestAPIRequest instance;

    static RestAPIRequest getInstance() {
        if (instance == null) {
            instance = new RestAPIRequest();
        }
        return instance;
    }

    static Retrofit getRetrofit() {
        BASE_URL = NRPersistedAppStorage.getInstance().getTagURL();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    static Retrofit getXmlRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    void removeRetroObjStack() {
        retrofit = null;
    }

    /**
     * Generic method for doing all API requests in the application
     */
    <T> void doRequest(Call<T> call, final IRequestListener<T> requestListener) {
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
