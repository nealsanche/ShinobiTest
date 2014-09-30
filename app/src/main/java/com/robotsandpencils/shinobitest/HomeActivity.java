package com.robotsandpencils.shinobitest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.shinobicontrols.charts.AxisStyle;
import com.shinobicontrols.charts.ChartView;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.LineSeries;
import com.shinobicontrols.charts.LineSeriesStyle;
import com.shinobicontrols.charts.NumberAxis;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.SeriesStyle;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.TickStyle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class HomeActivity extends Activity {

    private DomainAxis mDomainAxis;
    private NumberAxis mAnnotationAxis;
    private ChartView mChartView;
    private ShinobiChart mShinobiChart;
    private AsyncTask<Void, Void, Void> mDataGenerationTask;
    private DomainDataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final FrameLayout layout = (FrameLayout) findViewById(R.id.chart_container);

        mChartView = new ChartView(layout.getContext());
        mChartView.setLayoutParams(
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );

        // adding chartView to the bottom of the view group
        // crosshair overlay will be on top of the chart
        layout.addView(mChartView, 0);
        mChartView.onCreate(savedInstanceState);

        mShinobiChart = mChartView.getShinobiChart();
        setupChart(mShinobiChart);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupChart(ShinobiChart chart) {

        mDomainAxis = new DomainAxis(DomainAxis.Domain.TIME, true, TimeZone.getDefault());

        NumberAxis xAxis = new NumberAxis();
        final AxisStyle xAxisStyle = xAxis.getStyle();
        xAxisStyle.getTickStyle().setLabelsShown(false);
        xAxisStyle.getTickStyle().setMinorTicksShown(false);
        xAxisStyle.getTickStyle().setMajorTicksShown(false);
        xAxis.setCurrentDisplayedRangePreservedOnUpdate(false);
        xAxis.setDefaultRange(new NumberRange(0.0, 0.01));
        chart.addXAxis(xAxis);

        NumberAxis xAxis2 = new NumberAxis();
        final AxisStyle xAxisStyle2 = xAxis2.getStyle();
        xAxisStyle2.getTickStyle().setLabelsShown(false);
        xAxisStyle2.getTickStyle().setMinorTicksShown(false);
        xAxisStyle2.getTickStyle().setMajorTicksShown(false);
        xAxis2.setCurrentDisplayedRangePreservedOnUpdate(false);
        xAxis2.setDefaultRange(new NumberRange(0.0, 0.01));
        chart.addXAxis(xAxis2);

        NumberAxis xAxis3 = new NumberAxis();
        final AxisStyle xAxisStyle3 = xAxis3.getStyle();
        xAxisStyle3.getTickStyle().setLabelsShown(false);
        xAxisStyle3.getTickStyle().setMinorTicksShown(false);
        xAxisStyle3.getTickStyle().setMajorTicksShown(false);
        xAxis3.setCurrentDisplayedRangePreservedOnUpdate(false);
        xAxis3.setDefaultRange(new NumberRange(0.0, 0.01));
        chart.addXAxis(xAxis3);

        // axis for displaying static vertical grid lines
        NumberAxis gridAxis = new NumberAxis();
        final AxisStyle gridAxisStyle = gridAxis.getStyle();
        gridAxisStyle.getTickStyle().setLabelsShown(false);
        gridAxisStyle.getTickStyle().setMinorTicksShown(false);
        gridAxisStyle.getTickStyle().setMajorTicksShown(false);
        gridAxisStyle.getGridlineStyle().setGridlinesShown(true);
        gridAxis.setCurrentDisplayedRangePreservedOnUpdate(false);
        gridAxis.setDefaultRange(new NumberRange(0.0, 100.0));

        // Set up specific grid lines
        List<Double> tickMarkValues = new ArrayList<>();
        tickMarkValues.add(0.0);
        tickMarkValues.add(25.0);
        tickMarkValues.add(50.0);
        tickMarkValues.add(75.0);
        tickMarkValues.add(100.0);
        gridAxis.setMajorTickMarkValues(tickMarkValues);

        mAnnotationAxis = gridAxis;

        chart.addXAxis(gridAxis);

        chart.setYAxis(mDomainAxis);
        mDomainAxis.enableGesturePanning(true);
        mDomainAxis.enableMomentumPanning(true);
        mDomainAxis.enableGestureZooming(true);
        mDomainAxis.enableBouncingAtLimits(true);
        mDomainAxis.allowPanningOutOfDefaultRange(true);
        mDomainAxis.allowPanningOutOfMaxRange(true);
        mDomainAxis.enableAnimation(true);
        mDomainAxis.setExpectedLongestLabel("12:00");
        final AxisStyle style = mDomainAxis.getStyle();
        style.getGridlineStyle().setGridlinesShown(true);
        TickStyle domainTickStyle = style.getTickStyle();
        domainTickStyle.setLabelsShown(true);
        domainTickStyle.setMinorTicksShown(true);
        domainTickStyle.setMajorTicksShown(true);
        domainTickStyle.setLabelTextSize(10.0f);
        mDomainAxis.setMajorTickFrequency(10000.0);
        mDomainAxis.setMinorTickFrequency(1000.0);

        //mDomainAxis.setMajorTickMarkValues(new ArrayList<Double>());

        mDomainAxis.setCurrentDisplayedRangePreservedOnUpdate(true);

        xAxis.enableAnimation(false);
        xAxis.enableGesturePanning(false);
        xAxis.enableMomentumPanning(false);
        xAxis.setDefaultRange(new NumberRange(-1.2, 1.2));

        mAdapter = new DomainDataAdapter();
        LineSeries series = new LineSeries();
        series.setDataAdapter(mAdapter);
        LineSeriesStyle seriesStyle = series.getStyle();
        seriesStyle.setFillStyle(SeriesStyle.FillStyle.NONE);
        seriesStyle.setLineWidth(3.0f);
        seriesStyle.getPointStyle().setPointsShown(true);
        chart.addSeries(series, xAxis, mDomainAxis);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChartView.onResume();

        if (mDataGenerationTask != null) {
            mDataGenerationTask.cancel(true);
        }

        mDataGenerationTask = new AsyncTask<Void, Void, Void>() {
            boolean hasSetRange;

            @Override
            protected Void doInBackground(Void... params) {
                while (!isCancelled()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                    final Double timeAsDouble = DomainHelper.doubleFromDate(new Date());
                    mAdapter.add(new DataPoint<>(Math.sin(timeAsDouble / 10000.0), timeAsDouble));

                    mChartView.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.fireUpdates();

                            if (!hasSetRange) {
                                hasSetRange = true;
                                mDomainAxis.requestCurrentDisplayedRange(timeAsDouble, timeAsDouble - 30000);
                            }
                        }
                    });
                }

                return null;
            }
        };

        mDataGenerationTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mChartView.onPause();
        mDataGenerationTask.cancel(true);
    }
}