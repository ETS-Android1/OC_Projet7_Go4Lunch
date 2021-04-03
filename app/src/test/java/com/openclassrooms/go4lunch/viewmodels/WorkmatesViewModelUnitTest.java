package com.openclassrooms.go4lunch.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import com.openclassrooms.go4lunch.model.Workmate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * File providing tests to cover @{@link WorkmatesViewModel} class file.
 */
@RunWith(JUnit4.class)
public class WorkmatesViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private WorkmatesViewModel workmatesViewModel;

    private List<Workmate> listWormates;
    String NAME = "Nicolas MOGES";
    String EMAIL = "nicolasmoges@gmail.com";
    String RESTAURANT_SELECTED_ID = "ChIJXYZoq9h65kcRuWrPmnFCQUI";
    String PHOTO_URL = "https://graph.facebook.com/10215112752571861/picture";
    String RESTAURANT_SELECTED_NAME = "GEMINI Boulogne";
    List<String> LIKED_RESTAURANTS = Arrays.asList("ChIJGz20sdh65kcRCfY0bMPzkVo",
                                                   "ChIJA_5Kq9h65kcRvIzH3sGqB5Q",
                                                   "ChIJXYZoq9h65kcRuWrPmnFCQUI");

    @Before
    public void setup() {
        workmatesViewModel = new WorkmatesViewModel();

        // Initialize Workmate object
        Workmate workmate = new Workmate(NAME,
                EMAIL,
                RESTAURANT_SELECTED_ID,
                PHOTO_URL,
                RESTAURANT_SELECTED_NAME);
        workmate.setLiked(LIKED_RESTAURANTS);

        // Initialize List of Workmates
        listWormates = new ArrayList<>();
        listWormates.add(workmate);
    }

    /**
     * TEST #1 : Check if sending a new value to the MutableLiveData will correctly notify the attached
     * observer, and update the list of workmates.
     */
    @Test
    public void test_check_if_list_of_workmates_mutable_live_data_is_correctly_updated() {
        // Create observer
        Observer<List<Workmate>> observer = new Observer<List<Workmate>>() {
            @Override
            public void onChanged(List<Workmate> newListWorkmates) {
                assertNotNull(newListWorkmates);
                assertEquals(1, newListWorkmates.size());
                assertEquals(NAME, newListWorkmates.get(0).getName());
                assertEquals(EMAIL, newListWorkmates.get(0).getEmail());
                assertEquals(RESTAURANT_SELECTED_ID,
                             newListWorkmates.get(0).getRestaurantSelectedID());
                assertEquals(RESTAURANT_SELECTED_NAME, newListWorkmates.get(0).getRestaurantName());
                assertEquals(PHOTO_URL, newListWorkmates.get(0).getPhotoUrl());
                for (int i = 0; i < newListWorkmates.get(0).getLiked().size(); i++) {
                    switch (i) {
                        case 0:
                            assertEquals("ChIJGz20sdh65kcRCfY0bMPzkVo",
                                         newListWorkmates.get(0).getLiked().get(0));
                            break;
                        case 1 :
                            assertEquals("ChIJA_5Kq9h65kcRvIzH3sGqB5Q",
                                         newListWorkmates.get(0).getLiked().get(1));
                            break;
                        case 2 :
                            assertEquals("ChIJXYZoq9h65kcRuWrPmnFCQUI",
                                         newListWorkmates.get(0).getLiked().get(2));
                             break;
                    }
                }
                workmatesViewModel.getListWorkmates().removeObserver(this);
            }
        };

        // Add Observer to Mutable LiveData
        workmatesViewModel.getListWorkmates().observeForever(observer);

        // Send new value to MutableLiveData
        workmatesViewModel.getListWorkmates().setValue(listWormates);
    }
}
