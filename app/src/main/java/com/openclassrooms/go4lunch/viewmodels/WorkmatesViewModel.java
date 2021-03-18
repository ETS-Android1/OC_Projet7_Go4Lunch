package com.openclassrooms.go4lunch.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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

    public MutableLiveData<List<Workmate>> getListEmployees() {
        return listWorkmates;
    }

    public void setWorkmatesRepository(WorkmatesRepository workmatesRepository) {
        this.workmatesRepository = workmatesRepository;
    }

    /**
     * This method is used to access the Firestore database to retrieve the list of all existing
     * employees.
     */
    public void getEmployeesInfoFromFirestoreDatabase() {
        workmatesRepository.getEmployeesInfoFromFirestoreDatabase(listWorkmates::postValue);
    }
}
