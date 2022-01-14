package com.example.cardiaccorner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.chip.Chip;

import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

public class GraphViewActivity extends AppCompatActivity {

    Chip sodiumChip;
    Chip stressChip;
    Chip exerciseChip;
    Button backBtn;
    String dateTime;

    LineChart systolicLineChart, diastolicLineChart;
    ArrayList<com.example.cardiaccorner.Entry> logs, historicLogs;

    public void storeLogs(ArrayList<com.example.cardiaccorner.Entry> log){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String arr = gson.toJson(log);
        editor.putString("logs", arr);
        editor.commit();
    }

    public ArrayList<com.example.cardiaccorner.Entry> retrieveLogs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String arr = sharedPreferences.getString("logs", null);
        Type type = new TypeToken<ArrayList<com.example.cardiaccorner.Entry>>() {}.getType();

        ArrayList<com.example.cardiaccorner.Entry> log = gson.fromJson(arr, type);

        if (log == null) {
            log = new ArrayList<com.example.cardiaccorner.Entry>();
        }

        return log;
    }

    public Boolean logsStored(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.contains("logs");
    }

    static final String SHARED_PREFS = "cardiacCornerPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view_screen);

        if(logsStored()){
            logs = retrieveLogs();
            historicLogs = retrieveLogs();
        } else {
            //You can use this for node testing/dummy code
            logs = new ArrayList<>();
            historicLogs = new ArrayList<>();
        }

        System.out.println(logs);
        backBtn = (Button) findViewById(R.id.back_button);
        backBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(GraphViewActivity.this,MainActivity.class);
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

    private ArrayList<Entry> getSystolicDataSet(ArrayList<com.example.cardiaccorner.Entry> logs){
        ArrayList<Entry> sysVals = new ArrayList<Entry>();
        for(int i = 0; i<logs.size(); i++)
        {
            sysVals.add(makeSystolicEntryFromLog(logs.get(i), i));
        }

        return sysVals;
    }

    private Entry makeSystolicEntryFromLog(com.example.cardiaccorner.Entry entry, int xVal)
    {
        Entry newEntry = new Entry(xVal, entry.getSys_measurement());
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

    private ArrayList<Entry> getDiastolicDataSet(ArrayList<com.example.cardiaccorner.Entry> logs){
        ArrayList<Entry> diaVals = new ArrayList<Entry>();
        for(int i = 0; i<logs.size(); i++)
        {
            diaVals.add(makeDiastolicEntryFromLog(logs.get(i), i));
        }

        return diaVals;
    }

    private Entry makeDiastolicEntryFromLog(com.example.cardiaccorner.Entry entry, int xVal)
    {
        Entry newEntry = new Entry(xVal, entry.getDia_measurement());
        return newEntry;
    }

    private ArrayList<Entry> systolicValues()
    {
        ArrayList<Entry> sysVals = new ArrayList<Entry>();
        sysVals.add(new Entry(0,50));
        sysVals.add(new Entry(1,70));
        sysVals.add(new Entry(2,90));
        sysVals.add(new Entry(3,110));
        sysVals.add(new Entry(4,130));
        sysVals.add(new Entry(5,150));
        sysVals.add(new Entry(6,170));
        sysVals.add(new Entry(7,190));
        sysVals.add(new Entry(8,210));

        return sysVals;
    }

    private ArrayList<Entry> diastolicValues()
    {
        ArrayList<Entry> diaVals = new ArrayList<Entry>();
        diaVals.add(new Entry(0,80));
        diaVals.add(new Entry(1,70));
        diaVals.add(new Entry(2,95));
        diaVals.add(new Entry(3,55));
        diaVals.add(new Entry(4,105));
        diaVals.add(new Entry(5,67));
        diaVals.add(new Entry(6,115));
        diaVals.add(new Entry(7,90));
        diaVals.add(new Entry(8,76));

        return diaVals;
    }

    public void createGraphs(ArrayList<com.example.cardiaccorner.Entry> logs){
        // systolic line chart

        int[] colors = {R.color.dark_blue, R.color.medium_blue, R.color.light_blue, R.color.yellow, R.color.orange, R.color.red};

        ArrayList<String> dates = getDatesDataSet(logs);

        LineDataSet systolicLineDataSet = new LineDataSet(getSystolicDataSet(logs),"Systolic");
        systolicLineDataSet.setValueTextSize(12f);
        systolicLineDataSet.setLineWidth(6);
        systolicLineDataSet.setCircleColor(Color.BLACK);
        systolicLineDataSet.setDrawCircleHole(false);
        systolicLineDataSet.setCircleRadius(4f);

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

        XAxis x = systolicLineChart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setTextSize(0f);
        x.setDrawGridLines(false);
        x.setEnabled(false);

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
        diastolicLineDataSet.setValueTextSize(12f);
        diastolicLineDataSet.setLineWidth(6);
        diastolicLineDataSet.setCircleColor(Color.BLACK);
        diastolicLineDataSet.setDrawCircleHole(false);
        diastolicLineDataSet.setCircleRadius(4f);

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
        diastolicLineChart.setExtraBottomOffset(20);
        systolicLineChart.setExtraTopOffset(30);
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

}