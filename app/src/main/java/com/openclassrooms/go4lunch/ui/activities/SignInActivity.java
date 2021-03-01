package com.openclassrooms.go4lunch.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.ActivitySignInBinding;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to handle sign-in operation, using FirebaseUI Auth SDK
 */
public class SignInActivity extends AppCompatActivity {

    // Request Code
    private static final int RC_SIGN_IN = 100;

    // View Binding
    private ActivitySignInBinding binding;

    // Authentication providers
    private static final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // Check if user is currently logged-in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
        }

        handleConnexionButtonListener();
    }

    /**
     * This method handles click event on connexion button. When a click is detected, the authentication
     * activity is displayed using FirebaseUI
     */
    private void handleConnexionButtonListener() {
        AuthMethodPickerLayout layout = new AuthMethodPickerLayout.Builder(R.layout.authentication_layout)
                .setGoogleButtonId(R.id.google_auth_btn)
                .setFacebookButtonId(R.id.facebook_auth_btn).build();

        binding.buttonConnexion.setOnClickListener((View v) -> {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setAuthMethodPickerLayout(layout)
                                    .setTheme(R.style.LoginTheme)
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN
                    );
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
        );
    }

    /**
     * This method starts the main activity if user is already logged-in or, after successful
     * authentication.
     */
    private void startMainActivity() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) startMainActivity();
            else {
                if (response == null) {
                    Snackbar.make(binding.signInActivityLayout, R.string.snack_bar_auth_cancelled, Snackbar.LENGTH_SHORT).show();
                }
                else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(binding.signInActivityLayout, R.string.snack_bar_error_no_network, Snackbar.LENGTH_SHORT).show();
                }
                else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Snackbar.make(binding.signInActivityLayout, R.string.snack_bar_error_unknown, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }
}