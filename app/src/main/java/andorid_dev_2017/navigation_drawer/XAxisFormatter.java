package andorid_dev_2017.navigation_drawer;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class XAxisFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public XAxisFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int) value];
    }
}
