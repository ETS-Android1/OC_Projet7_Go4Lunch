package com.openclassrooms.go4lunch.service;

import com.openclassrooms.go4lunch.service.places.response.details.ClosingHours;
import com.openclassrooms.go4lunch.service.places.response.details.HoursInfo;
import com.openclassrooms.go4lunch.service.places.response.details.OpeningHours;
import com.openclassrooms.go4lunch.service.places.response.details.Period;
import com.openclassrooms.go4lunch.service.places.response.details.ResultDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * File providing tests to cover the following classes :
 * - @{@link ClosingHours}
 * - @{@link HoursInfo}
 * - @{@link OpeningHours}
 * - @{@link Period}
 * - @{@link ResultDetails}
 */
@RunWith(JUnit4.class)
public class DetailsResponseUnitTest {

    /**
     * TEST #1 : Checks if a @{@link ClosingHours} object is correctly instantiated.
     */
    @Test
    public void test_closing_hours_object_creation() {
        // Initialize object
        ClosingHours closingHours = new ClosingHours(1, "2300");
        // Check data
        assertEquals(1, closingHours.getDay());
        assertEquals("2300", closingHours.getTime());
    }

    /**
     * TEST #2 : Checks if a @{@link OpeningHours} object is correctly instantiated.
     */
    @Test
    public void test_opening_hours_object_creation() {
        // Initialize object
        OpeningHours openingHours = new OpeningHours(2, "1200");
        // Check data
        assertEquals(2, openingHours.getDay());
        assertEquals("1200", openingHours.getTime());
    }

    /**
     * TEST #3 : Checks if a @{@link HoursInfo} object is correctly instantiated.
     */
    @Test
    public void test_hours_info_object_creation() {
        // Describe behavior
        Period period = Mockito.mock(Period.class);
        ClosingHours closingHours = Mockito.mock(ClosingHours.class);
        OpeningHours openingHours = Mockito.mock(OpeningHours.class);
        Mockito.when(period.getClose()).thenReturn(closingHours);
        Mockito.when(period.getOpen()).thenReturn(openingHours);
        Mockito.when(closingHours.getTime()).thenReturn("2300");
        Mockito.when(closingHours.getDay()).thenReturn(2);
        Mockito.when(openingHours.getTime()).thenReturn("1930");
        Mockito.when(openingHours.getDay()).thenReturn(2);

        // Initialize object
        List<Period> listPeriods = Collections.singletonList(period);
        HoursInfo hoursInfo = new HoursInfo(listPeriods);

        // Check data
        assertEquals(1, hoursInfo.getPeriods().size());
        assertEquals(2, hoursInfo.getPeriods().get(0).getClose().getDay());
        assertEquals("2300", hoursInfo.getPeriods().get(0).getClose().getTime());
        assertEquals(2, hoursInfo.getPeriods().get(0).getOpen().getDay());
        assertEquals("1930", hoursInfo.getPeriods().get(0).getOpen().getTime());
    }

    /**
     * TEST #4 : Checks if a @{@link Period} object is correctly instantiated.
     */
    @Test
    public void test_period_object_creation() {
        // Describe behavior
        ClosingHours closingHours = Mockito.mock(ClosingHours.class);
        OpeningHours openingHours = Mockito.mock(OpeningHours.class);
        Mockito.when(closingHours.getTime()).thenReturn("1400");
        Mockito.when(closingHours.getDay()).thenReturn(3);
        Mockito.when(openingHours.getTime()).thenReturn("1200");
        Mockito.when(openingHours.getDay()).thenReturn(3);

        // Initialize object
        Period period = new Period(closingHours, openingHours);

        // Check data
        assertEquals("1400", period.getClose().getTime());
        assertEquals(3, period.getClose().getDay());
        assertEquals("1200", period.getOpen().getTime());
        assertEquals(3, period.getOpen().getDay());
    }

    /**
     * TEST #5 : Checks if a @{@link ResultDetails} object is correctly instantiated.
     */
    @Test
    public void test_result_details_object_creation() {
        String phone = "01 46 03 93 70";
        String website = "https://www.franprix.fr/magasins/5575";

        // Describe behavior
        ClosingHours closingHours = Mockito.mock(ClosingHours.class);
        OpeningHours openingHours = Mockito.mock(OpeningHours.class);
        HoursInfo hoursInfo = Mockito.mock(HoursInfo.class);
        Period period = Mockito.mock(Period.class);
        List<Period> listPeriods = Collections.singletonList(period);
        Mockito.when(hoursInfo.getPeriods()).thenReturn(listPeriods);
        Mockito.when(period.getOpen()).thenReturn(openingHours);
        Mockito.when(period.getClose()).thenReturn(closingHours);
        Mockito.when(openingHours.getDay()).thenReturn(1);
        Mockito.when(closingHours.getDay()).thenReturn(1);
        Mockito.when(openingHours.getTime()).thenReturn("1200");
        Mockito.when(closingHours.getTime()).thenReturn("1430");

        // Initialize object
        ResultDetails resultDetails = new ResultDetails(phone, hoursInfo, website);

        // Check data
        assertEquals(phone, resultDetails.getFormattedPhoneNumber());
        assertEquals(website, resultDetails.getWebsite());
        assertEquals(1, resultDetails.getOpeningHours().getPeriods().size());
        assertEquals(1, resultDetails.getOpeningHours().getPeriods().get(0)
                                                                      .getOpen().getDay());
        assertEquals(1, resultDetails.getOpeningHours().getPeriods().get(0)
                                                                      .getClose().getDay());
        assertEquals("1200", resultDetails.getOpeningHours().getPeriods().get(0)
                                                                       .getOpen().getTime());
        assertEquals("1430", resultDetails.getOpeningHours().getPeriods().get(0)
                                                                       .getClose().getTime());
    }
}
