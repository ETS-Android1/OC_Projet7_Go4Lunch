package com.openclassrooms.go4lunch.service.workmates;

import com.openclassrooms.go4lunch.model.Workmate;

import java.util.List;

public interface ServiceWorkmatesCallback {

    void onWorkmatesAvailable(List<Workmate> listWorkmates);
}
