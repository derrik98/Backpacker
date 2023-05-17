package it.ispw.daniele.backpacker.entity;

public class Itinerary {
    private int id;
    private String guideId;
    private String location;
    private String date;
    private String time;
    private int participants;
    private int price;
    private String steps;

    public Itinerary(String guideId, String location, String date, String time, int participants, int price, String steps) {

        //this.id = id;
        this.guideId = guideId;
        this.location = location;
        this.date = date;
        this.time = time;
        this.participants = participants;
        this.price = price;
        this.steps = steps;
    }

    public String getSteps() {
        return this.steps;
    }

    public int getId() {
        return this.id;
    }

    public String getGuideId() {
        return this.guideId;
    }

    public String getLocation() {
        return this.location;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }

    public int getParticipants() {
        return this.participants;
    }

    public int getPrice() {
        return this.price;
    }

}
