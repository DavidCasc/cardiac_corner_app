package com.example.cardiaccorner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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

    static final String SHARED_PREFS = "cardiacCornerPrefs";
    ArrayList<Entry> logs;

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
        } else {
            logs = new ArrayList<>();
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
                        if(sodiumChip.isChecked()){
                            // code to display data with sodium tag
                        } else{
                            // other case
                        }
                    }
                });

        stressChip = (Chip) findViewById(R.id.chip2_card);
        stressChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(stressChip.isChecked()){
                            // code to display data with stress tag
                        } else{
                            // other case
                        }
                    }
                });

        exerciseChip = (Chip) findViewById(R.id.chip3_card);
        exerciseChip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(exerciseChip.isChecked()){
                            // code to display data with heavy exercise tag
                        } else{
                            // other case
                        }
                    }
                });


        // systolic line chart

        int[] colors = {R.color.dark_blue, R.color.medium_blue, R.color.light_blue, R.color.yellow, R.color.orange, R.color.red};

        ArrayList<com.example.cardiaccorner.Entry> logs = retrieveLogs();

        ArrayList<String> dates = getDatesDataSet(logs);

        LineDataSet systolicLineDataSet = new LineDataSet(getSystolicDataSet(logs),"Systolic");
        systolicLineDataSet.setValueTextSize(0f);
        systolicLineDataSet.setLineWidth(6);
        systolicLineDataSet.setCircleColor(Color.BLACK);
        systolicLineDataSet.setDrawCircleHole(false);
        systolicLineDataSet.setCircleRadius(4f);

        LineData systolicData = new LineData(systolicLineDataSet);
        LineChart systolicLineChart = findViewById(R.id.systolic_line_chart);
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
        systolicLineChart.setExtraRightOffset(32);

        XAxis x = systolicLineChart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setTextSize(13f);
        x.setLabelRotationAngle(-45);
        x.setDrawGridLines(false);
        x.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(dates));
        x.setGranularityEnabled(true);

        YAxis y = systolicLineChart.getAxisLeft();
        y.setTextSize(15f);
        y.setAxisMinimum(60);
        y.setAxisMaximum(210);
        y.setDrawGridLines(false);

        Legend legend = systolicLineChart.getLegend();
        legend.setEnabled(false);

        LinearGradient linearGradient = new LinearGradient(
                0, 0, 0, 500,
                new int[]{Color.parseColor("#000000"), Color.parseColor("#d73027"), Color.parseColor("#fc8d59"), Color.parseColor("#fee090"), Color.parseColor("#e0f3f8"), Color.parseColor("#91bfdb"), Color.parseColor("#4575b4")},
                new float[]{0.01f, 0.1f, 0.25f, 0.4f, 0.45f, 0.65f, 0.85f},
                Shader.TileMode.CLAMP);

        Paint paint = systolicLineChart.getRenderer().getPaintRender();
        paint.setShader(linearGradient);


        // diastolic line chart

        LineDataSet diastolicLineDataSet = new LineDataSet(getDiastolicDataSet(logs),"Diastolic");
        diastolicLineDataSet.setValueTextSize(0f);
        diastolicLineDataSet.setLineWidth(6);
        diastolicLineDataSet.setCircleColor(Color.BLACK);
        diastolicLineDataSet.setDrawCircleHole(false);
        diastolicLineDataSet.setCircleRadius(4f);

        LineData diastolicData = new LineData(diastolicLineDataSet);
        LineChart diastolicLineChart = findViewById(R.id.diastolic_line_chart);
        diastolicLineChart.setData(diastolicData);

        diastolicLineChart.setTouchEnabled(true);
        diastolicLineChart.setPinchZoom(false);
        diastolicLineChart.getAxisRight().setDrawLabels(false);
        diastolicLineChart.getDescription().setEnabled(false);
        diastolicLineChart.setVisibleXRangeMaximum(5);
        diastolicLineChart.setHorizontalScrollBarEnabled(true);
        diastolicLineChart.setVerticalScrollBarEnabled(false);
        diastolicLineChart.moveViewToX(diastolicLineDataSet.getEntryCount()-1);
        diastolicLineChart.setExtraBottomOffset(10);
        diastolicLineChart.setExtraRightOffset(32);

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
        y2.setAxisMaximum(140);
        y2.setDrawGridLines(false);

        Legend legend2 = diastolicLineChart.getLegend();
        legend2.setEnabled(false);

        LinearGradient linearGradient2 = new LinearGradient(
                0, 0, 0, 500,
                new int[]{Color.parseColor("#000000"), Color.parseColor("#d73027"), Color.parseColor("#fc8d59"), Color.parseColor("#fee090"), Color.parseColor("#e0f3f8"), Color.parseColor("#91bfdb"), Color.parseColor("#4575b4")},
                new float[]{0.01f, 0.1f, 0.25f, 0.4f, 0.45f, 0.65f, 0.85f},
                Shader.TileMode.CLAMP);

        Paint paint2 = diastolicLineChart.getRenderer().getPaintRender();
        paint2.setShader(linearGradient2);


        systolicLineChart.setOnChartGestureListener(new MultiChartGestureListener(systolicLineChart, new Chart[] {diastolicLineChart}));
        diastolicLineChart.setOnChartGestureListener(new MultiChartGestureListener(diastolicLineChart, new Chart[] {systolicLineChart}));
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
        com.github.mikephil.charting.data.Entry newEntry = new com.github.mikephil.charting.data.Entry(xVal, entry.getSys_measurement());
        return newEntry;
    }

    private ArrayList<String> getDatesDataSet(ArrayList<com.example.cardiaccorner.Entry> logs){
        ArrayList<String> dates = new ArrayList<String>();
        for(int i = 0; i<logs.size(); i++)
        {
            String[] splitDate = logs.get(i).getTime_created().split(" ");
            dates.add(splitDate[0]);
        }

        System.out.println(dates);
        return dates;
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
        com.github.mikephil.charting.data.Entry newEntry = new com.github.mikephil.charting.data.Entry(xVal, entry.getDia_measurement());
        return newEntry;
    }

}
