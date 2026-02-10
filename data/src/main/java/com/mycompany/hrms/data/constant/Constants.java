package com.mycompany.hrms.data.constant;

public class Constants {
    public enum DocType{
        AADHAAR_CARD,
        PAN_CARD
    }

    public enum Category{
        FOOD,
        SHOPPING,
        HOTEL
    }

    public enum Designation{
        INTERN,
        CEO,
        CTO,
        HR,
        MANAGER,
        TEAM_LEAD,
    }

    public enum JobStatus{
        REJECTED,
        SELECTED,
        UNDER_REVIEW,
        PENDING
    }

    public enum TravelStatus{
        PENDING,
        ONHOLD,
        CLOSED,
        COMPETED
    }
}
