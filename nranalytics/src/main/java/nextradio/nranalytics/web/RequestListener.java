package nextradio.nranalytics.web;

/**
 * Created by gkondati
 */
public interface RequestListener<T> {

    void onResponse(T response);

    void onDisplayError(String errorMsg);
}
