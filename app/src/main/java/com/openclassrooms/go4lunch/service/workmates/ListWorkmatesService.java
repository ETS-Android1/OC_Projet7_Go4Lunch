package com.openclassrooms.go4lunch.service.workmates;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.utils.AppInfo;
import com.openclassrooms.go4lunch.utils.CustomComparators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service class to access the list of workmates stored in Firestore database
 */
public class ListWorkmatesService {

    private final Context context;

    public ListWorkmatesService(Context context) {
        this.context = context;
    }

    /**
     * Retrieves a list of workmates from Firestore database.
     * @param callback @{@link ServiceWorkmatesCallback} callback interface to send back results
     */
    public void getEmployeesInfoFromFirestoreDatabase(ServiceWorkmatesCallback callback) {
        ArrayList<Workmate> list = new ArrayList<>();
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = dbFirestore.collection(AppInfo.ROOT_COLLECTION_ID);

        SharedPreferences sharedPrefUserId = context.getSharedPreferences(
                                               AppInfo.FILE_FIRESTORE_USER_ID, Context.MODE_PRIVATE);
        String userId = sharedPrefUserId.getString(AppInfo.PREF_FIRESTORE_USER_ID_KEY, null);

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
                        Collections.sort(list, new CustomComparators.WorkmateAZComparator());
                        callback.onWorkmatesAvailable(list);
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    /**
     * Retrieves the DocumentReference object associated with the user documentReference id.
     * @param documentCurrentUserId : Id of the document in collection
     * @return : DocumentReference
     */
    public DocumentReference getDocumentReferenceCurrentUser(String documentCurrentUserId) {
        // Get Document in Firestore collection
        return FirebaseFirestore.getInstance().collection(AppInfo.ROOT_COLLECTION_ID)
                                              .document(documentCurrentUserId);
    }

    /**
     * Updates the fields "restaurantId" et "restaurantName" of a document in collection
     * @param restaurantName : New value for the field "restaurantName"
     * @param restaurantId : New value for the field "restaurantId"
     * @param documentCurrentUserId : Id of the document in collection
     */
    public void updateDocumentReferenceCurrentUser(String restaurantName,
                                                   String restaurantId, String documentCurrentUserId) {
        DocumentReference documentReference = getDocumentReferenceCurrentUser(documentCurrentUserId);
        documentReference.update("restaurantName", restaurantName);
        documentReference.update("restaurantSelectedID", restaurantId);
    }

    /**
     * Updates the field "liked" of a document in collection
     * @param documentCurrentUserId : New value for the field "liked"
     * @param listLikedRestaurants : Id of the document in collection
     */
    public void updateCurrentUserListOfLikedRestaurant(String documentCurrentUserId,
                                                       List<String> listLikedRestaurants) {
        getDocumentReferenceCurrentUser(documentCurrentUserId).update("liked", listLikedRestaurants);
    }

    /**
     * Delete Document from database.
     * @param documentCurrentUserId : id of the document to delete
     */
    public void deleteDocument(String documentCurrentUserId) {
        getDocumentReferenceCurrentUser(documentCurrentUserId).delete();
    }
}
