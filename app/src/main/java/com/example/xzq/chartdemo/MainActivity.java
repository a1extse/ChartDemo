package com.example.xzq.chartdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PriceChartView mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChart = findViewById(R.id.chart);
        PriceChartDataInfo info = JsonUtils.fromJson(json, PriceChartDataInfo.class);
        mChart.setxStepSize(info.getDay());
        mChart.setData(info.getPrice_y(), info.getPrice_x(), info.getMax_price(), info.getMin_price());
    }

    private class PriceChartDataInfo {
        private int day;
        private double max_price;
        private double min_price;
        private List<Double> price_y;
        private List<PriceChartView.DatePriceBean> price_x;

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public double getMax_price() {
            return max_price;
        }

        public void setMax_price(double max_price) {
            this.max_price = max_price;
        }

        public double getMin_price() {
            return min_price;
        }

        public void setMin_price(double min_price) {
            this.min_price = min_price;
        }

        public List<Double> getPrice_y() {
            return price_y;
        }

        public void setPrice_y(List<Double> price_y) {
            this.price_y = price_y;
        }

        public List<PriceChartView.DatePriceBean> getPrice_x() {
            return price_x;
        }

        public void setPrice_x(List<PriceChartView.DatePriceBean> price_x) {
            this.price_x = price_x;
        }

    }

    private String json = "{\"day\":9,\"max_price\":6550.5,\"min_price\":6450,\"price_y\":[6600,6550.5,6500,6450,6400],\"price_x\":[{\"date\":\"2018-12-27\",\"price\":6550.5},{\"date\":\"2018-12-28\",\"price\":6550.5},{\"date\":\"2018-12-29\",\"price\":6550.5},{\"date\":\"2018-12-30\",\"price\":6550.5},{\"date\":\"2018-12-31\",\"price\":6550.5},{\"date\":\"2019-01-01\",\"price\":6550.5},{\"date\":\"2019-01-02\",\"price\":6550.5},{\"date\":\"2019-01-03\",\"price\":6550.5},{\"date\":\"2019-01-04\",\"price\":6550.5},{\"date\":\"2019-01-05\",\"price\":6550.5},{\"date\":\"2019-01-06\",\"price\":6550.5},{\"date\":\"2019-01-07\",\"price\":6550.5},{\"date\":\"2019-01-08\",\"price\":6550.5},{\"date\":\"2019-01-09\",\"price\":6550.5},{\"date\":\"2019-01-10\",\"price\":6550.5},{\"date\":\"2019-01-11\",\"price\":6550.5},{\"date\":\"2019-01-12\",\"price\":6550.5},{\"date\":\"2019-01-13\",\"price\":6550.5},{\"date\":\"2019-01-14\",\"price\":6550.5},{\"date\":\"2019-01-15\",\"price\":6550.5},{\"date\":\"2019-01-16\",\"price\":6550.5},{\"date\":\"2019-01-17\",\"price\":6550.5},{\"date\":\"2019-01-18\",\"price\":6550.5},{\"date\":\"2019-01-19\",\"price\":6550.5},{\"date\":\"2019-01-20\",\"price\":6550.5},{\"date\":\"2019-01-21\",\"price\":6550.5},{\"date\":\"2019-01-22\",\"price\":6450}]}";
}
