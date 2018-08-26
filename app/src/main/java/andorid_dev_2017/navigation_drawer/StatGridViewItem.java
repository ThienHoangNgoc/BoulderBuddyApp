package andorid_dev_2017.navigation_drawer;

public class StatGridViewItem {


    String statValue;
    String statLabel;

    public StatGridViewItem(String statValue, String statLabel) {

        this.statValue = statValue;
        this.statLabel = statLabel;
    }

    public String getStatValue() {
        return statValue;
    }

    public void setStatValue(String statValue) {
        this.statValue = statValue;
    }

    public String getStatLabel() {
        return statLabel;
    }

    public void setStatLabel(String statLabel) {
        this.statLabel = statLabel;
    }


}
