package com.travelDestinationPlanner.fawry.exception;

public class TravelDestinationPlannerApiException extends RuntimeException {

    public TravelDestinationPlannerApiException(String message) {
        super(message);
    }


    public TravelDestinationPlannerApiException() {
        super();
    }
}