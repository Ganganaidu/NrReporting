package nextradio.nranalytics.objects;

import com.google.gson.annotations.Expose;

public class GdPrApprovalObject {

    @Expose
    private Boolean gdprApproved;

    public Boolean getGdprApproved() {
        return gdprApproved;
    }

    public void setGdprApproved(Boolean gdprApproved) {
        this.gdprApproved = gdprApproved;
    }
}
