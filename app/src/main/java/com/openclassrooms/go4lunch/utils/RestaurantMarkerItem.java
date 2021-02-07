package com.openclassrooms.go4lunch.utils;

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
    private final boolean type; // Defines type of marker (selected restaurant or non-selected)

    public RestaurantMarkerItem(LatLng position, String title, String snippet, boolean type) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.type = type;
    }

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
}
