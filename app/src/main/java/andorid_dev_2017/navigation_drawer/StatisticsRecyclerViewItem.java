package andorid_dev_2017.navigation_drawer;

public class StatisticsRecyclerViewItem {

    String value;
    String label;

    public StatisticsRecyclerViewItem(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
