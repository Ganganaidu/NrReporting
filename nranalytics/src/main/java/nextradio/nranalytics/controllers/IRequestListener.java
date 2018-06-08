package nextradio.nranalytics.controllers;

/**
 * Created by gkondati
 */
interface IRequestListener<T> {

    void onResponse(T response);

    void onDisplayError(String errorMsg);
}
