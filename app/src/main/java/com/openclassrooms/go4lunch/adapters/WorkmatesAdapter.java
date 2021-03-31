package com.openclassrooms.go4lunch.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.databinding.WorkmatesItemBinding;
import com.openclassrooms.go4lunch.model.Workmate;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class to display all employees from Firestore database,
 * using a @{@link androidx.recyclerview.widget.RecyclerView.ViewHolder} class
 */
public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.ViewHolderWorkmates>{

    // List of workmates to display
    private final ArrayList<Workmate> listWorkmates = new ArrayList<>();
    // Context parameter
    private final Context context;
    // Defines the type of list to display using workmates information
    // true : List to display in WorkmatesFragment
    // false : List to display in RestaurantDetailsFragment
    private final boolean typeOfList;

    public WorkmatesAdapter(Context context, boolean typeOfList) {
        this.context = context;
        this.typeOfList = typeOfList;
    }

    @NonNull
    @Override
    public ViewHolderWorkmates onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkmatesItemBinding binding = WorkmatesItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolderWorkmates(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderWorkmates holder, int position) {
        // Display text
        displayText(holder.binding.textWorkmate, position);

        // Display icon
        Glide.with(context).clear(holder.binding.photoWorkmate); // Cancel any pending loads
        loadUserIcon(holder.binding.photoWorkmate, listWorkmates.get(position).getPhotoUrl());
    }

    @Override
    public int getItemCount() {
        return listWorkmates.size();
    }

    /**
     * Displays the text associated to a workmates, and corresponding to its restaurant selection.
     * @param textView : View in which the text must be displayed
     * @param position : Position in the RecyclerView
     */
    private void displayText(TextView textView, int position) {
        if (typeOfList) {
            // Text
            String text;
            if (listWorkmates.get(position).getRestaurantName() != null) {
                if (listWorkmates.get(position).getRestaurantName().length() == 0) {
                    text = context.getResources().getString(R.string.no_decision,
                                                            listWorkmates.get(position).getName());
                    textView.setText(text);
                    displayStyleTextView(textView, R.color.light_grey, Typeface.ITALIC);
                }
                else {
                    text = context.getResources().getString(R.string.is_eating_at,
                                                            listWorkmates.get(position).getName(),
                            listWorkmates.get(position).getRestaurantName());
                    textView.setText(text);
                    displayStyleTextView(textView, R.color.black, Typeface.NORMAL);
                }
            }
        }
        else {
            // Text
            String text = context.getResources().getString(R.string.is_going,
                                                           listWorkmates.get(position).getName());
            textView.setText(text);
        }
    }

    /**
     * Loads workmate photo if available, otherwise displays a default drawable.
     * @param icon : ImageView in which icon must be displayed
     * @param url : Url to load photo
     */
    private void loadUserIcon(ImageView icon, String url) {
        // Load photo from url
        if (url != null) {
            if (!url.isEmpty()) {
                Glide.with(context)
                        .load(url)
                        .circleCrop()
                        .override(icon.getWidth(), icon.getHeight())
                        .into(icon);
            }
            else icon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                   R.drawable.ic_baseline_account_circle_24dp_dark_orange,
                                                                            null));

        }
        // Otherwise display default drawable
        else icon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                               R.drawable.ic_baseline_account_circle_24dp_dark_orange,
                                                                        null));
    }


    /**
     * ViewHolder class to display employee information using WorkmatesAdapter.
     */
    static class ViewHolderWorkmates extends RecyclerView.ViewHolder {

        private final WorkmatesItemBinding binding;

        ViewHolderWorkmates(WorkmatesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Updates list of workmates when a new one is available.
     * @param newList : New list of workmates to display
     */
    public void updateList(List<Workmate> newList) {
        listWorkmates.clear();
        listWorkmates.addAll(newList);
        notifyDataSetChanged();
    }

    /**
     * Updates the appearance of the text using a color resource and a specified style.
     * @param text : TextView to update
     * @param color : Color to apply
     * @param typeface : Text style to apply
     */
    private void displayStyleTextView(TextView text, @ColorRes int color, int typeface) {
        text.setTextColor(context.getResources().getColor(color));
        text.setTypeface(null, typeface);
    }
}
