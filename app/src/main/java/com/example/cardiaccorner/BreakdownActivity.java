package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BreakdownActivity extends AppCompatActivity {

    Chip sodiumChip;
    Chip stressChip;
    Chip exerciseChip;

    Button finishBtn;
    Button backBtn;
    LineChart systolicLineChart, diastolicLineChart;
    ArrayList<com.example.cardiaccorner.Entry> logs, historicLogs;

    static final String SHARED_PREFS = "cardiacCornerPrefs";

    public Boolean logsStored(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.contains("logs");
    }
    public void storeLogs(ArrayList<Entry> log){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String arr = gson.toJson(log);
        editor.putString("logs", arr);
        editor.commit();
    }
    public ArrayList<Entry> retrieveLogs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String arr = sharedPreferences.getString("logs", null);
        Type type = new TypeToken<ArrayList<Entry>>() {}.getType();

        ArrayList<Entry> log = gson.fromJson(arr, type);

        if (log == null) {
            log = new ArrayList<Entry>();
        }

        return log;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(BreakdownActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakdown_screen);

        if(logsStored()){
            logs = retrieveLogs();
            historicLogs = retrieveLogs();
        } else {
            logs = new ArrayList<>();
            historicLogs = new ArrayList<>();
        }
        System.out.println("HERE");
        System.out.println(logs);

        finishBtn = (Button) findViewById(R.id.finish_button);
        finishBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BreakdownActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                });

        backBtn = (Button) findViewById(R.id.back_button);
        backBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(BreakdownActivity.this,NewMeasurementActivity.class);
                        startActivity(i);
                    }
                });

        sodiumChip = (Chip) findViewById(R.id.chip1_card);
        sodiumChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterList();
                    }
                });

        stressChip = (Chip) findViewById(R.id.chip2_card);
        stressChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterList();
                    }
                });

        exerciseChip = (Chip) findViewById(R.id.chip3_card);
        exerciseChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterList();
                    }
                });

        systolicLineChart = findViewById(R.id.systolic_line_chart);
        diastolicLineChart = findViewById(R.id.diastolic_line_chart);
        createGraphs(logs);
    }

    private ArrayList<com.github.mikephil.charting.data.Entry> getSystolicDataSet(ArrayList<com.example.cardiaccorner.Entry> logs){
        ArrayList<com.github.mikephil.charting.data.Entry> sysVals = new ArrayList<com.github.mikephil.charting.data.Entry>();
        for(int i = 0; i<logs.size(); i++)
        {
            sysVals.add(makeSystolicEntryFromLog(logs.get(i), i));
        }

        return sysVals;
    }

    private com.github.mikephil.charting.data.Entry makeSystolicEntryFromLog(com.example.cardiaccorner.Entry entry, int xVal)
    {
        com.github.mikephil.charting.data.Entry newEntry = new com.github.mikephil.charting.data.Entry(xVal, entry.getSys_measurement(), entry);
        return newEntry;
    }

    private ArrayList<String> getDatesDataSet(ArrayList<com.example.cardiaccorner.Entry> logs){
        ArrayList<String> dates = new ArrayList<String>();
        for(int i = 0; i<logs.size(); i++)
        {
            String[] splitDate = logs.get(i).getTime_created().split(" ");
            String[] splitTime = splitDate[1].split(":");
            dates.add(splitDate[0] + "\n" + splitTime[0] + ":" + splitTime[1]);
        }

        System.out.println(dates);
        return dates;
    }

    private ArrayList<String> getTagsDataSet(ArrayList<com.example.cardiaccorner.Entry> logs){
        ArrayList<String> tags = new ArrayList<String>();
        for(int i = 0; i<logs.size(); i++)
        {
            String getTags = logs.get(i).getTags();
            tags.add(getTags);
        }
        return tags;
    }

    private ArrayList<com.github.mikephil.charting.data.Entry> getDiastolicDataSet(ArrayList<com.example.cardiaccorner.Entry> logs){
        ArrayList<com.github.mikephil.charting.data.Entry> diaVals = new ArrayList<com.github.mikephil.charting.data.Entry>();
        for(int i = 0; i<logs.size(); i++)
        {
            diaVals.add(makeDiastolicEntryFromLog(logs.get(i), i));
        }

        return diaVals;
    }

    private com.github.mikephil.charting.data.Entry makeDiastolicEntryFromLog(com.example.cardiaccorner.Entry entry, int xVal)
    {
        com.github.mikephil.charting.data.Entry newEntry = new com.github.mikephil.charting.data.Entry(xVal, entry.getDia_measurement(), entry);
        return newEntry;
    }
    public void createGraphs(ArrayList<com.example.cardiaccorner.Entry> logs){
        // systolic line chart

        int[] colors = {R.color.dark_blue, R.color.medium_blue, R.color.light_blue, R.color.yellow, R.color.orange, R.color.red};

        ArrayList<String> dates = getDatesDataSet(logs);
        ArrayList<String> tags = getTagsDataSet(logs);

        LineDataSet systolicLineDataSet = new LineDataSet(getSystolicDataSet(logs),"Systolic");
        systolicLineDataSet.setValueTextSize(12f);
        systolicLineDataSet.setLineWidth(6);
        systolicLineDataSet.setCircleColor(Color.BLACK);
        systolicLineDataSet.setDrawCircleHole(false);
        systolicLineDataSet.setCircleRadius(4f);
        systolicLineDataSet.setValueFormatter(new BreakdownActivity.MyValueFormatter());

        LineData systolicData = new LineData(systolicLineDataSet);

        systolicLineChart.setData(systolicData);

        systolicLineChart.setTouchEnabled(true);
        systolicLineChart.setPinchZoom(false);
        systolicLineChart.getAxisRight().setDrawLabels(false);
        systolicLineChart.getDescription().setEnabled(false);
        systolicLineChart.setVisibleXRangeMaximum(5);
        systolicLineChart.setHorizontalScrollBarEnabled(true);
        systolicLineChart.setVerticalScrollBarEnabled(false);
        systolicLineChart.moveViewToX(systolicLineDataSet.getEntryCount()-1);
        systolicLineChart.setExtraBottomOffset(10);
        systolicLineChart.setExtraTopOffset(30);
        systolicLineChart.setExtraRightOffset(32);

        BreakdownActivity.CustomMarkerView mv = new BreakdownActivity.CustomMarkerView(getApplicationContext(), R.layout.custom_marker_view_layout);
        systolicLineChart.setMarker(mv);

        XAxis x = systolicLineChart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setTextSize(0f);
        x.setDrawGridLines(false);
        x.setEnabled(false);

        YAxis y = systolicLineChart.getAxisLeft();
        y.setTextSize(15f);
        y.setAxisMinimum(30);
        y.setAxisMaximum(210);
        y.setDrawGridLines(false);

        Legend legend = systolicLineChart.getLegend();
        legend.setEnabled(false);

        LinearGradient linearGradient = new LinearGradient(
                0, 0, 0, 500,
                new int[]{Color.parseColor("#000000"), Color.parseColor("#d73027"), Color.parseColor("#fc8d59"), Color.parseColor("#fee090"), Color.parseColor("#e0f3f8"), Color.parseColor("#91bfdb"), Color.parseColor("#4575b4")},
                new float[]{0.01f, 0.2f, 0.3f, 0.35f, 0.45f, 0.6f, 0.7f},
                Shader.TileMode.CLAMP);

        Paint paint = systolicLineChart.getRenderer().getPaintRender();
        paint.setShader(linearGradient);


        // diastolic line chart

        LineDataSet diastolicLineDataSet = new LineDataSet(getDiastolicDataSet(logs),"Diastolic");
        diastolicLineDataSet.setValueTextSize(12f);
        diastolicLineDataSet.setLineWidth(6);
        diastolicLineDataSet.setCircleColor(Color.BLACK);
        diastolicLineDataSet.setDrawCircleHole(false);
        diastolicLineDataSet.setCircleRadius(4f);
        diastolicLineDataSet.setValueFormatter(new BreakdownActivity.MyValueFormatter());

        LineData diastolicData = new LineData(diastolicLineDataSet);
        diastolicLineChart.setData(diastolicData);

        diastolicLineChart.setTouchEnabled(true);
        diastolicLineChart.setPinchZoom(false);
        diastolicLineChart.getAxisRight().setDrawLabels(false);
        diastolicLineChart.getDescription().setEnabled(false);
        diastolicLineChart.setVisibleXRangeMaximum(5);
        diastolicLineChart.setHorizontalScrollBarEnabled(true);
        diastolicLineChart.setVerticalScrollBarEnabled(false);
        diastolicLineChart.moveViewToX(diastolicLineDataSet.getEntryCount()-1);
        systolicLineChart.setExtraTopOffset(30);
        diastolicLineChart.setExtraRightOffset(32);
        diastolicLineChart.setXAxisRenderer(new BreakdownActivity.CustomXAxisRenderer(diastolicLineChart.getViewPortHandler(), diastolicLineChart.getXAxis(), diastolicLineChart.getTransformer(YAxis.AxisDependency.LEFT)));

        diastolicLineChart.setMarker(mv);

        XAxis x2 = diastolicLineChart.getXAxis();
        x2.setPosition(XAxis.XAxisPosition.BOTTOM);
        x2.setTextSize(13f);
        x2.setLabelRotationAngle(-45);
        x2.setDrawGridLines(false);
        x2.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(dates));
        x2.setGranularityEnabled(true);

        YAxis y2 = diastolicLineChart.getAxisLeft();
        y2.setTextSize(15f);
        y2.setAxisMinimum(30);
        y2.setAxisMaximum(210);
        y2.setDrawGridLines(false);

        Legend legend2 = diastolicLineChart.getLegend();
        legend2.setEnabled(false);

        LinearGradient linearGradient2 = new LinearGradient(
                0, 0, 0, 500,
                new int[]{Color.parseColor("#000000"), Color.parseColor("#d73027"), Color.parseColor("#fc8d59"), Color.parseColor("#fee090"), Color.parseColor("#e0f3f8"), Color.parseColor("#91bfdb"), Color.parseColor("#4575b4")},
                new float[]{0.01f, 0.4f, 0.44f, 0.48f, 0.50f, 0.55f, 0.65f},
                Shader.TileMode.CLAMP);

        Paint paint2 = diastolicLineChart.getRenderer().getPaintRender();
        paint2.setShader(linearGradient2);


        systolicLineChart.setOnChartGestureListener(new MultiChartGestureListener(systolicLineChart, new Chart[] {diastolicLineChart}));
        diastolicLineChart.setOnChartGestureListener(new MultiChartGestureListener(diastolicLineChart, new Chart[] {systolicLineChart}));
    }

    public void filterList() {
        logs.clear();
        for(com.example.cardiaccorner.Entry e: historicLogs){
            if(stressChip.isChecked() && e.isStress()){
                logs.add(e);
            } else if(sodiumChip.isChecked() && e.isSodium()){
                logs.add(e);
            } else if(exerciseChip.isChecked() && e.isSodium()){
                logs.add(e);
            }
        }
        if(!stressChip.isChecked() && !exerciseChip.isChecked() && !sodiumChip.isChecked()){
            logs = new ArrayList<>(historicLogs);
        }

        createGraphs(logs);
        systolicLineChart.invalidate();
        diastolicLineChart.invalidate();
    }
    public class CustomXAxisRenderer extends XAxisRenderer {
        public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
        }

        @Override
        protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
            String line[] = formattedLabel.split("\n");
            Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees);
            Utils.drawXAxisValue(c, line[1], x + mAxisLabelPaint.getTextSize(), y + mAxisLabelPaint.getTextSize(), mAxisLabelPaint, anchor, angleDegrees);
        }
    }

    public class CustomMarkerView extends MarkerView {

        private TextView tvContent;

        public CustomMarkerView (Context context, int layoutResource) {
            super(context, layoutResource);
            // this markerview only displays a textview
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(com.github.mikephil.charting.data.Entry e, Highlight highlight) {

            tvContent.setText(((com.example.cardiaccorner.Entry) e.getData()).getTags()); // set the entry-value as the display text
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }

    }

    class MyValueFormatter extends ValueFormatter {

        @Override
        public String getFormattedValue(float value) {

            int intVal = (int) value;
            return String.valueOf(intVal);
        }
    }
}
