package andorid_dev_2017.navigation_drawer.sqlite_database;

public class Entry {

    String id;
    String location;
    String date;
    String startTime;
    String endTime;
    String diff1;
    String diff2;
    String diff3;
    String diff4;
    String diff5;
    String diff6;
    String diff7;
    String rating;
    String exp;
    String creator;

    public Entry(String id, String location, String date,
                 String startTime, String endTime,
                 String diff1, String diff2, String diff3,
                 String diff4, String diff5, String diff6,
                 String diff7, String rating, String exp,
                 String creator) {
        this.id = id;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.diff1 = diff1;
        this.diff2 = diff2;
        this.diff3 = diff3;
        this.diff4 = diff4;
        this.diff5 = diff5;
        this.diff6 = diff6;
        this.diff7 = diff7;
        this.rating = rating;
        this.exp = exp;
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDiff1() {
        return diff1;
    }

    public void setDiff1(String diff1) {
        this.diff1 = diff1;
    }

    public String getDiff2() {
        return diff2;
    }

    public void setDiff2(String diff2) {
        this.diff2 = diff2;
    }

    public String getDiff3() {
        return diff3;
    }

    public void setDiff3(String diff3) {
        this.diff3 = diff3;
    }

    public String getDiff4() {
        return diff4;
    }

    public void setDiff4(String diff4) {
        this.diff4 = diff4;
    }

    public String getDiff5() {
        return diff5;
    }

    public void setDiff5(String diff5) {
        this.diff5 = diff5;
    }

    public String getDiff6() {
        return diff6;
    }

    public void setDiff6(String diff6) {
        this.diff6 = diff6;
    }

    public String getDiff7() {
        return diff7;
    }

    public void setDiff7(String diff7) {
        this.diff7 = diff7;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
