package com.openclassrooms.go4lunch.service.authentication;

import android.content.Context;
import com.firebase.ui.auth.AuthUI;
import com.openclassrooms.go4lunch.ui.activities.MainActivityCallback;

/**
 * Service class handling Firebase authentication operations
 */
public class AuthenticationService {

    /**
     * Logs out user
     * @param context : Context
     * @param callback : @{@link MainActivityCallback} callback interface to send back authentication result
     */
    public static void logoutUser(Context context, MainActivityCallback callback) {
        AuthUI.getInstance().signOut(context)
                .addOnFailureListener(exception -> callback.exitApplicationAfterError())
                .addOnSuccessListener(aVoid -> callback.updateUIAfterRequestCompleted(true));
    }

    /**
     * Delete user from Firebase list of users
     * @param context : Context
     * @param callback : @{@link MainActivityCallback} callback interface to send back authentication result
     */
    public static void deleteUser(Context context, MainActivityCallback callback) {
        AuthUI.getInstance().delete(context)
                .addOnSuccessListener(aVoid -> callback.updateUIAfterRequestCompleted(false))
                .addOnFailureListener(exception -> callback.exitApplicationAfterError());
    }


}
