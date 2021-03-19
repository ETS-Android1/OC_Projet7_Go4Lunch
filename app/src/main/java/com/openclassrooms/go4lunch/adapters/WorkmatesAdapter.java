package com.openclassrooms.go4lunch.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.model.Workmate;
import com.openclassrooms.go4lunch.utils.CustomComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter class to display all employees from Firestore database,
 * using a @{@link androidx.recyclerview.widget.RecyclerView.ViewHolder} class
 */
public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.ViewHolderWorkmates>{

    private final ArrayList<Workmate> listWorkmates = new ArrayList<>();
    private final Context context;
    private final boolean typeOfList;

    public WorkmatesAdapter(Context context, boolean typeOfList) {
        this.context = context;
        this.typeOfList = typeOfList;
    }

    @NonNull
    @Override
    public ViewHolderWorkmates onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workmates_item, parent, false);
        return new ViewHolderWorkmates(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderWorkmates holder, int position) {
        if (typeOfList) {
            // Text
            String text = "";
            if (listWorkmates.get(position).getRestaurantName() != null) {
                if (listWorkmates.get(position).getRestaurantName().length() == 0) {
                    text = listWorkmates.get(position).getName() + " " + context.getResources().getString(R.string.no_decision);
                    holder.text.setText(text);
                    displayStyleTextView(holder.text, R.color.light_grey, Typeface.ITALIC);
                }
                else {
                    text = listWorkmates.get(position).getName() + " " + context.getResources().getString(R.string.is_eating_at) + " "
                            + listWorkmates.get(position).getRestaurantName();
                    holder.text.setText(text);
                    displayStyleTextView(holder.text, R.color.black, Typeface.NORMAL);
                }
            }
            // Photo
            if (listWorkmates.get(position).getPhotoUrl() != null) {
                if (listWorkmates.get(position).getPhotoUrl().length() != 0) {
                    String url = listWorkmates.get(position).getPhotoUrl();
                    Glide.with(context)
                            .load(url)
                            .centerCrop()
                            .override(holder.photo.getWidth(), holder.photo.getHeight())
                            .into(holder.photo);
                }
            }
        }
        else {
            // Text
            String text = listWorkmates.get(position).getName() + " is going";
            holder.text.setText(text);

            // Photo
            if (listWorkmates.get(position).getPhotoUrl() != null) {
                if (listWorkmates.get(position).getPhotoUrl().length() != 0) {
                    String url = listWorkmates.get(position).getPhotoUrl();
                    Glide.with(context)
                            .load(url)
                            .centerCrop()
                            .override(holder.photo.getWidth(), holder.photo.getHeight())
                            .into(holder.photo);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return listWorkmates.size();
    }

    /**
     * ViewHolder class to display employee information using WorkmatesAdapter.
     */
    static class ViewHolderWorkmates extends RecyclerView.ViewHolder {

        private final TextView text;
        private final ImageView photo;

        ViewHolderWorkmates(View view) {
            super(view);
            text = view.findViewById(R.id.text_workmate);
            photo = view.findViewById(R.id.photo_workmate);
        }
    }

    public void updateList(List<Workmate> newList) {
        Collections.sort(newList, new CustomComparator.WorkmateAZComparator());
        listWorkmates.clear();
        listWorkmates.addAll(newList);
        notifyDataSetChanged();
    }

    private void displayStyleTextView(TextView text, @ColorRes int color, int typeface) {
        text.setTextColor(context.getResources().getColor(color));
        text.setTypeface(null, typeface);
    }
}
