package com.openclassrooms.go4lunch.service.authentication;

import android.content.Context;
import com.firebase.ui.auth.AuthUI;
import com.openclassrooms.go4lunch.ui.activities.MainActivityCallback;

public class AuthenticationService {

    public static void logoutUser(Context context, MainActivityCallback callback) {
        AuthUI.getInstance().signOut(context)
                .addOnFailureListener(exception -> callback.exitApplicationAfterError())
                .addOnSuccessListener(aVoid -> callback.updateUIAfterRequestCompleted(true));
    }

    public static void deleteUser(Context context, MainActivityCallback callback) {
        AuthUI.getInstance().delete(context)
                .addOnSuccessListener(aVoid -> callback.updateUIAfterRequestCompleted(false))
                .addOnFailureListener(exception -> callback.exitApplicationAfterError());
    }


}
