package com.openclassrooms.go4lunch.ui.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;

/**
 * This class is used to detect any "network change" event and update MainActivity UI according
 * to the network status.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    private final LinearLayout connectivityInfoBar;

    public NetworkBroadcastReceiver(LinearLayout connectivityInfoBar) {
        this.connectivityInfoBar = connectivityInfoBar;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) { // Wifi ou Mobile Data network activated -> Hide connectivity info bar
            ViewPropertyAnimator fadeOutAnim = (ViewPropertyAnimator) connectivityInfoBar.animate().alpha(0.0f).setDuration(200);
            fadeOutAnim.withEndAction(() -> connectivityInfoBar.setVisibility(View.GONE));

        }
        else { // No network -> Display connectivity info bar
            connectivityInfoBar.setVisibility(View.VISIBLE);
            ViewPropertyAnimator fadeInAnim = (ViewPropertyAnimator) connectivityInfoBar.animate().alpha(1.0f).setDuration(200);
            fadeInAnim.start();
        }
    }
}
