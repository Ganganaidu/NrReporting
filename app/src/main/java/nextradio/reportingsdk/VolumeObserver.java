package nextradio.reportingsdk;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

public class VolumeObserver extends ContentObserver {
    private AudioManager audioManager;

    VolumeObserver(Context context, Handler handler) {
        super(handler);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        if (currentVolume == 0) {
//            //stop listening
//        }
    }
}
