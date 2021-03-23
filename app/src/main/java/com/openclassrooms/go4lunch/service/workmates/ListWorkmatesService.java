package com.openclassrooms.go4lunch.service.workmates;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.utils.AppInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListWorkmatesService {

    public void getEmployeesInfoFromFirestoreDatabase(ServiceWorkmatesCallback callback) {

        ArrayList<Workmate> list = new ArrayList<>();
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = dbFirestore.collection(AppInfo.ROOT_COLLECTION_ID);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Retrieve all employees information
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Create list
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (!Objects.equals(user.getDisplayName(), document.get("name")) && !Objects.equals(user.getEmail(), document.get("mail"))) {
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
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    public DocumentReference getDocumentReferenceCurrentUser(String documentCurrentUserId) {
        // Get Document in Firestore collection
        return FirebaseFirestore.getInstance().collection(AppInfo.ROOT_COLLECTION_ID)
                                              .document(documentCurrentUserId);
    }

    public void updateDocumentReferenceCurrentUser(String restaurantName, String restaurantId, String documentCurrentUserId) {
        DocumentReference documentReference = getDocumentReferenceCurrentUser(documentCurrentUserId);
        documentReference.update("restaurantName", restaurantName);
        documentReference.update("restaurantSelectedID", restaurantId);
    }

    public void updateCurrentUserListOfLikedRestaurant(String documentCurrentUserId, List<String> listLikedRestaurants) {
        getDocumentReferenceCurrentUser(documentCurrentUserId).update("liked", listLikedRestaurants);
    }
}
