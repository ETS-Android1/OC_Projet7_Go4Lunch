package com.openclassrooms.go4lunch.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import java.util.Calendar;
import java.util.Locale;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.model.Restaurant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Adapter class to display all restaurants from list in ListViewFragment RecyclerView,
 * using a @{@link androidx.recyclerview.widget.RecyclerView.ViewHolder} class
 */
public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolderListView> {

    private final ArrayList<Restaurant> list = new ArrayList<>();
    private final FusedLocationProviderClient client;
    private final Context context;
    private final static int WIDTH_BITMAP_ITEM = 120;
    private final static int HEIGHT_BITMAP_ITEM = 120;
    private final OnItemRestaurantClickListener onItemRestaurantClickListener;

    public ListViewAdapter(FusedLocationProviderClient client, Context context,
                           OnItemRestaurantClickListener onItemRestaurantClickListener) {
        this.client = client;
        this.context = context;
        this.onItemRestaurantClickListener = onItemRestaurantClickListener;
    }

    @NonNull
    @Override
    public ViewHolderListView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        return new ViewHolderListView(view, onItemRestaurantClickListener);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderListView holder, int position) {
        // Name
        holder.name.setText(list.get(position).getName());

        // Address
        holder.address.setText(list.get(position).getAddress());

        // Distance between restaurant location and user location
        displayDistanceBetweenRestaurantAndUserLocation(holder, position);

        // Rating
        if (list.get(position).getRating() != null) displayRestaurantRating(holder, position);

        // Closing hours
        if (list.get(position).getOpeningHours() != null) displayOpenHours(holder,position);

        // Photo
        if (list.get(position).getPhoto() != null) displayRestaurantPhoto(holder, position);
        else holder.photo.setImageDrawable(context.getResources()
                                                  .getDrawable(R.drawable.ic_baseline_image_not_supported_24dp_oyster_white));
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
            else if (rating >= 0.5 && rating < 1) // 0.5 STARS
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_half_24dp_yellow));
            else if (rating >= 1 && rating < 1.5) // 1 STAR
                holder.rating.get(4).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_star_24dp_yellow));
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
        if (list.get(position).getOpeningHours() != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                int closingHour = 0;
                int closingMinutes = 0;
                int currentHour;
                int currentMinutes = 0;
                // Get current day
                int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
                // Find closing hours for a restaurant, according to the current day
                switch (currentDay) {
                    case 2 : // MONDAY
                        if (list.get(position).getOpeningHours().getPeriods().size() >= 2) {
                            if (list.get(position).getOpeningHours().getPeriods().get(1) != null) {
                                closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(1).getClose()).getTime().getHours();
                                closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(1).getClose()).getTime().getMinutes();
                            }
                        }
                        break;
                    case 3 : // TUESDAY
                        if (list.get(position).getOpeningHours().getPeriods().size() >= 3) {
                            if (list.get(position).getOpeningHours().getPeriods().get(2) != null) {
                                closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(2).getClose()).getTime().getHours();
                                closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(2).getClose()).getTime().getMinutes();
                            }
                        }
                        break;
                    case 4 : // WEDNESDAY
                        if (list.get(position).getOpeningHours().getPeriods().size() >= 4) {
                            if (list.get(position).getOpeningHours().getPeriods().get(3) != null) {
                                closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(3).getClose()).getTime().getHours();
                                closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(3).getClose()).getTime().getMinutes();
                            }
                        }
                        break;
                    case 5 : // THURSDAY
                        if (list.get(position).getOpeningHours().getPeriods().size() >= 5) {
                            if (list.get(position).getOpeningHours().getPeriods().get(4) != null) {
                                closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(4).getClose()).getTime().getHours();
                                closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(4).getClose()).getTime().getMinutes();
                            }
                        }
                        break;
                    case 6 : // FRIDAY
                        if (list.get(position).getOpeningHours().getPeriods().size() >= 6) {
                            if (list.get(position).getOpeningHours().getPeriods().get(5) != null) {
                                closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(5).getClose()).getTime().getHours();
                                closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(5).getClose()).getTime().getMinutes();
                            }
                        }
                        break;
                    case 7 : // SATURDAY
                        if (list.get(position).getOpeningHours().getPeriods().size() == 7) {
                            if (list.get(position).getOpeningHours().getPeriods().get(6) != null) {
                                closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(6).getClose()).getTime().getHours();
                                closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(6).getClose()).getTime().getMinutes();
                            }
                        }
                        break;
                    case 1 : // SUNDAY
                        if (list.get(position).getOpeningHours().getPeriods().size() >= 1) {
                            if (list.get(position).getOpeningHours().getPeriods().get(0) != null) {
                                closingHour = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(0).getClose()).getTime().getHours();
                                closingMinutes = Objects.requireNonNull(list.get(position).getOpeningHours().getPeriods()
                                        .get(0).getClose()).getTime().getMinutes();
                            }
                        }
                        break;
                }

                // Format hours and minutes to the language
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinutes = calendar.get(Calendar.MINUTE);

                if (currentHour > closingHour) {
                    holder.hour.setText(R.string.closed); // Closed
                    displayStyleTextViewForHoursDisplay(holder.hour, R.color.grey_50);
                }
                else if (currentHour == closingHour) {
                    if (currentMinutes >= closingMinutes) {
                        holder.hour.setText(R.string.closed);
                        displayStyleTextViewForHoursDisplay(holder.hour, R.color.grey_50);
                    }
                    else {
                        if (Math.abs(currentMinutes - closingMinutes) <= 30) {
                            holder.hour.setText(R.string.closing_soon);
                            displayStyleTextViewForHoursDisplay(holder.hour, R.color.red);
                        }
                        else {
                            displayFormatHours(holder, closingMinutes, closingHour);
                            displayStyleTextViewForHoursDisplay(holder.hour, R.color.grey_50);
                        }
                    }
                }
                else if (currentHour == closingHour-1) {
                    if (Math.abs(currentMinutes - closingMinutes) <= 30){
                        holder.hour.setText(R.string.closing_soon);
                        displayStyleTextViewForHoursDisplay(holder.hour, R.color.red);
                    }
                    else {
                        displayFormatHours(holder, closingMinutes, closingHour);
                        displayStyleTextViewForHoursDisplay(holder.hour, R.color.grey_50);
                    }
                }
                else {
                    displayFormatHours(holder, closingMinutes, closingHour);
                    displayStyleTextViewForHoursDisplay(holder.hour, R.color.grey_50);
                }
            } catch (NullPointerException exception) { exception.printStackTrace(); }
        }
    }

    /**
     * This method displays the correct hour format according to device language
     * @param holder : holder containing the item view
     * @param closingMinutes : Closing minutes for a restaurant
     * @param closingHour : CLosing hour for a restaurant
     */
    private void displayFormatHours(@NonNull ViewHolderListView holder, int closingMinutes, int closingHour) {
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

    /**
     * This method updates the "closing hour" text with a color
     * @param text : "closing hour" text for a recyclerview item
     * @param color : color to apply
     */
    private void displayStyleTextViewForHoursDisplay(TextView text, @ColorRes int color) {
        text.setTextColor(context.getResources().getColor(color));
        text.setTypeface(null, Typeface.BOLD_ITALIC);
    }

    /**
     * This method displays the associated photo to a restaurant
     * @param holder : holder containing the item view
     * @param position : restaurant at the indice "position" in list
     */
    private void displayRestaurantPhoto(@NonNull ViewHolderListView holder, int position) {
        holder.noImageIcon.setVisibility(View.GONE);
        holder.photo.setImageBitmap(Bitmap.createScaledBitmap(list.get(position).getPhoto(),
                WIDTH_BITMAP_ITEM,
                HEIGHT_BITMAP_ITEM,
                false));
    }

    /**
     * ViewHolder class for ListViewAdapter
     */
    public static class ViewHolderListView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView name;
        private final TextView address;
        private final TextView hour;
        private final TextView distance;
        private final ImageView photo;
        private final List<ImageView> rating;
        private final OnItemRestaurantClickListener onItemRestaurantClickListener;
        private final ImageView noImageIcon;

        @SuppressLint("ClickableViewAccessibility")
        ViewHolderListView(View view, OnItemRestaurantClickListener onItemRestaurantClickListener) {
            super(view);
            this.onItemRestaurantClickListener = onItemRestaurantClickListener;

            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            distance = view.findViewById(R.id.distance);
            hour = view.findViewById(R.id.hour);
            photo = view.findViewById(R.id.photo_restaurant);
            View layout = view.findViewById(R.id.root_layout_item);
            noImageIcon = view.findViewById(R.id.no_image_icon);

            rating = Arrays.asList(view.findViewById(R.id.note_star_5),
                                   view.findViewById(R.id.note_star_4),
                                   view.findViewById(R.id.note_star_3),
                                   view.findViewById(R.id.note_star_2),
                                   view.findViewById(R.id.note_star_1));

            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemRestaurantClickListener.onItemRestaurantClick(getAdapterPosition());
        }
    }

    /**
     * Interface to handle click on RecyclerView items
     */
    public interface OnItemRestaurantClickListener {
        void onItemRestaurantClick(int position);
    }
}
