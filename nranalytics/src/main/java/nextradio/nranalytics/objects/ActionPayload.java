package nextradio.nranalytics.objects;

/**
 * This class gives you information about reporting inputs
 */
public class ActionPayload {

    public String mTrackingID;
    public String mTEID;
    public String mCardTrackingID;
    public String mUFID;
    public int mStationID;
    public int mPreviousStationId;
    public int mSourceId;
    /*listenType of the station 2:STREAM OR 1:FM*/
    public int mListenType;
    /*gives you station status like LOCAL or NON-LOCAL*/
    public int mMarket;
    /*gives you Audio listenType like BLUETOOTH,HEADPHONE,SPEAKER*/
    public int mOutPut;
    /*gives you listenType of event like PROMO,AD, ONG*/
    public int mItemType;

    public int currentVolume;


    public ActionPayload(String track, String te, String card, String UFID, int station) {
        mTrackingID = track;
        mTEID = te;
        mCardTrackingID = card;
        mStationID = station;
        mUFID = UFID;
    }

    public ActionPayload(String track, String te, String card, String UFID, int station, int previousStationId, int sourceId) {
        mTrackingID = track;
        mTEID = te;
        mCardTrackingID = card;
        mStationID = station;
        mUFID = UFID;
        mPreviousStationId = previousStationId;
        mSourceId = sourceId;
    }

    /**
     * <summary>new reporting columns added in V4<summary/>
     *
     * @param listenType : listenType of the station 2:STREAM OR 1:FM
     * @param market     : gives you station status like LOCAL or NON-LOCAL
     * @param itemType   : gives you listenType of event like PROMO,AD, ONG
     */
    public void setStreamingReportTypes(int listenType, int market, int itemType) {
        mListenType = listenType;
        mMarket = market;
        mItemType = itemType;
    }

    @Override
    public String toString() {
        return "ActionPayload{" +
                "mTrackingID='" + mTrackingID + '\'' +
                ", mTEID='" + mTEID + '\'' +
                ", mCardTrackingID='" + mCardTrackingID + '\'' +
                ", mUFID='" + mUFID + '\'' +
                ", mStationID=" + mStationID +
                ", mPreviousStationId='" + mPreviousStationId + '\'' +
                ", mSourceId=" + mSourceId +
                '}';
    }
}
