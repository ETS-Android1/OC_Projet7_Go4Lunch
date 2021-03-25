package com.openclassrooms.go4lunch.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import java.util.Calendar;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.utils.CustomComparators;
import com.openclassrooms.go4lunch.utils.RatingDisplayHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Adapter class to display all restaurants from list in ListViewFragment RecyclerView,
 * using a @{@link androidx.recyclerview.widget.RecyclerView.ViewHolder} class
 */
public class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Lists
    private final ArrayList<Restaurant> listRestaurant = new ArrayList<>();
    private final ArrayList<Restaurant> listRestaurantBackup = new ArrayList<>();
    private final ArrayList<Workmate> listWorkmates = new ArrayList<>();

    // To access user location
    private final FusedLocationProviderClient locationClient;
    private final Context context;
    private final OnItemRestaurantClickListener onItemRestaurantClickListener;

    // Values attached to a type of ViewHolder
    private final static int VIEW_ITEM = 0;
    private final static int VIEW_FOOTER = 1;

    // ProgressBar visibility status value
    private int progressBarVisibilityStatus = View.INVISIBLE;

    public ListViewAdapter(FusedLocationProviderClient locationClient, Context context,
                           OnItemRestaurantClickListener onItemRestaurantClickListener) {
        this.locationClient = locationClient;
        this.context = context;
        this.onItemRestaurantClickListener = onItemRestaurantClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) { // ViewHolderListView
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
            return new ViewHolderListView(view, onItemRestaurantClickListener);
        }
        else { // ViewHolderFooterListView
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_footer_item, parent, false);
            return new ViewHolderFooterListView(view);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Handle item display
       // Log.i("PERFORMAUTOCOMPLETE", "onBindViewHolder");
        if (holder instanceof ViewHolderListView) {
        //    Log.i("PERFORMAUTOCOMPLETE", "ViewHolderListView");
            // Name
            ((ViewHolderListView) holder).name.setText(listRestaurant.get(position).getName());

            // Address
            ((ViewHolderListView) holder).address.setText(listRestaurant.get(position).getAddress());

            // Distance between restaurant location and user location
            displayDistanceBetweenRestaurantAndUserLocation(((ViewHolderListView) holder), position);

            // Rating
            RatingDisplayHandler.displayRating(((ViewHolderListView) holder).rating.get(0), ((ViewHolderListView) holder).rating.get(1), ((ViewHolderListView) holder).rating.get(2),
                    ((ViewHolderListView) holder).rating.get(3), ((ViewHolderListView) holder).rating.get(4), listRestaurant.get(position).getRating(), context);

            // Closing hours
            if (listRestaurant.get(position).getOpeningAndClosingHours() != null) displayOpenHours(((ViewHolderListView) holder),position);

            // Photo
            displayRestaurantPhoto(((ViewHolderListView) holder), position);

            // Number of workmates
            displayNumberOfWorkmates(holder, position);
        }
        // Handle footer item display
        if (holder instanceof ViewHolderFooterListView) {
            ((ViewHolderFooterListView) holder).progressBar.setVisibility(progressBarVisibilityStatus);
        }
    }


    @Override
    public int getItemCount() {
        return listRestaurant.size();
    }

    /**
     * This method is used to update the list of restaurant each time a new restaurant is detected around
     * user location.
     */
    public void updateListRestaurants(List<Restaurant> newList) {
        Log.i("PERFORMAUTOCOMPLETE", "updateListRestaurants newList : " + newList.size());
        listRestaurant.clear();
        listRestaurant.addAll(newList);
        Log.i("PERFORMAUTOCOMPLETE", "updateListRestaurants listRestaurant " + listRestaurant.size());
        notifyDataSetChanged();
    }

    public void updateListRestaurantsBackup() {
        listRestaurantBackup.addAll(listRestaurant);
    }

    public void restoreListRestaurants() {
        Log.i("PERFORMAUTOCOMPLETE", "PERFORMAUTOCOMPLETE");
        listRestaurant.clear();
        listRestaurant.addAll(listRestaurantBackup);
        notifyDataSetChanged();
    }

    public void updateListWorkmates(List<Workmate> newList) {
        Log.i("LISTWORKMATE", "updateListWorkmates");
        listWorkmates.clear();
        listWorkmates.addAll(newList);
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
            locationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        try {
                            double userLatitude = location.getLatitude();
                            double userLongitude = location.getLongitude();
                            double restaurantLatitude = listRestaurant.get(position).getLatitude();
                            double restaurantLongitude = listRestaurant.get(position).getLongitude();
                            float[] result = new float[1];
                            Location.distanceBetween(userLatitude, userLongitude, restaurantLatitude, restaurantLongitude, result);

                            String distance = ((int) result[0]) + " m";
                            holder.distance.setText(distance);
                        } catch (IndexOutOfBoundsException exception) {
                            exception.printStackTrace();
                        }
                    }).addOnFailureListener(Throwable::printStackTrace);
        }
    }

    /**
     * This method is used to display open hours for a restaurant in each recyclerview item.
     * @param holder : holder containing the item view
     * @param position : restaurant at the indice "position" in list
     */
    private void displayOpenHours(@NonNull ViewHolderListView holder, int position) {
        boolean colorText = false;
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);

        // Get Opening/Closing hours associated to the current day
        ArrayList<String> closingHours = new ArrayList<>();
        ArrayList<String> openingHours = new ArrayList<>();
        closingHours = getClosingAndOpeningHoursForADay(closingHours, currentDay, position, true);
        openingHours = getClosingAndOpeningHoursForADay(closingHours, currentDay, position, false);

        // Check number of opening and closing hours for a day
        if (closingHours.size() == 1) { // Restaurant closed once
            int closingHour = Integer.parseInt(closingHours.get(0).substring(0,2));
            int closingMinutes = Integer.parseInt(closingHours.get(0).substring(2,4));
            int openingHour = Integer.parseInt(openingHours.get(0).substring(0,2));
            int openingMinutes = Integer.parseInt(openingHours.get(0).substring(2,4));
            // Update text
            if (CustomComparators.getTimeDiff(currentHour, currentMinutes, closingHour, closingMinutes) >= 0) { // CLOSED
                holder.hour.setText(context.getResources().getString(R.string.closed));
            }
            else {
                if (CustomComparators.getTimeDiff(currentHour, currentMinutes, closingHour, closingMinutes) > -60) { // CLOSED SOON
                    holder.hour.setText(context.getResources().getString(R.string.closing_soon));
                    colorText = true;
                }
                else {
                    if (CustomComparators.getTimeDiff(currentHour, currentMinutes, openingHour, openingMinutes) > 0) { // OPEN UNTIL
                        String text = context.getResources().getString(R.string.open_until) + " " + closingHours.get(0).substring(0,2)
                                + ":" + closingHours.get(0).substring(2,4);
                        holder.hour.setText(text);
                    }
                    else { // CLOSED (NOT OPENED YET)
                        holder.hour.setText(context.getResources().getString(R.string.closed));
                    }
                }
            }
        }
        if (closingHours.size() == 2) { // Restaurant closed twice
            int firstClosingHour = Integer.parseInt(closingHours.get(0).substring(0,2));
            int firstClosingMinutes = Integer.parseInt(closingHours.get(0).substring(2,4));
            int firstOpeningHour = Integer.parseInt(openingHours.get(0).substring(0,2));
            int firstOpeningMinutes = Integer.parseInt(openingHours.get(0).substring(2,4));

            int secondClosingHour = Integer.parseInt(closingHours.get(1).substring(0,2));
            int secondClosingMinutes = Integer.parseInt(closingHours.get(1).substring(2,4));
            int secondOpeningHour = Integer.parseInt(openingHours.get(1).substring(0,2));
            int secondOpeningMinutes = Integer.parseInt(openingHours.get(1).substring(2,4));
            // Update text
            if (CustomComparators.getTimeDiff(currentHour, currentMinutes, firstOpeningHour, firstOpeningMinutes) < 0) { // CLOSED (NOT OPENING YET)
                holder.hour.setText(context.getResources().getString(R.string.closed));
            }
            else if (CustomComparators.getTimeDiff(currentHour, currentMinutes, firstOpeningHour, firstOpeningMinutes) >= 0 &&
                    CustomComparators.getTimeDiff(currentHour, currentMinutes, firstClosingHour, firstClosingMinutes) < 0) {
                    if (CustomComparators.getTimeDiff(currentHour, currentMinutes, firstClosingHour, firstClosingMinutes) > -60) { // CLOSING SOON
                        holder.hour.setText(context.getResources().getString(R.string.closing_soon));
                        colorText = true;
                    }
                    else { // OPEN UNTIL
                        String text = context.getResources().getString(R.string.open_until) + " " + closingHours.get(0).substring(0,2)
                                                                                            + ":" + closingHours.get(0).substring(2,4);
                        holder.hour.setText(text);
                    }
            }
            else if (CustomComparators.getTimeDiff(currentHour, currentMinutes, firstClosingHour, firstClosingMinutes) >= 0 &&
                    CustomComparators.getTimeDiff(currentHour, currentMinutes, secondOpeningHour, secondOpeningMinutes) < 0) {  // CLOSED (NOT OPENING YET)
                    holder.hour.setText(context.getResources().getString(R.string.closed));
            }
            else if (CustomComparators.getTimeDiff(currentHour, currentMinutes, secondOpeningHour, secondOpeningMinutes) >= 0 &&
                    CustomComparators.getTimeDiff(currentHour, currentMinutes, secondClosingHour, secondClosingMinutes) < 0) {
                if (CustomComparators.getTimeDiff(currentHour, currentMinutes, secondClosingHour, secondClosingHour) > -60) { // CLOSING SOON
                    holder.hour.setText(context.getResources().getString(R.string.closed));
                    colorText = true;
                }
                else { // OPEN UNTIL
                    String text = context.getResources().getString(R.string.open_until) + " " + closingHours.get(1).substring(0,2) + ":" + closingHours.get(1).substring(2,4);
                    holder.hour.setText(text);
                }
            }
            else { // CLOSED
                holder.hour.setText(context.getResources().getString(R.string.closed));
            }
        }

        // Update color text for "closing soon" hours
        if (colorText)
            displayStyleTextViewForHoursDisplay(holder.hour, R.color.red, Typeface.BOLD_ITALIC);
        else
            displayStyleTextViewForHoursDisplay(holder.hour, R.color.grey_50, Typeface.ITALIC);
    }


    /**
     * This method is used to get the list of opening or closing hours for a Restaurant.
     * @param hours : list or hours
     * @param currentDay : current day
     * @param position : position of the Restaurant in the list of Restaurant
     * @param type : type of list (true : Closing hours / false : Opening hours)
     * @return : list of hours
     */
    private ArrayList<String> getClosingAndOpeningHoursForADay(ArrayList<String> hours, int currentDay,
                                                               int position, boolean type) {
        if (type) { // CLOSING HOURS
            switch (currentDay) {
                case 1 : // SUNDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getSundayClosingHours();
                    break;
                case 2: // MONDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getMondayClosingHours();
                    break;
                case 3: // TUESDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getTuesdayClosingHours();
                    break;
                case 4: // WEDNESDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getWednesdayClosingHours();
                    break;
                case 5: // THURSDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getThursdayClosingHours();
                    break;
                case 6: // FRIDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getFridayClosingHours();
                    break;
                case 7 : // SATURDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getSaturdayClosingHours();
                    break;
            }
        }
        else { // OPENING HOURS
            switch (currentDay) {
                case 1 : // SUNDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getSundayOpeningHours();
                    break;
                case 2: // MONDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getMondayOpeningHours();
                    break;
                case 3: // TUESDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getTuesdayOpeningHours();
                    break;
                case 4: // WEDNESDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getWednesdayOpeningHours();
                    break;
                case 5: // THURSDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getThursdayOpeningHours();
                    break;
                case 6: // FRIDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getFridayOpeningHours();
                    break;
                case 7 : // SATURDAY
                    hours = listRestaurant.get(position).getOpeningAndClosingHours().getSaturdayOpeningHours();
                    break;
            }
        }
        return hours;
    }


    /**
     * This method updates the "closing hour" text with a color and a style.
     * @param text : "closing hour" text for a recyclerview item
     * @param color : color to apply
     * @param typeface : style to apply
     */
    private void displayStyleTextViewForHoursDisplay(TextView text, @ColorRes int color, int typeface) {
        text.setTextColor(context.getResources().getColor(color));
        text.setTypeface(null, typeface);
    }

    /**
     * This method displays the associated photo to a restaurant
     * @param holder : holder containing the item view
     * @param position : restaurant at the indice "position" in list
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void displayRestaurantPhoto(@NonNull ViewHolderListView holder, int position) {
        if (listRestaurant.get(position).getPhotoReference() != null) {
            Glide.with(context)
                    .load("https://maps.googleapis.com/maps/api/place/photo?&maxwidth=400&maxheight=400&photo_reference="
                            + listRestaurant.get(position).getPhotoReference() + "&key=" + BuildConfig.API_KEY)
                             .centerCrop()
                    .override(holder.photo.getWidth(), holder.photo.getHeight())
                    .into(holder.photo);
        }
        else {
            holder.photo.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.ic_baseline_image_not_supported_24dp_oyster_white));
        }
    }

    /**
     * This method displays the number of workmates going to a specified restaurant
     * @param holder : holder containing the item view
     * @param position : restaurant at the indice "position" in list
     */
    private void displayNumberOfWorkmates(@NonNull RecyclerView.ViewHolder holder, int position) {
        String restaurantId = listRestaurant.get(position).getPlaceId();
        int nbWorkmates = 0;
        for (int i = 0; i < listWorkmates.size(); i++) {
            if (listWorkmates.get(i).getRestaurantSelectedID().equals(restaurantId))
                nbWorkmates++;
        }
        String text = "(" + nbWorkmates + ")";
        ((ViewHolderListView) holder).nbWorkmates.setText(text);
    }

    /**
     * ViewHolder class to display a Restaurant item using ListViewAdapter.
     */
    private static class ViewHolderListView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView name;
        private final TextView address;
        private final TextView hour;
        private final TextView distance;
        private final ImageView photo;
        private final List<ImageView> rating;
        private final OnItemRestaurantClickListener onItemRestaurantClickListener;
        private final TextView nbWorkmates;

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

            rating = Arrays.asList(view.findViewById(R.id.note_star_5),
                                   view.findViewById(R.id.note_star_4),
                                   view.findViewById(R.id.note_star_3),
                                   view.findViewById(R.id.note_star_2),
                                   view.findViewById(R.id.note_star_1));
            nbWorkmates = view.findViewById(R.id.nb_workmates);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemRestaurantClickListener.onItemRestaurantClick(getAdapterPosition());
        }
    }

    /**
     * ViewHolder class to display a footer item using ListViewAdapter
     */
    private static class ViewHolderFooterListView extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;

        ViewHolderFooterListView(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progress_bar_list_view);
        }
    }

    /**
     * Interface to handle click on RecyclerView items
     */
    public interface OnItemRestaurantClickListener {
        void onItemRestaurantClick(int position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionItem(position)) return VIEW_ITEM;
        else return VIEW_FOOTER;
    }

    private boolean isPositionItem(int position) {
        if (getItemCount() == 1) return true;
        else return position < getItemCount()-1; // last position
    }

    public void updateVisibilityProgressBarStatus(int visibility) {
        progressBarVisibilityStatus = visibility;
        notifyDataSetChanged();
    }

    // Getter
    public List<Restaurant> getListRestaurant() {
        return listRestaurant;
    }

    public List<Restaurant> getListRestaurantBackup() { return listRestaurantBackup; }
}
