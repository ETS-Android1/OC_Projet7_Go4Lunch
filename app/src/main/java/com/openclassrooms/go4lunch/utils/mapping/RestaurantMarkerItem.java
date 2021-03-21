package com.openclassrooms.go4lunch.utils.mapping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * This class contains all information for a restaurant marker in map.
 */
public class RestaurantMarkerItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;
    private boolean type; // Defines type of marker (selected restaurant or non-selected)
    private final int indice; // Indice of the corresponding Restaurant object in the list
    private final String restaurantId;

    public RestaurantMarkerItem(LatLng position, String title, String snippet, boolean type, int indice, String restaurantId) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.type = type;
        this.indice = indice;
        this.restaurantId = restaurantId;
    }

    // Getter methods
    @NonNull
    @Override
    public LatLng getPosition() { return position; }

    @Nullable
    @Override
    public String getTitle() { return title; }

    @Nullable
    @Override
    public String getSnippet() { return snippet; }

    public boolean getType() { return type; }

    public int getIndice() {
        return indice;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    // Setter methods
    public void setType(boolean type) {
        this.type = type;
    }
}