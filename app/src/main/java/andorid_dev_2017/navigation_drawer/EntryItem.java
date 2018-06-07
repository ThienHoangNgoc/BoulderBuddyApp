package andorid_dev_2017.navigation_drawer;

public class EntryItem {

    String id;
    String hall;
    String date;
    float rating;


    public EntryItem(String id, String hall, String date, float rating) {
        this.id = id;
        this.hall = hall;
        this.date = date;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
