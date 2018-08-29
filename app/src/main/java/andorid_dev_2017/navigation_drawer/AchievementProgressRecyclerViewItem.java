package andorid_dev_2017.navigation_drawer;

public class AchievementProgressRecyclerViewItem {

    String label;
    int progress;
    boolean isDoneStatus;

    public AchievementProgressRecyclerViewItem(String label, int progress, boolean isDoneStatus) {
        this.label = label;
        this.progress = progress;
        this.isDoneStatus = isDoneStatus;

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isDoneStatus() {
        return isDoneStatus;
    }

    public void setDoneStatus(boolean doneStatus) {
        isDoneStatus = doneStatus;
    }
}
