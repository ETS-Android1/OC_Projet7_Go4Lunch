package com.openclassrooms.go4lunch.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.ActivitySignInBinding;
import com.openclassrooms.go4lunch.model.Workmate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

            if (resultCode == RESULT_OK) {
                addUserIdToFirestoreDatabase();
                startMainActivity();
            }
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

    /**
     * This method is used to update Firestore database with current user information, if these
     * information are not stored yet (first authentication).
     */
    private void addUserIdToFirestoreDatabase() {
        SharedPreferences userIdPreferences = getSharedPreferences("user_id", MODE_PRIVATE);
        SharedPreferences.Editor editor = userIdPreferences.edit();

        String id = userIdPreferences.getString("id", null);

        if (id == null) { // If id does not exist, user data not stored in db yet
            // Get user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Create new data object to store in dn
            Workmate workmate = new Workmate(Objects.requireNonNull(user).getDisplayName(),
                                             user.getEmail(),
                           "", // No restaurant selected yet
                                             Objects.requireNonNull(user.getPhotoUrl()).toString(),
                               "");

            // Get db instance
            FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
            dbFirestore.collection("list_employees").add(workmate).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    // Store in SharedPreferences file
                    editor.putString("id", documentReference.getId()).apply();
                }
            });
        }
    }
}