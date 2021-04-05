package com.openclassrooms.go4lunch.repositories;

import android.content.Context;
import com.google.firebase.firestore.DocumentReference;
import com.openclassrooms.go4lunch.service.workmates.ListWorkmatesService;
import com.openclassrooms.go4lunch.service.workmates.ServiceWorkmatesCallback;
import java.util.List;

/**
 * Repository class to communicate with the @{@link ListWorkmatesService} service class.
 */
public class WorkmatesRepository {

    private final ListWorkmatesService listWorkmatesService;

    public WorkmatesRepository(Context context) {
        this.listWorkmatesService = new ListWorkmatesService(context);
    }

    /**
     * Accesses the getEmployeesInfoFromFirestoreDatabase() method of the @{@link ListWorkmatesService}
     * service class.
     * @param callback : @{@link ServiceWorkmatesCallback} callback interface
     */
    public void getEmployeesInfoFromFirestoreDatabase(ServiceWorkmatesCallback callback) {
        listWorkmatesService.getEmployeesInfoFromFirestoreDatabase(callback);
    }

    /**
     * Accesses the getDocumentReferenceCurrentUser() method of the @{@link ListWorkmatesService}
     * service class.
     * @param documentCurrentUserId : Id Document in Firestore of the current user
     */
    public DocumentReference getDocumentReferenceCurrentUser(String documentCurrentUserId) {
        return listWorkmatesService.getDocumentReferenceCurrentUser(documentCurrentUserId);
    }

    /**
     * Accesses the updateDocumentReferenceCurrentUser() method of the @{@link ListWorkmatesService}
     * service class.
     * @param restaurantName : Name of the restaurant
     * @param restaurantId : Id of the restaurant
     * @param documentCurrentUserId : Id Document in Firestore of the current user
     */
    public void updateDocumentReferenceCurrentUser(String restaurantName,
                                                   String restaurantId,
                                                   String documentCurrentUserId) {
        listWorkmatesService.updateDocumentReferenceCurrentUser(restaurantName,
                                                                restaurantId,
                                                                documentCurrentUserId);
    }

    /**
     * Accesses the updateCurrentUserListOfLikedRestaurant() method of the @{@link ListWorkmatesService}
     * service class.
     * @param documentCurrentUserId : Id Document in Firestore of the current user
     * @param listLikedRestaurants : list of "liked" restaurant
     */
    public void updateCurrentUserListOfLikedRestaurant(String documentCurrentUserId, List<String> listLikedRestaurants) {
        listWorkmatesService.updateCurrentUserListOfLikedRestaurant(documentCurrentUserId, listLikedRestaurants);
    }
}
