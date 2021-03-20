package com.openclassrooms.go4lunch.service.workmates;

import com.openclassrooms.go4lunch.model.Workmate;
import java.util.List;

/**
 * Callback interface to get a list of workmates from firestore database
 */
public interface ServiceWorkmatesCallback {
    void onWorkmatesAvailable(List<Workmate> listWorkmates);
}
