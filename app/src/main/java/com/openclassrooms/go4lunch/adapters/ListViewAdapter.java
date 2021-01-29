package com.openclassrooms.go4lunch.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import java.util.Calendar;
import java.util.Locale;
import android.location.Location;
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
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.ListRestaurantsService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolderListView> {

    private ArrayList<Restaurant> list = new ArrayList<>();
    private FusedLocationProviderClient client;
    private Context context;

    public ListViewAdapter(FusedLocationProviderClient client, Context context) {
        this.client = client;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderListView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        return new ViewHolderListView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderListView holder, int position) {
        // Name
        holder.name.setText(list.get(position).getName());

        // Address
        holder.address.setText(list.get(position).getAddress());

        // Distance between restaurant location and user location
        displayDistanceBetweenRestaurantAndUserLocation(holder, position);

        // Image
        holder.photo.setImageBitmap(list.get(position).getPhoto());

        // Rating
        displayRestaurantRating(holder, position);

        // Open hours
        displayOpenHours(holder, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * This method is used to update the list of restaurant each time a new restaurant is detected around
     * user location.
     */
    public void updateList() {
        list.clear();
        list.addAll(ListRestaurantsService.getListRestaurants());
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
                        Double userLatitude = location.getLatitude();
                        Double userLongitude = location.getLongitude();
                        Double restaurantLatitude = list.get(position).getLatLng().latitude;
                        Double restaurantLongitude = list.get(position).getLatLng().longitude;
                        float result[] = new float[1];
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
    }

    /**
     * This method is used to display open hours for a restaurant in each recyclerview item.
     * @param holder : holder containing the item view
     * @param position : restaurant at the indice "position" in list
     */
    private void displayOpenHours(@NonNull ViewHolderListView holder, int position) {
        Calendar calendar = Calendar.getInstance();
        int closingHour = 0;
        int closingMinutes = 0;
        int currentHour;

        // Get current day
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        // Find closing hours for a restaurant, according to the current day
        switch (currentDay) {
            case 0 : // MONDAY
                closingHour = list.get(position).getOpeningHours().getPeriods().get(1).getOpen().getTime().getHours();
                closingMinutes = list.get(position).getOpeningHours().getPeriods().get(1).getOpen().getTime().getMinutes();
                break;
            case 1 : // TUESDAY
                closingHour = list.get(position).getOpeningHours().getPeriods().get(2).getOpen().getTime().getHours();
                closingMinutes = list.get(position).getOpeningHours().getPeriods().get(2).getOpen().getTime().getMinutes();
                break;
            case 2 : // WEDNESDAY
                closingHour = list.get(position).getOpeningHours().getPeriods().get(3).getOpen().getTime().getHours();
                closingMinutes = list.get(position).getOpeningHours().getPeriods().get(3).getOpen().getTime().getMinutes();
                break;
            case 3 : // THURSDAY
                closingHour = list.get(position).getOpeningHours().getPeriods().get(4).getOpen().getTime().getHours();
                closingMinutes = list.get(position).getOpeningHours().getPeriods().get(4).getOpen().getTime().getMinutes();
                break;
            case 4 : // FRIDAY
                closingHour = list.get(position).getOpeningHours().getPeriods().get(5).getOpen().getTime().getHours();
                closingMinutes = list.get(position).getOpeningHours().getPeriods().get(5).getOpen().getTime().getMinutes();
                break;
            case 5 : // SATURDAY
                closingHour = list.get(position).getOpeningHours().getPeriods().get(6).getOpen().getTime().getHours();
                closingMinutes = list.get(position).getOpeningHours().getPeriods().get(6).getOpen().getTime().getMinutes();
                break;
            case 6 : // SUNDAY
                closingHour = list.get(position).getOpeningHours().getPeriods().get(0).getOpen().getTime().getHours();
                closingMinutes = list.get(position).getOpeningHours().getPeriods().get(0).getOpen().getTime().getMinutes();
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
            if (Locale.getDefault().getDisplayLanguage().equals("français")) { // FR
                String textFormatH24 = R.string.open_until + " " + closingHour + ":" + closingMinutes;
                holder.hour.setText(textFormatH24);
            }
            else { // ENG
                String textFormatAMPM;
                if (closingHour > 12) textFormatAMPM = R.string.open_until + " " + (closingHour-12) + "PM";
                else textFormatAMPM = R.string.open_until + " " + closingHour + "AM";
                holder.hour.setText(textFormatAMPM);
            }
        }
    }


    public static class ViewHolderListView extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView address;
        private TextView hour;
        private TextView distance;
        private ImageView photo;
        private List<ImageView> rating;

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
}
