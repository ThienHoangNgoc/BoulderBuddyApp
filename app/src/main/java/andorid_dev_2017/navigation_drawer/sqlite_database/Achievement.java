package andorid_dev_2017.navigation_drawer.sqlite_database;

public class Achievement {
    String id;
    String name;
    int status;
    String creator;

    public Achievement(String id, String name, int status, String creator) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
