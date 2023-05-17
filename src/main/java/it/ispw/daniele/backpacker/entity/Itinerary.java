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

    public Itinerary(int id, String guideId, String location, String date, String time, int participants, int price, String steps) {

        this.id = id;
        this.guideId = guideId;
        this.location = location;
        this.date = date;
        this.time = time;
        this.participants = participants;
        this.price = price;
        this.steps = steps;
    }

    public String getSteps() {
        return steps;
    }

    public int getId() {
        return id;
    }

    public String getGuideId() {
        return guideId;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getParticipants() {
        return participants;
    }

    public int getPrice() {
        return price;
    }

}
