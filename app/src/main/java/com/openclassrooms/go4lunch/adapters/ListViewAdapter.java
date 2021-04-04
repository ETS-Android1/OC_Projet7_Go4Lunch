package com.openclassrooms.go4lunch.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import java.util.Calendar;
import android.graphics.Typeface;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.ListViewFooterItemBinding;
import com.openclassrooms.go4lunch.databinding.ListViewItemBinding;
import com.openclassrooms.go4lunch.model.OpeningAndClosingHours;
import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.model.ScheduleType;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.utils.AppInfo;
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
            ListViewItemBinding binding = ListViewItemBinding.inflate(LayoutInflater.from(context), parent, false);
            return new ViewHolderListView(binding, onItemRestaurantClickListener);
        }
        else { // ViewHolderFooterListView
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_footer_item, parent, false);
            ListViewFooterItemBinding binding = ListViewFooterItemBinding.inflate(LayoutInflater.from(context), parent, false);
            return new ViewHolderFooterListView(binding);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Handle item display
        if (holder instanceof ViewHolderListView) {
            // Name
            ((ViewHolderListView) holder).binding.name.setText(listRestaurant.get(position).getName());

            // Address
            ((ViewHolderListView) holder).binding.address.setText(listRestaurant.get(position).getAddress());

            // Distance between restaurant location and user location
            displayDistanceBetweenRestaurantAndUserLocation(((ViewHolderListView) holder), position);

            // Rating
            RatingDisplayHandler.displayRating(((ViewHolderListView) holder).rating.get(0), ((ViewHolderListView) holder).rating.get(1), ((ViewHolderListView) holder).rating.get(2),
                    ((ViewHolderListView) holder).rating.get(3), ((ViewHolderListView) holder).rating.get(4), listRestaurant.get(position).getRating(), context);

            // Closing hours
            if (listRestaurant.get(position).getOpeningAndClosingHours() != null) displayClosingHours(((ViewHolderListView) holder),position);

            // Photo
            displayRestaurantPhoto(((ViewHolderListView) holder), position);

            // Number of workmates
            displayNumberOfWorkmates(holder, position);
        }
        // Handle footer item display
        if (holder instanceof ViewHolderFooterListView) {
            ((ViewHolderFooterListView) holder).binding.progressBarListView.setVisibility(progressBarVisibilityStatus);
        }
    }


    @Override
    public int getItemCount() {
        return listRestaurant.size();
    }

    /**
     * Updates the list of restaurant to display (all restaurants or autocomplete results).
     */
    public void updateListRestaurants(List<Restaurant> newList) {
        listRestaurant.clear();
        listRestaurant.addAll(newList);
        notifyDataSetChanged();
    }

    /**
     * Updates the backup list of Restaurants, containing the list of all detected restaurants
     * around user location.
     */
    public void updateListRestaurantsBackup() {
        listRestaurantBackup.addAll(listRestaurant);
    }

    /**
     * Restores the list of all restaurants in backup to reinitialize display after autocompletion
     * ends.
     */
    public void restoreListRestaurants() {
        listRestaurant.clear();
        listRestaurant.addAll(listRestaurantBackup);
        notifyDataSetChanged();
    }

    /**
     * Updates the list of workmates.
     * @param newList : List of workmates get from Firestore database
     */
    public void updateListWorkmates(List<Workmate> newList) {
        listWorkmates.clear();
        listWorkmates.addAll(newList);
        notifyDataSetChanged();
    }

    /**
     * This method is used to display the distance between user location and a restaurant location
     * in each recyclerview item.
     * @param holder : Holder containing the item view
     * @param position : Restaurant at the indice "position" in list
     */
    @SuppressLint("MissingPermission")
    private void displayDistanceBetweenRestaurantAndUserLocation(@NonNull ViewHolderListView holder, int position) {
        if (AppInfo.checkIfLocationPermissionIsGranted(context)) {
            locationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        try {
                            double userLatitude = location.getLatitude();
                            double userLongitude = location.getLongitude();
                            double restaurantLatitude = listRestaurant.get(position).getLatitude();
                            double restaurantLongitude = listRestaurant.get(position).getLongitude();
                            float[] result = new float[1];
                            Location.distanceBetween(userLatitude, userLongitude, restaurantLatitude, restaurantLongitude, result);
                            String distance = context.getResources().getString(R.string.distance, (int) result[0]);
                            holder.binding.distance.setText(distance);
                        } catch (IndexOutOfBoundsException exception) { exception.printStackTrace(); }
                    }).addOnFailureListener(Throwable::printStackTrace);
        }
    }

    /**
     * Displays closing hours for a restaurant in each recyclerview item.
     * @param holder : Holder containing the item view
     * @param position : Restaurant at the indice "position" in list
     */
    private void displayClosingHours(@NonNull ViewHolderListView holder, int position) {
        boolean colorText = false;
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);

        // Get Opening/Closing hours associated to the current day
        ArrayList<String> closingHours = getOpeningAndClosingHoursForADay(position, ScheduleType.CLOSE, currentDay);
        ArrayList<String> openingHours = getOpeningAndClosingHoursForADay(position, ScheduleType.OPEN, currentDay);

        try {
            // Check number of opening and closing hours for a day
            if (closingHours.size() == 1) { // Restaurant closed once
                int closingHour = Integer.parseInt(closingHours.get(0).substring(0,2));
                int closingMinutes = Integer.parseInt(closingHours.get(0).substring(2,4));
                int openingHour = Integer.parseInt(openingHours.get(0).substring(0,2));
                int openingMinutes = Integer.parseInt(openingHours.get(0).substring(2,4));
                // Update text
                if (CustomComparators.getTimeDiff(currentHour, currentMinutes, closingHour, closingMinutes) >= 0) { // CLOSED
                    holder.binding.hour.setText(context.getResources().getString(R.string.closed));
                }
                else {
                    if (CustomComparators.getTimeDiff(currentHour, currentMinutes, closingHour, closingMinutes) > -60) { // CLOSED SOON
                        holder.binding.hour.setText(context.getResources().getString(R.string.closing_soon));
                        colorText = true;
                    }
                    else {
                        if (CustomComparators.getTimeDiff(currentHour, currentMinutes, openingHour, openingMinutes) > 0) { // OPEN UNTIL
                            String text = context.getResources().getString(R.string.open_until,
                                                                 closingHours.get(0).substring(0,2),
                                                                 closingHours.get(0).substring(2,4));
                            holder.binding.hour.setText(text);
                        }
                        else { // CLOSED (NOT OPENED YET)
                            holder.binding.hour.setText(context.getResources().getString(R.string.closed));
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
                    holder.binding.hour.setText(context.getResources().getString(R.string.closed));
                }
                else if (CustomComparators.getTimeDiff(currentHour, currentMinutes, firstOpeningHour, firstOpeningMinutes) >= 0 &&
                        CustomComparators.getTimeDiff(currentHour, currentMinutes, firstClosingHour, firstClosingMinutes) < 0) {
                    if (CustomComparators.getTimeDiff(currentHour, currentMinutes, firstClosingHour, firstClosingMinutes) > -60) { // CLOSING SOON
                        holder.binding.hour.setText(context.getResources().getString(R.string.closing_soon));
                        colorText = true;
                    }
                    else { // OPEN UNTIL
                        String text = context.getResources().getString(R.string.open_until,
                                closingHours.get(0).substring(0,2),
                                closingHours.get(0).substring(2,4));
                        holder.binding.hour.setText(text);
                    }
                }
                else if (CustomComparators.getTimeDiff(currentHour, currentMinutes, firstClosingHour, firstClosingMinutes) >= 0 &&
                        CustomComparators.getTimeDiff(currentHour, currentMinutes, secondOpeningHour, secondOpeningMinutes) < 0) {  // CLOSED (NOT OPENING YET)
                    holder.binding.hour.setText(context.getResources().getString(R.string.closed));
                }
                else if (CustomComparators.getTimeDiff(currentHour, currentMinutes, secondOpeningHour, secondOpeningMinutes) >= 0 &&
                        CustomComparators.getTimeDiff(currentHour, currentMinutes, secondClosingHour, secondClosingMinutes) < 0) {
                    if (CustomComparators.getTimeDiff(currentHour, currentMinutes, secondClosingHour, secondClosingHour) > -60) { // CLOSING SOON
                        holder.binding.hour.setText(context.getResources().getString(R.string.closing_soon));
                        colorText = true;
                    }
                    else { // OPEN UNTIL
                         String text = context.getResources().getString(R.string.open_until,
                                closingHours.get(1).substring(0,2),
                                closingHours.get(1).substring(2,4));
                        holder.binding.hour.setText(text);
                    }
                }
                else { // CLOSED
                    holder.binding.hour.setText(context.getResources().getString(R.string.closed));
                }
            }
            // Update color text for "closing soon" hours
            if (colorText) displayStyleTextViewForHoursDisplay(holder.binding.hour, R.color.red, Typeface.BOLD_ITALIC);
            else displayStyleTextViewForHoursDisplay(holder.binding.hour, R.color.grey_50, Typeface.ITALIC);
        }
        catch (IndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Retrieves OpeningAndClosingHours object for a Restaurant in the list.
     * @param position : Position in the list of restaurants
     * @param type : Type of hours to return (Closing or opening)
     * @param currentDay : Day of the week
     * @return : OpeningAndClosingHours
     */
    private ArrayList<String> getOpeningAndClosingHoursForADay(int position, ScheduleType type, int currentDay) {
        OpeningAndClosingHours openingAndClosingHours = listRestaurant.get(position)
                                                                      .getOpeningAndClosingHours();
        return openingAndClosingHours.getHours(type, currentDay);
    }

    /**
     * Updates the "closing hour" text with a color and a style.
     * @param text : "closing hour" text for a recyclerview item
     * @param color : Color to apply
     * @param typeface : Style to apply
     */
    private void displayStyleTextViewForHoursDisplay(TextView text, @ColorRes int color, int typeface) {
        text.setTextColor(context.getResources().getColor(color));
        text.setTypeface(null, typeface);
    }

    /**
     * Displays the associated photo of a restaurant.
     * @param holder : Holder containing the item view
     * @param position : Restaurant at the indice "position" in list
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void displayRestaurantPhoto(@NonNull ViewHolderListView holder, int position) {
        if (listRestaurant.get(position).getPhotoReference() != null) {
            Glide.with(context)
                    .load("https://maps.googleapis.com/maps/api/place/photo?&maxwidth=400&maxheight=400&photo_reference="
                            + listRestaurant.get(position).getPhotoReference() + "&key=" + BuildConfig.API_KEY)
                             .centerCrop()
                    .override(holder.binding.photoRestaurant.getWidth(), holder.binding.photoRestaurant.getHeight())
                    .into(holder.binding.photoRestaurant);
        }
        else {
            holder.binding.photoRestaurant.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.ic_baseline_image_not_supported_24dp_oyster_white));
        }
    }

    /**
     * Displays the number of workmates going to a selected restaurant.
     * @param holder : Holder containing the item view
     * @param position : Restaurant at the indice "position" in list
     */
    private void displayNumberOfWorkmates(@NonNull RecyclerView.ViewHolder holder, int position) {
        String restaurantId = listRestaurant.get(position).getPlaceId();
        int nbWorkmates = 0;
        for (int i = 0; i < listWorkmates.size(); i++) {
            if (listWorkmates.get(i).getRestaurantSelectedID().equals(restaurantId))
                nbWorkmates++;
        }
        String text = context.getResources().getString(R.string.nb_workmates, nbWorkmates);
        ((ViewHolderListView) holder).binding.nbWorkmates.setText(text);
    }

    /**
     * ViewHolder class to display a Restaurant item using ListViewAdapter.
     */
    private static class ViewHolderListView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final List<ImageView> rating;
        private final OnItemRestaurantClickListener onItemRestaurantClickListener;
        private final ListViewItemBinding binding;

        @SuppressLint("ClickableViewAccessibility")
        ViewHolderListView(ListViewItemBinding binding, OnItemRestaurantClickListener onItemRestaurantClickListener) {
            super(binding.getRoot());
            this.onItemRestaurantClickListener = onItemRestaurantClickListener;

            this.binding = binding;
            binding.rootLayoutItem.setOnClickListener(this);
            rating = Arrays.asList(binding.noteStar1,
                                   binding.noteStar2,
                                   binding.noteStar3,
                                   binding.noteStar4,
                                   binding.noteStar5);

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

        private final ListViewFooterItemBinding binding;

        ViewHolderFooterListView(ListViewFooterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Interface to handle click on RecyclerView items
     */
    public interface OnItemRestaurantClickListener {
        void onItemRestaurantClick(int position);
    }

    /**
     * Returns the type of ViewHolder to create according to the position in the RecyclerView.
     * @param position : Position in the RecyclerView
     * @return : Type of ViewHolder
     */
    @Override
    public int getItemViewType(int position) {
        if (isPositionItem(position)) return VIEW_ITEM;
        else return VIEW_FOOTER;
    }

    /**
     * Returns a boolean value according to the position in the RecyclerView
     * @param position : Position in the RecyclerView
     * @return : Boolean value
     */
    private boolean isPositionItem(int position) {
        if (getItemCount() == 1) return true;
        else return position < getItemCount()-1; // last position
    }

    /**
     * Updates visibility of the progress bar.
     * @param visibility : Visibility to apply
     */
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
