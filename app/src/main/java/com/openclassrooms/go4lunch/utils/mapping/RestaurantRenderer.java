package com.openclassrooms.go4lunch.utils.mapping;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.openclassrooms.go4lunch.R;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class defining how all @{@link RestaurantMarkerItem} item must be defined in map cluster.
 */
public class RestaurantRenderer extends DefaultClusterRenderer<RestaurantMarkerItem> {

    private final Context context;

    // To handle resources to display in markers
    private final AssetManager assetManager;

    // Defines if cluster display must be activated or not
    private boolean clusterActivation;

    public RestaurantRenderer(Context context, GoogleMap map, ClusterManager<RestaurantMarkerItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        assetManager = context.getAssets();
        this.clusterActivation = true;
    }

    /**
     * Called before the marker for a ClusterItem is added to the map.
     * @param item : Item in map
     * @param markerOptions : Options of the associated marker
     */
    @Override
    protected void onBeforeClusterItemRendered(@NonNull RestaurantMarkerItem item,
                                               @NonNull MarkerOptions markerOptions) {
        markerOptions.title(item.getTitle()).icon(getIconMarkerItem(item));
    }

    /**
     * Overriden to define the minimum size of a cluster in map.
     * @param minClusterSize : Minimum size of a cluster
     */
    @Override
    public void setMinClusterSize(int minClusterSize) {
        super.setMinClusterSize(minClusterSize);
    }

    /**
     * Defines which Bitmap must be displayed for a marker according to the restaurant status
     * (selected by at least one workmate, or not).
     * @param item : Restaurant item in map
     * @return : BitmapDescriptor object associated with the corresponding .png file
     */
    private BitmapDescriptor getIconMarkerItem(RestaurantMarkerItem item) {
        InputStream stream;
        Bitmap iconMaker = null;
        try {
            if (item.getType()) stream = assetManager.open("icon_resto_loc_selected.png");
            else stream = assetManager.open("icon_resto_loc.png");
            iconMaker = BitmapFactory.decodeStream(stream);
        } catch (IOException exception) { exception.printStackTrace(); }

        return BitmapDescriptorFactory.fromBitmap(iconMaker);
    }

    /**
     * Defines the color of a cluster icon.
     * @param clusterSize : Not used
     * @return : Resource id associated with the corresponding color
     */
    @Override
    protected int getColor(int clusterSize) {
        return context.getResources().getColor(R.color.grey_50);
    }

    /**
     * Determines whether markers must be displayed with cluster or not.
     * @param cluster : Cluster
     * @return : Boolean value
     */
    @Override
    protected boolean shouldRenderAsCluster(@NonNull Cluster<RestaurantMarkerItem> cluster) {
        if (clusterActivation) return super.shouldRenderAsCluster(cluster);
        else return false;

    }

    // Setter
    public void setClusterActivation(boolean clusterActivation) {
        this.clusterActivation = clusterActivation;
    }
}
