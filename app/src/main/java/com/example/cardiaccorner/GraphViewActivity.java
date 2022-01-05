package com.example.cardiaccorner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.chip.Chip;

import com.github.mikephil.charting.charts.LineChart;

import java.lang.reflect.Array;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view_screen);

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


        // line chart code below

        int[] colors = {R.color.dark_blue, R.color.medium_blue, R.color.light_blue, R.color.yellow, R.color.orange, R.color.red};

        LineDataSet systolicLineDataSet = new LineDataSet(systolicValues(),"Systolic");
        systolicLineDataSet.setValueTextSize(0f);
        systolicLineDataSet.setLineWidth(6);
        systolicLineDataSet.setCircleColor(Color.BLACK);
        systolicLineDataSet.setDrawCircleHole(false);
        systolicLineDataSet.setCircleRadius(4f);

        LineDataSet diastolicLineDataSet = new LineDataSet(diastolicValues(),"Diastolic");

        diastolicLineDataSet.setValueTextSize(0f);
        diastolicLineDataSet.setLineWidth(6);
        diastolicLineDataSet.setCircleColor(Color.BLACK);
        diastolicLineDataSet.setDrawCircleHole(false);
        diastolicLineDataSet.setCircleRadius(4f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(systolicLineDataSet);
        dataSets.add(diastolicLineDataSet);


        LineData data = new LineData(dataSets);
        LineChart lineChart = findViewById(R.id.line_chart);
        lineChart.setData(data);

        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setVisibleXRangeMaximum(5);
        lineChart.setHorizontalScrollBarEnabled(true);
        lineChart.setVerticalScrollBarEnabled(false);
        lineChart.moveViewToX(systolicLineDataSet.getEntryCount()-1);

        XAxis x = lineChart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.enableGridDashedLine(6f, 6f, 0f);
        x.setTextSize(16f);
        x.setLabelCount(5, true);

        YAxis y = lineChart.getAxisLeft();
        y.enableGridDashedLine(10f, 10f, 0f);
        y.setTextSize(16f);

        Legend legend = lineChart.getLegend();
        legend.setTextSize(16f);

    }

    private ArrayList<Entry> systolicValues()
    {
        ArrayList<Entry> sysVals = new ArrayList<Entry>();
        sysVals.add(new Entry(0,120));
        sysVals.add(new Entry(1,110));
        sysVals.add(new Entry(2,150));
        sysVals.add(new Entry(3,80));
        sysVals.add(new Entry(4,170));
        sysVals.add(new Entry(5,126));
        sysVals.add(new Entry(6,185));

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

        return diaVals;
    }

}