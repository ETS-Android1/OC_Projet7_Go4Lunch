package com.openclassrooms.go4lunch.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.repositories.WorkmatesRepository;
import java.util.List;

/**
 * ViewModel class used to store a list of @{@link Workmate}, retrieved from a Firestore
 * NoSQL database.
 */
public class WorkmatesViewModel extends ViewModel {

    // Repository to access the ListWorkmatesService
    private WorkmatesRepository workmatesRepository;

    // To store the list of Employees
    private final MutableLiveData<List<Workmate>> listWorkmates = new MutableLiveData<>();

    public WorkmatesViewModel() { /* Empty constructor */ }

    public MutableLiveData<List<Workmate>> getListWorkmates() {
        return listWorkmates;
    }

    public void setWorkmatesRepository(WorkmatesRepository workmatesRepository) {
        this.workmatesRepository = workmatesRepository;
    }

    /**
     * Accesses the getEmployeesInfoFromFirestoreDatabase() method from @{@link WorkmatesRepository}
     * repository class.
     */
    public void getEmployeesInfoFromFirestoreDatabase() {
        workmatesRepository.getEmployeesInfoFromFirestoreDatabase(listWorkmates::postValue);
    }

    /**
     * Accesses the getDocumentReferenceCurrentUser() method from @{@link WorkmatesRepository}
     * repository class.
     * @param documentCurrentUserId : Id of the document in firestore associated with the current user.
     * @return : DocumentReference from firestore database
     */
    public DocumentReference getDocumentReferenceCurrentUser(String documentCurrentUserId) {
        return workmatesRepository.getDocumentReferenceCurrentUser(documentCurrentUserId);
    }

    /**
     * Accesses the updateDocumentReferenceCurrentUser() method from @{@link WorkmatesRepository}
     * repository class.
     * @param restaurantName : Name of a selected restaurant by current user.
     * @param restaurantId : Id of a selected restaurant by current user.
     * @param documentCurrentUserId : Id of the document in firestore associated with the current user.
     */
    public void updateDocumentReferenceCurrentUser(String restaurantName,
                                                   String restaurantId,
                                                   String documentCurrentUserId) {
        workmatesRepository.updateDocumentReferenceCurrentUser(restaurantName,
                                                               restaurantId,
                                                               documentCurrentUserId);
    }

    /**
     * Accesses the updateCurrentUserListOfLikedRestaurant() method from @{@link WorkmatesRepository}
     * repository class.
     * @param documentCurrentUserId : Id of the document in firestore associated with the current user
     * @param listLikedRestaurants : list of liked restaurant by the current user
     */
    public void updateCurrentUserListOfLikedRestaurant(String documentCurrentUserId, List<String> listLikedRestaurants) {
        workmatesRepository.updateCurrentUserListOfLikedRestaurant(documentCurrentUserId, listLikedRestaurants);
    }
}
