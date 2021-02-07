package com.openclassrooms.go4lunch.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import java.util.Calendar;
import java.util.Locale;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.viewmodels.PlacesViewModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Adapter class to display all restaurants from list in ListViewFragment RecyclerView,
 * using a @{@link androidx.recyclerview.widget.RecyclerView.ViewHolder} class
 */
public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolderListView> implements ListViewAdapterCallback{

    private final ArrayList<Restaurant> list = new ArrayList<>();
    private final FusedLocationProviderClient client;
    private final Context context;
    private final PlacesViewModel placesViewModel;
    private final PlacesClient placesClient;

    private final static int WIDTH_BITMAP_ITEM = 120;
    private final static int HEIGHT_BITMAP_ITEM = 120;

    public ListViewAdapter(FusedLocationProviderClient client, Context context, PlacesViewModel placesViewModel, PlacesClient placesClient) {
        this.client = client;
        this.context = context;
        this.placesViewModel = placesViewModel;
        this.placesClient = placesClient;
    }

    @NonNull
    @Override
    public ViewHolderListView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        return new ViewHolderListView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderListView holder, int position) {
        holder.name.setText(list.get(position).getName());  // Name
        holder.address.setText(list.get(position).getAddress()); // Address
        displayDistanceBetweenRestaurantAndUserLocation(holder, position); // Distance between restaurant location and user location
        placesViewModel.getPlaceDetails(placesClient, position, holder, this);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * This method is used to update the list of restaurant each time a new restaurant is detected around
     * user location.
     */
    public void updateList(List<Restaurant> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    /**
     * This method is used to display the distance between user location and a restaurant location
     * in each recyclerview item.
     * @param holder : holder containing the item view
     * @param position : restaurant at the indice "position" in list
     */
    private void displayDistanceBetweenRestaurantAndUserLocation(@NonNull ViewHolderListView holder, int position) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            client.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        double userLatitude = location.getLatitude();
                        double userLongitude = location.getLongitude();
                        double restaurantLatitude = list.get(position).getLatLng().latitude;
                        double restaurantLongitude = list.get(position).getLatLng().longitude;
                        float[] result = new float[1];
                        Location.distanceBetween(userLatitude, userLongitude, restaurantLatitude, restaurantLongitude, result);

                        String distance = ((int) result[0]) + " m";
                        holder.distance.setText(distance);
                    }).addOnFailureListener(Throwable::printStackTrace);
        }
    }

    /**
     * This method is used to display rating for a restaurant in each recyclerview item.
     * @param holder : holder containing the item view
     * @param position : restaurant at the indice "position" in list
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void displayRestaurantRating(@NonNull ViewHolderListView holder, int position) {
        try {
            double rating = list.get(position).getRating();

            if (rating >= 0 && rating < 0.5) { // 0 STARS
                // DO NOTHING
            }
            else if (rating >= 0.5 && rating < 1) { // 0.5 STARS
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_half_24dp_yellow));
            }
            else if (rating >= 1 && rating < 1.5) { // 1 STAR
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
            }
            else if (rating >= 1.5 && rating < 2) { // 1.5 STARS
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(3).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_half_24dp_yellow));
            }
            else if (rating >= 2 && rating < 2.5) { // 2 STARS
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(3).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
            }
            else if (rating >= 2.5 & rating < 3) { // 2.5 STARS
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(3).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(2).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_half_24dp_yellow));
            }
            else if (rating >= 3 & rating < 3.5) { // 3 STARS
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(3).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(2).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
            }
            else if (rating >= 3.5 && rating < 4) { // 3.5 STARS
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(3).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(2).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(1).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_half_24dp_yellow));
            }
            else if (rating >= 4 && rating < 4.5) { // 4 STARS
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(3).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(2).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(1).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
            }
            else if (rating >= 4.5 && rating < 5) { // 4.5 STARS
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(3).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(2).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(1).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(0).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_half_24dp_yellow));
            }
            else { // 5 STARS
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(3).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(2).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(1).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
                holder.rating.get(0).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method is used to display open hours for a restaurant in each recyclerview item.
     * @param holder : holder containing the item view
     * @param position : restaurant at the indice "position" in list
     */
    private void displayOpenHours(@NonNull ViewHolderListView holder, int position) {
        try {
            Calendar calendar = Calendar.getInstance();
            int closingHour = 0;
            int closingMinutes = 0;
            int currentHour;

            // Get current day
            int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
            // Find closing hours for a restaurant, according to the current day
            switch (currentDay) {
                case 2 : // MONDAY
                    closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                         .get(1).getClose()).getTime().getHours();
                    closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                         .get(1).getClose()).getTime().getMinutes();
                    break;
                case 3 : // TUESDAY
                    closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(2).getClose()).getTime().getHours();
                    closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(2).getClose()).getTime().getMinutes();
                    break;
                case 4 : // WEDNESDAY
                    closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(3).getClose()).getTime().getHours();
                    closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(3).getClose()).getTime().getMinutes();
                    break;
                case 5 : // THURSDAY
                    closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(4).getClose()).getTime().getHours();
                    closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(4).getClose()).getTime().getMinutes();
                    break;
                case 6 : // FRIDAY
                    closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(5).getClose()).getTime().getHours();
                    closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(5).getClose()).getTime().getMinutes();
                    break;
                case 7 : // SATURDAY
                    closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(6).getClose()).getTime().getHours();
                    closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(6).getClose()).getTime().getMinutes();
                    break;
                case 1 : // SUNDAY
                    closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(0).getClose()).getTime().getHours();
                    closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                                        .get(0).getClose()).getTime().getMinutes();
                    break;
            }

            // Format hours and minutes to the language
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            if (currentHour == (closingHour - 1)) { // Restaurant closed in an hour
                holder.hour.setText(R.string.closing_soon);
            }
            else if (currentHour > closingHour) { // Already closed
                holder.hour.setText(R.string.closed);
            }
            else { // Still open
                String closingMinutesStr = closingMinutes < 10 ? closingMinutes + "0" : String.valueOf(closingMinutes);
                if (Locale.getDefault().getDisplayLanguage().equals("français")) { // FR
                    String textFormatH24 = context.getResources().getString(R.string.open_until)
                                           + " " + closingHour + ":" + closingMinutesStr;
                    holder.hour.setText(textFormatH24);
                }
                else { // ENG
                    String textFormatAMPM;
                    if (closingHour > 12) textFormatAMPM = context.getResources().getString(R.string.open_until)
                                                           + " " + (closingHour-12) + ":" + closingMinutesStr + "PM";
                    else textFormatAMPM = context.getResources().getString(R.string.open_until)
                                          + " " + closingHour + ":" + closingMinutesStr + "AM";
                    holder.hour.setText(textFormatAMPM);
                }
            }
        } catch (NullPointerException exception) { exception.printStackTrace(); }
    }

    /**
     * ViewHolder class for ListViewAdapter
     */
    public static class ViewHolderListView extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView address;
        private final TextView hour;
        private final TextView distance;
        private final ImageView photo;
        private final List<ImageView> rating;

        ViewHolderListView(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            distance = view.findViewById(R.id.distance);
            hour = view.findViewById(R.id.hour);
            photo = view.findViewById(R.id.photo_restaurant);

            rating = Arrays.asList(view.findViewById(R.id.note_star_5),
                    view.findViewById(R.id.note_star_4),
                    view.findViewById(R.id.note_star_3),
                    view.findViewById(R.id.note_star_2),
                    view.findViewById(R.id.note_star_1));
        }
    }

    // -------------- ListViewAdapterCallback interface implementation --------------
    /**
     * This method is a executed as a callback function after photos are available to be displayed
     * in RecyclerView.
     * @param position : position of the item in the restaurant list
     * @param holder : view holder associated with the item position
     */
    @Override
    public void updateViewHolderWithPhoto(int position, @NonNull ListViewAdapter.ViewHolderListView holder) {
        holder.photo.setImageBitmap(Bitmap.createScaledBitmap(list.get(position).getPhoto(),
                                                              WIDTH_BITMAP_ITEM,
                                                              HEIGHT_BITMAP_ITEM,
                                                        false));
    }

    @Override
    public void updateViewHolderWithDetails(int position, @NonNull ViewHolderListView holder) {
        displayRestaurantRating(holder, position);
        displayOpenHours(holder,position);
    }
}
