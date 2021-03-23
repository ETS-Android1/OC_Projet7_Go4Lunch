package com.openclassrooms.go4lunch.repositories;

import com.google.firebase.firestore.DocumentReference;
import com.openclassrooms.go4lunch.service.workmates.ListWorkmatesService;
import com.openclassrooms.go4lunch.service.workmates.ServiceWorkmatesCallback;

import java.util.List;

/**
 * Repository class to communicate with the @{@link ListWorkmatesService} service class.
 */
public class WorkmatesRepository {

    public ListWorkmatesService listWorkmatesService;

    public WorkmatesRepository() {
        this.listWorkmatesService = new ListWorkmatesService();
    }

    /**
     * This method is used to access the getEmployeesInfoFromFirestoreDatabase() method of the
     * @{@link ListWorkmatesService} service class.
     * @param callback : Callback interface
     */
    public void getEmployeesInfoFromFirestoreDatabase(ServiceWorkmatesCallback callback) {
        listWorkmatesService.getEmployeesInfoFromFirestoreDatabase(callback);
    }

    public DocumentReference getDocumentReferenceCurrentUser(String documentCurrentUserId) {
        return listWorkmatesService.getDocumentReferenceCurrentUser(documentCurrentUserId);
    }

    public void updateDocumentReferenceCurrentUser(String restaurantName,
                                                   String restaurantId,
                                                   String documentCurrentUserId) {
        listWorkmatesService.updateDocumentReferenceCurrentUser(restaurantName,
                                                                restaurantId,
                                                                documentCurrentUserId);
    }

    public void updateCurrentUserListOfLikedRestaurant(String documentCurrentUserId, List<String> listLikedRestaurants) {
        listWorkmatesService.updateCurrentUserListOfLikedRestaurant(documentCurrentUserId, listLikedRestaurants);
    }
}
