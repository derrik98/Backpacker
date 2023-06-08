package it.ispw.daniele.backpacker.entity;

public class Itinerary {

    protected int id;
    protected String guideId;
    protected String location;
    protected String date;
    protected String time;
    protected int participants;
    protected int price;
    protected String steps;

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

    public Itinerary(String guideId, String location, String date, String time, int participants, int price, String steps) {

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

    public int getId() {
        return id;
    }
}
