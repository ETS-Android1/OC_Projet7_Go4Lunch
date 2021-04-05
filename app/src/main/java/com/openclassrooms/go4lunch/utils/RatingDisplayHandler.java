package com.openclassrooms.go4lunch.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import com.openclassrooms.go4lunch.R;

/**
 * Class used to handle display of rating information in fragment views
 */
public class RatingDisplayHandler {

    /**
     *
     * @param star1 : ImageView representing a part of the rating information
     * @param star2 : ImageView representing a part of the rating information
     * @param star3 : ImageView representing a part of the rating information
     * @param star4 : ImageView representing a part of the rating information
     * @param star5 : ImageView representing a part of the rating information
     * @param rating : Rating information
     * @param context : Context
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public static void displayRating(ImageView star1, ImageView star2,
                                     ImageView star3, ImageView star4,
                                     ImageView star5, double rating,
                                     Context context) {

        if (rating >= 0 && rating < 0.5) { // 0 STARS
            star5.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            // DO NOTHING
        }
        else if (rating >= 0.5 && rating < 1) // 0.5 STARS
        {
            star5.setImageDrawable(context.getResources().getDrawable(
                                                     R.drawable.ic_baseline_star_half_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
        }
        else if (rating >= 1 && rating < 1.5) // 1 STAR
        {
            star5.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
        }
        else if (rating >= 1.5 && rating < 2) { // 1.5 STARS
            star5.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                     R.drawable.ic_baseline_star_half_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
        }
        else if (rating >= 2 && rating < 2.5) { // 2 STARS
            star5.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
        }
        else if (rating >= 2.5 && rating < 3) { // 2.5 STARS
            star5.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                     R.drawable.ic_baseline_star_half_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
        }
        else if (rating >= 3 && rating < 3.5) { // 3 STARS
            star5.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
        }
        else if (rating >= 3.5 && rating < 4) { // 3.5 STARS
            star5.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                     R.drawable.ic_baseline_star_half_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
        }
        else if (rating >= 4 && rating < 4.5) { // 4 STARS
            star5.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                   R.drawable.ic_baseline_star_border_24dp_yellow));
        }
        else if (rating >= 4.5 && rating < 5) { // 4.5 STARS
            star5.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                     R.drawable.ic_baseline_star_half_24dp_yellow));
        }
        else { // 5 STARS
            star5.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star4.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star3.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star2.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
            star1.setImageDrawable(context.getResources().getDrawable(
                                                          R.drawable.ic_baseline_star_24dp_yellow));
        }
    }
}
