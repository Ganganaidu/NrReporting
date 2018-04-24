package nextradio.nranalytics.controllers;

/**
 * Created by gkondati
 */
interface RequestListener<T> {

    void onResponse(T response);

    void onDisplayError(String errorMsg);
}
