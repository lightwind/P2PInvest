package com.lightwind.p2pinvest.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lightwind.p2pinvest.R;
import com.lightwind.p2pinvest.common.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class LineChartActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView mIvTitleSetting;
    @BindView(R.id.line_chart)
    LineChart mLineChart;

    private Typeface mTf;//声明字体库

    @Override
    protected int getLayoutId() {
        return R.layout.activity_line_chart;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initTitle() {
        mIvTitleBack.setVisibility(View.VISIBLE);
        mTvTitle.setText("折线图Demo");
        mIvTitleSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title_back)
    public void onBack() {
        removeCurrentActivity();
    }

    @Override
    protected void initData() {

        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // 描述
        mLineChart.setDescription("收益率");
        // 是否绘制网格背景
        mLineChart.setDrawGridBackground(true);
        // 获取当前的X轴的显示位置
        XAxis xAxis = mLineChart.getXAxis();
        // 设置X轴的显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 设置X轴的字体
        xAxis.setTypeface(mTf);
        // 是否绘制X轴的网格线
        xAxis.setDrawGridLines(false);
        // 是否X轴的轴线
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        // 参数1：左边Y轴提供的区间个数。参数2：是否均匀分布间隔的区间，false表示均匀，true表示不均匀
        leftAxis.setLabelCount(5, false);

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);

        // set data
        LineData mChartData = generateDataLine(1);
        mLineChart.setData((LineData) mChartData);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        mLineChart.animateX(750);
    }

    /**
     * generates a random ChartData object with just one DataSet
     */
    private LineData generateDataLine(int cnt) {
        // 折线1
        ArrayList<Entry> e1 = new ArrayList<>();
        // 提供折线中点的数据
        for (int i = 0; i < 12; i++) {
            e1.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + cnt + ", (1)");
        // 设置折线的宽度
        d1.setLineWidth(4.5f);
        // 设置小圆圈的尺寸
        d1.setCircleSize(4.5f);
        // 设置高亮的颜色
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        // 设置是否显示小圆圈的数值
        d1.setDrawValues(false);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e2.add(new Entry(e1.get(i).getVal() - 30, i));
        }

        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleSize(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<LineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        return new LineData(getMonths(), sets);
    }

    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<>();
        m.add("Jan");
        m.add("Feb");
        m.add("Mar");
        m.add("Apr");
        m.add("May");
        m.add("Jun");
        m.add("Jul");
        m.add("Aug");
        m.add("Sep");
        m.add("Okt");
        m.add("Nov");
        m.add("Dec");

        return m;
    }
}
