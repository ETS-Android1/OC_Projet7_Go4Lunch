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
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class to display all employees from Firestore database,
 * using a @{@link androidx.recyclerview.widget.RecyclerView.ViewHolder} class
 */
public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.ViewHolderWorkmates>{

    private final ArrayList<Workmate> listWorkmates = new ArrayList<>();
    private final Context context;

    public WorkmatesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderWorkmates onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workmates_item, parent, false);
        return new ViewHolderWorkmates(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderWorkmates holder, int position) {
        // Name
        String text = "";
        if (listWorkmates.get(position).getRestaurantName() != null) {
            if (listWorkmates.get(position).getRestaurantName().length() == 0) {
                text = listWorkmates.get(position).getName() + " " + context.getResources().getString(R.string.no_decision);
                holder.name.setText(text);
                displayStyleTextView(holder.name, R.color.light_grey, Typeface.ITALIC);
            }
            else {
                text = listWorkmates.get(position).getName() + " " + context.getResources().getString(R.string.is_eating_at) + " "
                        + listWorkmates.get(position).getRestaurantName();
                holder.name.setText(text);
                displayStyleTextView(holder.name, R.color.black, Typeface.NORMAL);
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

    @Override
    public int getItemCount() {
        return listWorkmates.size();
    }

    /**
     * ViewHolder class to display employee information using WorkmatesAdapter.
     */
    static class ViewHolderWorkmates extends RecyclerView.ViewHolder {

        private final TextView name;
        private final ImageView photo;

        ViewHolderWorkmates(View view) {
            super(view);
            name = view.findViewById(R.id.name_workmate);
            photo = view.findViewById(R.id.photo_workmate);
        }
    }

    public void updateList(List<Workmate> newList) {
        listWorkmates.clear();
        listWorkmates.addAll(newList);
        notifyDataSetChanged();
    }

    private void displayStyleTextView(TextView text, @ColorRes int color, int typeface) {
        text.setTextColor(context.getResources().getColor(color));
        text.setTypeface(null, typeface);
    }
}
