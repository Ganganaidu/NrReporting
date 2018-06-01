package nextradio.reportingsdk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    int data = 0;

    private VolumeObserver volumeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        NextRadioAnalyticsHelper.activateApp();

//        Button register = findViewById(R.id.register);
//        register.setOnClickListener(view -> NextRadioReportingSDK.registerApp("sdk test"));

        Button listening = findViewById(R.id.playBtn);
        listening.setOnClickListener(view -> {
            data = data + 1;
            if (data == 1) {
                NextRadioAnalyticsHelper.startListeningSession(93100000, 1, 1, "WXRT");
            } else if (data == 2) {
                NextRadioAnalyticsHelper.startListeningSession(93100000, 1, 1, "WXRT");
            } else if (data == 3) {
                NextRadioAnalyticsHelper.startListeningSession(93100000, 1, 1, "WXRT");
            } else if (data == 4) {
                NextRadioAnalyticsHelper.startListeningSession(93100000, 1, 1, "WXRT");
            }
        });

        Button textView = findViewById(R.id.text);//send Data
        textView.setOnClickListener(view -> {

        });

        Button updateData = findViewById(R.id.updateData);
        updateData.setOnClickListener(view -> {
                    //NRListeningSessionLogger.getInstance().updateListeningSession();
                    NextRadioAnalyticsHelper.recordRadioImpressionEvent("", "",
                            "Data", 101900000, 1, 0, "WTMX", 1, false);
                }
        );

        Button stopListening = findViewById(R.id.stopBtn);
        stopListening.setOnClickListener(view -> {
            NextRadioAnalyticsHelper.stopListeningSession();
        });

        setUpVolumeObserver();
    }

    private void setUpVolumeObserver() {
        volumeObserver = new VolumeObserver(this, new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, volumeObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPermission()) {
            requestPermissions();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NextRadioAnalyticsHelper.deActivateApp();
        getApplicationContext().getContentResolver().unregisterContentObserver(volumeObserver);
    }

    /**
     * @return TRUE if any one of these location permission are missing or if user denies the location permission
     */
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        boolean shouldProvide = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale || shouldProvide) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: ");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
