package nextradio.nranalytics.interfaces;

public interface IActions {
    /**
     * favorite the station that the event came from
     */
    public static final String ACTION_FAVORITE_STATION = "favoritestation";
    /**
     * save the event
     */
    public static final String ACTION_SAVE = "save";
    /**
     * share the event
     */
    public static final String ACTION_SHARE = "share";
    /**
     * share the event
     */
    public static final String ACTION_EMAIL = "email";

    /**
     * visit the event's URL
     */
    public static final String ACTION_WEB = "web";
    /**
     * add an event to the user's calendar
     */
    public static final String ACTION_CALENDAR = "calendar";
    /**
     * launch google maps
     */
    public static final String ACTION_FINDNEARBY = "findnearby";
    /**
     * send a SMS message
     */
    public static final String ACTION_SMS = "sms";
    /**
     * show the event's coupon
     */
    public static final String ACTION_COUPON = "coupon";
    /**
     * place a phone call
     */
    public static final String ACTION_PHONENUMBER = "phone";
    /**
     * search the google play store for a song
     */
    public static final String ACTION_MUSIC_PURCHASE_SEARCH = "mp3search";

    /**
     * search the Amazon store for a song(Only for amazon tablets)
     */
    public static final String ACTION_MUSIC_BUY_SONG = "Buy Now";

    /**
     * like the event
     */
    public static final String ACTION_LIKE = "thumbsup";
    /**
     * dislike the event
     */
    public static final String ACTION_DISLIKE = "thumbsdown";

    /**
     * view the list of recently played events
     */
    public static final String ACTION_INTERNAL_VIEW_RECENTLY_PLAYED = "internal_viewrecent";
    /**
     * view a single
     */
    public static final String ACTION_INTERNAL_VIEW_EVENT = "internal_viewevent";

    String PARAM_PHONENUMBER = "phoneNumber";

    String PARAM_URL = "url";

    /**
     * Close fullscreen
     */
}