package com.openclassrooms.go4lunch.service.response.details;

import java.util.List;

public class OpeningHoursResponse {
    public Boolean open_now;
    public List<PeriodResponse> periods;
    public List<String> weekday_text;
}
