package com.openclassrooms.go4lunch.repositories;

import com.openclassrooms.go4lunch.service.workmates.ListWorkmatesService;
import com.openclassrooms.go4lunch.service.workmates.ServiceWorkmatesCallback;

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

}
