package com.openclassrooms.go4lunch.database;

import com.openclassrooms.go4lunch.model.Restaurant;
import com.openclassrooms.go4lunch.service.places.response.details.ClosingHours;
import com.openclassrooms.go4lunch.service.places.response.details.OpeningHours;

public class FakeDataTest {

    // FAKE DATA RESTAURANT 1
    public final static String RESTAURANT_1_PLACE_ID = "ChIJGz20sdh65kcRCfY0bMPzkVo";
    public final static String RESTAURANT_1_NAME = "Franprix";
    public final static String RESTAURANT_1_ADDRESS = "80 Avenue Jean Baptiste Clement, Boulogne-Billancourt";
    public final static double RESTAURANT_1_LATITUDE = 48.8437898;
    public final static double RESTAURANT_1_LONGITUDE = 2.2320991;
    public final static double RESTAURANT_1_RATING = 3.4;
    public final static String RESTAURANT_1_NUMBER = "01 46 03 93 70";
    public final static String RESTAURANT_1_WEBSITE_URI = "https://www.franprix.fr/magasins/5575";
    public final static String RESTAURANT_1_PHOTO_REFERENCE = "ATtYBwIJO4zRghygCxwMS7VGe2o-U9VPD_" +
                                           "tOktAYaf6LJ3Ay1YQHaFHFmfTAiF9mSzHL" +
                                           "k7PKJyf6Ky-kRvgCmTDwj_9aURPdmLLZrK-" +
                                           "DAjr1zieyxLKaf1A-lboLIBcY4SornCkG2yP" +
                                           "shwrwlFmxzpcvxjv7FoC58N4W-aqaufY6ir8s9tEv";
    public final static int RESTAURANT_1_PHOTO_HEIGHT = 4000;
    public final static int RESTAURANT_1_PHOTO_WIDTH = 4000;

    // FAKE DATA OPENING HOURS FOR RESTAURANT 1
    public final static OpeningHours MONDAY_OPENING_HOURS_RESTAURANT_1 = new OpeningHours(1, "0900");
    public final static OpeningHours TUESDAY_OPENING_HOURS_RESTAURANT_1 = new OpeningHours(1, "0900");
    public final static OpeningHours WEDNESDAY_OPENING_HOURS_RESTAURANT_1 = new OpeningHours(1, "0900");
    public final static OpeningHours THURSDAY_OPENING_HOURS_RESTAURANT_1 = new OpeningHours(1, "0900");
    public final static OpeningHours FRIDAY_OPENING_HOURS_RESTAURANT_1 = new OpeningHours(1, "0900");
    public final static OpeningHours SATURDAY_OPENING_HOURS_RESTAURANT_1 = new OpeningHours(1, "0100");
    public final static OpeningHours SUNDAY_OPENING_HOURS_RESTAURANT_1 = new OpeningHours(1, "0830");

    // FAKE DATA CLOSING HOURS FOR RESTAURANT 1
    public final static ClosingHours MONDAY_CLOSING_HOURS_RESTAURANT_1 = new ClosingHours(1, "2100");
    public final static ClosingHours TUESDAY_CLOSING_HOURS_RESTAURANT_1 = new ClosingHours(1, "2100");
    public final static ClosingHours WEDNESDAY_CLOSING_HOURS_RESTAURANT_1 = new ClosingHours(1, "2100");
    public final static ClosingHours THURSDAY_CLOSING_HOURS_RESTAURANT_1 = new ClosingHours(1, "2100");
    public final static ClosingHours FRIDAY_CLOSING_HOURS_RESTAURANT_1 = new ClosingHours(1, "2100");
    public final static ClosingHours SATURDAY_CLOSING_HOURS_RESTAURANT_1 = new ClosingHours(1, "2300");
    public final static ClosingHours SUNDAY_CLOSING_HOURS_RESTAURANT_1 = new ClosingHours(1, "1300");

    // FAKE DATA RESTAURANT 2
    public final static String RESTAURANT_2_PLACE_ID = "ChIJA_5Kq9h65kcRvIzH3sGqB5Q";
    public final static String RESTAURANT_2_NAME = "iThaï";
    public final static String RESTAURANT_2_ADDRESS = "88 Avenue Jean Baptiste Clement, Boulogne-Billancourt";
    public final static double RESTAURANT_2_LATITUDE = 48.84330809999999;
    public final static double RESTAURANT_2_LONGITUDE = 2.2315919;
    public final static double RESTAURANT_2_RATING = 3.9;
    public final static String RESTAURANT_2_NUMBER = "0141109112";
    public final static String RESTAURANT_2_WEBSITE_URI = "https//ithai.fr/fr";
    public final static String RESTAURANT_2_PHOTO_REFERENCE = "ATtYBwIfVw6sZ_bOeFhcYNffkjG7hPjbGi58Bkyt0" +
                                                        "vFlyppfSt9cyg5wIb7AdcojcHzwjgJb5e0eKy-wk8" +
                                                        "goCZoEg9FzdjP9LXXBiiCAfjTx7h4phHBa6ZIOzQy" +
                                                        "5VI0tiNQElljWSSefzb2LAQF6uGngsweyWKDcTI7d" +
                                                        "UCZCibC9siqqtj7I";
    public final static int RESTAURANT_2_PHOTO_HEIGHT = 4032;
    public final static int RESTAURANT_2_PHOTO_WIDTH = 3024;

    // FAKE DATA RESTAURANT 3
    public final static String RESTAURANT_3_PLACE_ID = "ChIJXYZoq9h65kcRuWrPmnFCQUI";
    public final static String RESTAURANT_3_NAME = "GEMINI Boulogne";
    public final static String RESTAURANT_3_ADDRESS = "86 Avenue Jean Baptiste Clement, Boulogne-Billancourt";
    public final static double RESTAURANT_3_LATITUDE = 48.8434249;
    public final static double RESTAURANT_3_LONGITUDE = 2.2317602;
    public final static double RESTAURANT_3_RATING = 4;
    public final static String RESTAURANT_3_NUMBER = "0146056719";
    public final static String RESTAURANT_3_WEBSITE_URI = "http://www.restaurantgemini.fr";
    public final static String RESTAURANT_3_PHOTO_REFERENCE = "ATtYBwKZH_U3P3IYattCloi4PdBnOiW0zhuvhN9" +
            "-Ykmdd1kdT8ohj_gLJqYVbP3oWu4i-uw1Lvo5R_" +
            "hUiXs4k6mVPxnQcddTcGap54oHlDqpa-C339eqG" +
            "m5Amy0JS41nhLYqqHCD63S1pWCgne7DKn4kNsu4" +
            "wZKpnxnIH9BuarxS9CRQVt-u";
    public final static int RESTAURANT_3_PHOTO_HEIGHT = 1280;
    public final static int RESTAURANT_3_PHOTO_WIDTH = 1920;
}
