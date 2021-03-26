package com.openclassrooms.go4lunch.service.authentication;

import android.content.Context;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.openclassrooms.go4lunch.ui.activities.MainActivityCallback;

public class AuthenticationService {

    public static void logoutUser(Context context, MainActivityCallback callback) {
        AuthUI.getInstance().signOut(context)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        callback.exitApplicationAfterError();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.updateUIAfterRequestCompleted(true);
                    }
                });
    }

    public static void deleteUser(Context context, MainActivityCallback callback) {
        AuthUI.getInstance().delete(context)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.updateUIAfterRequestCompleted(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.exitApplicationAfterError();
                    }
                });
    }


}
