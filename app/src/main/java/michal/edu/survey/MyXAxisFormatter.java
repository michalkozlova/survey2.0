package michal.edu.survey;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyXAxisFormatter extends ValueFormatter {

    public ArrayList<String> months = new ArrayList<>();
    private int lastMonth;

    public MyXAxisFormatter(int lastMonth) {
        this.lastMonth = lastMonth;

        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");
        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");

//        12-5=7
        for (int i = 0; i < (lastMonth + 7); i++) {
            months.remove(0);
        }

    }

    @Override
    public String getFormattedValue(float value) {
        return months.get((int) value);
    }
}
