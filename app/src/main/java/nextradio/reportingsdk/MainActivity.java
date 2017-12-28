package nextradio.reportingsdk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import nextradio.nranalytics.controllers.NraController;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //NraController.getInstance().activateApp();
        // appStorage = new NRPersistedAppStorage();

        Button register = findViewById(R.id.register);
        register.setOnClickListener(view -> NraController.getInstance().registerApp("sdk test"));

        Button listening = findViewById(R.id.playBtn);
        listening.setOnClickListener(view -> {
            NraController.getInstance().startListeningSession(931000, 0, 1, "WXRT");
        });

        Button textView = findViewById(R.id.text);
        textView.setOnClickListener(view -> {
            //NRReportingTracker.getInstance().reportDataToServer();
        });

        Button updateData = findViewById(R.id.updateData);
        updateData.setOnClickListener(view -> {
                    //NRListeningSessionLogger.getInstance().updateListeningSession();
//                    NraController.getInstance().recordRadioImpressionEvent("test artist", "test song",
//                            null, 1, 931000, "0", "WXRT");
                }
        );

        Button stopListening = findViewById(R.id.stopBtn);
        stopListening.setOnClickListener(view -> {
            //  NraController.getInstance().stopListeningSession();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPermissions()) {
            requestPermissions();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  NraController.getInstance().deActivateApp();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
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
}
