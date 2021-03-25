package com.openclassrooms.go4lunch.service.workmates;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.utils.AppInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListWorkmatesService {

    private Context context;

    public ListWorkmatesService(Context context) {
        this.context = context;
    }
    public void getEmployeesInfoFromFirestoreDatabase(ServiceWorkmatesCallback callback) {

        ArrayList<Workmate> list = new ArrayList<>();
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = dbFirestore.collection(AppInfo.ROOT_COLLECTION_ID);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sharedPrefUserId = context.getSharedPreferences(AppInfo.FILE_FIRESTORE_USER_ID, Context.MODE_PRIVATE);
        String userId = sharedPrefUserId.getString(AppInfo.PREF_FIRESTORE_USER_ID_KEY, null);
        Log.i("USERID", "User Id  :" + userId);

        // Retrieve all employees information
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Create list
                if (userId != null) {
                    try {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!document.getId().equals(userId)) {
                                // Create Employee and add it to the list (except current user)
                                Workmate workmate = new Workmate(document.getString("name"),
                                        document.getString("email"),
                                        document.getString("restaurantSelectedID"),
                                        document.getString("photoUrl"),
                                        document.getString("restaurantName"));
                                list.add(workmate);
                            }
                        }
                        // Send to ViewModel
                        callback.onWorkmatesAvailable(list);
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    public DocumentReference getDocumentReferenceCurrentUser(String documentCurrentUserId) {
        // Get Document in Firestore collection
        return FirebaseFirestore.getInstance().collection(AppInfo.ROOT_COLLECTION_ID)
                                              .document(documentCurrentUserId);
    }

    public void updateDocumentReferenceCurrentUser(String restaurantName, String restaurantId, String documentCurrentUserId) {
        Log.i("REFERENCEDOCUMENT", "Service :" + documentCurrentUserId);
        DocumentReference documentReference = getDocumentReferenceCurrentUser(documentCurrentUserId);
        documentReference.update("restaurantName", restaurantName);
        documentReference.update("restaurantSelectedID", restaurantId);
    }

    public void updateCurrentUserListOfLikedRestaurant(String documentCurrentUserId, List<String> listLikedRestaurants) {
        getDocumentReferenceCurrentUser(documentCurrentUserId).update("liked", listLikedRestaurants);
    }
}
