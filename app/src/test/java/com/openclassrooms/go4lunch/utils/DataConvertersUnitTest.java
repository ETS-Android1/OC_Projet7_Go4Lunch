package com.openclassrooms.go4lunch.utils;

import com.openclassrooms.go4lunch.database.HoursData;
import com.openclassrooms.go4lunch.database.HoursDataUnitTest;
import com.openclassrooms.go4lunch.model.OpeningAndClosingHours;
import com.openclassrooms.go4lunch.service.places.response.details.ClosingHours;
import com.openclassrooms.go4lunch.service.places.response.details.OpeningHours;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * File providing tests to cover @{@link DataConverters} class file.
 */
@RunWith(JUnit4.class)
public class DataConvertersUnitTest {

    /**
     * TEST #1 : Checks if the static method converterHoursDataToOpeningAndClosingHours() of the @{@link DataConverters}
     * class correctly convert a list of @{@link HoursDataUnitTest} into an @{@link OpeningAndClosingHours} object.
     */
    @Test
    public void test_data_converters_conversion() {
        List<HoursData> listHoursData = new ArrayList<>();

        // Add Opening and Closing hours for each of the week, for a restaurant defined with its ID.
        // Each HoursData of this list represents a row of data in the "hours_table" from database.
        listHoursData.add(new HoursData(new ClosingHours(0, "1300"), new OpeningHours(0, "0900"), "ChIJGz20sdh65kcRCfY0bMPzkVo"));
        listHoursData.add(new HoursData(new ClosingHours(1, "2100"), new OpeningHours(1, "0900"), "ChIJGz20sdh65kcRCfY0bMPzkVo"));
        listHoursData.add(new HoursData(new ClosingHours(2, "2200"), new OpeningHours(2, "0930"), "ChIJGz20sdh65kcRCfY0bMPzkVo"));
        listHoursData.add(new HoursData(new ClosingHours(3, "2200"), new OpeningHours(3, "0930"), "ChIJGz20sdh65kcRCfY0bMPzkVo"));
        listHoursData.add(new HoursData(new ClosingHours(4, "2100"), new OpeningHours(4, "0900"), "ChIJGz20sdh65kcRCfY0bMPzkVo"));
        listHoursData.add(new HoursData(new ClosingHours(5, "2100"), new OpeningHours(5, "0900"), "ChIJGz20sdh65kcRCfY0bMPzkVo"));
        listHoursData.add(new HoursData(new ClosingHours(6, "2300"), new OpeningHours(6, "1000"), "ChIJGz20sdh65kcRCfY0bMPzkVo"));

        // After being extracted from database, all the rows corresponding to a specific restaurant are
        // converted into an OpeningAndClosingHours, before being affected to a Restaurant object
        OpeningAndClosingHours openingAndClosingHours = DataConverters.converterHoursDataToOpeningAndClosingHours(listHoursData);

        // Check if conversion has been correctly done
        // Check non-nullability
        assertNotNull(openingAndClosingHours);
        // Check size of each attribute (Closing hours arraylists and Opening hours arraylists, both containing String values)
        assertEquals(1, openingAndClosingHours.getMondayOpeningHours().size());
        assertEquals(1, openingAndClosingHours.getTuesdayOpeningHours().size());
        assertEquals(1, openingAndClosingHours.getWednesdayOpeningHours().size());
        assertEquals(1, openingAndClosingHours.getThursdayOpeningHours().size());
        assertEquals(1, openingAndClosingHours.getFridayOpeningHours().size());
        assertEquals(1, openingAndClosingHours.getSaturdayOpeningHours().size());
        assertEquals(1, openingAndClosingHours.getSundayOpeningHours().size());

        assertEquals(1, openingAndClosingHours.getMondayClosingHours().size());
        assertEquals(1, openingAndClosingHours.getTuesdayClosingHours().size());
        assertEquals(1, openingAndClosingHours.getWednesdayClosingHours().size());
        assertEquals(1, openingAndClosingHours.getThursdayClosingHours().size());
        assertEquals(1, openingAndClosingHours.getFridayClosingHours().size());
        assertEquals(1, openingAndClosingHours.getSaturdayClosingHours().size());
        assertEquals(1, openingAndClosingHours.getSundayClosingHours().size());

        // Check String values for each arraylist
        assertEquals("0900", openingAndClosingHours.getMondayOpeningHours().get(0));
        assertEquals("0930", openingAndClosingHours.getTuesdayOpeningHours().get(0));
        assertEquals("0930", openingAndClosingHours.getWednesdayOpeningHours().get(0));
        assertEquals("0900", openingAndClosingHours.getThursdayOpeningHours().get(0));
        assertEquals("0900", openingAndClosingHours.getFridayOpeningHours().get(0));
        assertEquals("1000", openingAndClosingHours.getSaturdayOpeningHours().get(0));
        assertEquals("0900", openingAndClosingHours.getSundayOpeningHours().get(0));

        assertEquals("2100", openingAndClosingHours.getMondayClosingHours().get(0));
        assertEquals("2200", openingAndClosingHours.getTuesdayClosingHours().get(0));
        assertEquals("2200", openingAndClosingHours.getWednesdayClosingHours().get(0));
        assertEquals("2100", openingAndClosingHours.getThursdayClosingHours().get(0));
        assertEquals("2100", openingAndClosingHours.getFridayClosingHours().get(0));
        assertEquals("2300", openingAndClosingHours.getSaturdayClosingHours().get(0));
        assertEquals("1300", openingAndClosingHours.getSundayClosingHours().get(0));
    }
}
