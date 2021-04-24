package com.campuscard.app.ui.activity.card;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XDateUtil;
import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.activity.home.TongJiChartActivity;
import com.campuscard.app.ui.entity.BillEntity;
import com.campuscard.app.ui.entity.CardUserInfoEntity;
import com.campuscard.app.utils.DateTimeUtils;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.view.WheelStringView;
import com.campuscard.app.view.wheel.NumericWheelAdapter;
import com.campuscard.app.view.wheel.OnWheelChangedListener;
import com.campuscard.app.view.wheel.OnWheelScrollListener;
import com.campuscard.app.view.wheel.WheelView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 账单按时间筛选
 */
@ContentView(R.layout.activity_screening_by_time)
public class ScreeningByTimeActivity extends BaseActivity {
    @ViewInject(R.id.iv_back)
    protected TextView ivBack;
    @ViewInject(R.id.tv_title)
    protected TextView tvTitle;
    @ViewInject(R.id.iv_title)
    protected ImageView ivTitle;
    @ViewInject(R.id.lin_title)
    protected LinearLayout linTitle;
    @ViewInject(R.id.btn_right)
    protected TextView btnRight;


    @ViewInject(R.id.tv_start_time)
    protected TextView tvStartTime;
    @ViewInject(R.id.tv_end_time)
    protected TextView tvEndTime;
    @ViewInject(R.id.iv_clean)
    protected ImageView ivClean;
    @ViewInject(R.id.lin_day)
    protected LinearLayout linDay;
    @ViewInject(R.id.tv_week_time)
    protected TextView tvWeekTime;
    @ViewInject(R.id.lin_week)
    protected LinearLayout linWeek;
    @ViewInject(R.id.tv_month_time)
    protected TextView tvMonthTime;
    @ViewInject(R.id.lin_month)
    protected LinearLayout linMonth;

    //日
    @ViewInject(R.id.ll_day_time_start)
    private LinearLayout llDayTime;
    @ViewInject(R.id.wl_start_year)
    private WheelView wl_start_year;
    @ViewInject(R.id.wl_start_month)
    private WheelView wl_start_month;
    @ViewInject(R.id.wl_start_day)
    private WheelView wl_start_day;
    @ViewInject(R.id.view_one)
    private View viewOne;
    @ViewInject(R.id.view_two)
    private View viewTwo;

    @ViewInject(R.id.ll_day_time_end)
    private LinearLayout llDayTimeEnd;
    @ViewInject(R.id.wl_end_year)
    private WheelView wl_end_year;
    @ViewInject(R.id.wl_end_month)
    private WheelView wl_end_month;
    @ViewInject(R.id.wl_end_day)
    private WheelView wl_end_day;

    //月账单
    @ViewInject(R.id.lin_month_time)
    private LinearLayout linMonthTime;
    @ViewInject(R.id.wl_year)
    private WheelView wl_year;
    @ViewInject(R.id.wl_month)
    private WheelView wl_month;
    //周
    @ViewInject(R.id.mWheelView)
    private WheelStringView wheelStringView;

    private CardUserInfoEntity infoEntity;
    private String type = "DAY";//type:账单的类型['DAY', 'WEEK', 'MONTH'],
    private String typeCode = "1";//标题选择标识
    private String typeTag;

    @Override
    public void initView() {
        tvTitle.setText("日账单");
        toolbar.setNavigationIcon(null);
        infoEntity = EventBus.getDefault().getStickyEvent(CardUserInfoEntity.class);
        typeTag = getIntent().getStringExtra("type");

    }

    @Override
    public void getData() {
        intDayTime();
        //周数
        ArrayList<String> weekList = new ArrayList<>();
        for (int i = 1; i < 26; i++) {
            weekList.add(i + "周");
        }
        wheelStringView.refreshData(weekList);
        wheelStringView.setOnSelectListener(new WheelStringView.OnSelectListener() {
            @Override
            public void endSelect(int id, String areaProvEntity) {
                tvWeekTime.setText(areaProvEntity);
            }

            @Override
            public void selecting(int id, String areaProvEntity) {
            }
        });
    }

    @Event(value = {R.id.btn_right, R.id.iv_back, R.id.lin_title, R.id.tv_start_time, R.id.tv_end_time, R.id.iv_clean}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.btn_right:
                //完成
                loadData();
                break;
            case R.id.lin_title:
                //标题筛选
                DialogUtils.filterShowTime(this, linTitle, typeCode, new DialogUtils.OnResult() {
                    @Override
                    public void onResult(String code) {
                        if (code.equals("1")) {
                            tvTitle.setText("日账单");
                            ivTitle.setImageResource(R.mipmap.ic_on);
                            type = "DAY";
                            typeCode = code;
                            linDay.setVisibility(View.VISIBLE);
                            linMonth.setVisibility(View.GONE);
                            linWeek.setVisibility(View.GONE);
                            wheelStringView.setVisibility(View.GONE);
                            llDayTime.setVisibility(View.VISIBLE);
                            llDayTimeEnd.setVisibility(View.GONE);
                            linMonthTime.setVisibility(View.GONE);
                            intDayTime();
                        } else if (code.equals("2")) {
                            tvTitle.setText("周账单");
                            ivTitle.setImageResource(R.mipmap.ic_on);
                            type = "WEEK";
                            typeCode = code;
                            linDay.setVisibility(View.GONE);
                            linMonth.setVisibility(View.GONE);
                            linWeek.setVisibility(View.VISIBLE);
                            wheelStringView.setVisibility(View.VISIBLE);
                            llDayTimeEnd.setVisibility(View.GONE);
                            llDayTime.setVisibility(View.GONE);
                            linMonthTime.setVisibility(View.GONE);
                        } else if (code.equals("3")) {
                            tvTitle.setText("月账单");
                            ivTitle.setImageResource(R.mipmap.ic_on);
                            type = "MONTH";
                            typeCode = code;
                            linDay.setVisibility(View.GONE);
                            linMonth.setVisibility(View.VISIBLE);
                            linWeek.setVisibility(View.GONE);
                            wheelStringView.setVisibility(View.GONE);
                            llDayTimeEnd.setVisibility(View.GONE);
                            llDayTime.setVisibility(View.GONE);
                            linMonthTime.setVisibility(View.VISIBLE);
                            initMonthTime();
                        }
                    }
                });
                break;
            case R.id.tv_start_time:
                //开始时间
                llDayTime.setVisibility(View.VISIBLE);
                llDayTimeEnd.setVisibility(View.GONE);
                intDayTime();
                viewOne.setBackgroundColor(getResources().getColor(R.color.color_00b464));
                break;
            case R.id.tv_end_time:
                //结束时间
                llDayTime.setVisibility(View.GONE);
                llDayTimeEnd.setVisibility(View.VISIBLE);
                intDayTimeEnd();
                viewTwo.setBackgroundColor(getResources().getColor(R.color.color_00b464));
                break;
            case R.id.iv_clean:
                tvStartTime.setText("");
                tvEndTime.setText("");
                break;
        }
    }

    /**
     * 数据请求
     */
    private void loadData() {
        ParamsMap paramsMap = new ParamsMap();
        if (infoEntity != null) {
            paramsMap.put("customerId", infoEntity.getEcardNo());
        }
        switch (type) {
            case "DAY":
                paramsMap.put("endDay", tvEndTime.getText().toString());
                paramsMap.put("startDay", tvStartTime.getText().toString());
                break;
            case "WEEK":
                paramsMap.put("week", tvWeekTime.getText().toString().substring(0, tvWeekTime.getText().toString().length() - 1));//周账单的周数
                //季度：SPRING：夏季；AUTUMN：冬季
                paramsMap.put("quarter", "SPRING");
                paramsMap.put("year", DateTimeUtils.getInstance().newTime(XDateUtil.dateFormatY));
                paramsMap.put("month", DateTimeUtils.getInstance().newTime(XDateUtil.dateFormatYM));//月账单的月份
                break;
            case "MONTH":
                paramsMap.put("month", tvMonthTime.getText().toString());//月账单的月份
                break;
        }
        paramsMap.put("type", type);//账单的类型 = ['DAY', 'WEEK', 'MONTH'],

        EventBus.getDefault().postSticky(paramsMap);
        IntentUtil.redirectToNextActivity(ScreeningByTimeActivity.this, TongJiChartActivity.class);
    }

    /**
     * 月账单
     */
    private void initMonthTime() {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        NumericWheelAdapter numericWheelAdapterStart1 = new NumericWheelAdapter(this, 2010, 2100);
        numericWheelAdapterStart1.setLabel("年");
        wl_year.setViewAdapter(numericWheelAdapterStart1);
        numericWheelAdapterStart1.setTextColor(getResources().getColor(R.color.black_color));
        numericWheelAdapterStart1.setTextSize(20);
        wl_year.setCyclic(true);//是否可循环滑动
        wl_year.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                tvMonthTime.setText(String.valueOf(wl_year.getCurrentItem() + 2010) + "-" + String.format("%02d", wl_month.getCurrentItem() + 1));
            }
        });
        NumericWheelAdapter numericWheelAdapterStart2 = new NumericWheelAdapter(this, 1, 12, "%02d");
        numericWheelAdapterStart2.setLabel("月");
        wl_month.setViewAdapter(numericWheelAdapterStart2);
        numericWheelAdapterStart2.setTextColor(getResources().getColor(R.color.black_color));
        numericWheelAdapterStart2.setTextSize(20);
        wl_month.setCyclic(true);
        wl_year.setCurrentItem(curYear - 2010);
        wl_month.setCurrentItem(curMonth - 1);
        wl_month.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                tvMonthTime.setText(String.valueOf(wl_year.getCurrentItem() + 2010) + "-" + String.format("%02d", wl_month.getCurrentItem() + 1));
            }
        });
    }

    /**
     * 开始时间
     */
    private int curYear;
    private int curMonth;

    private void intDayTime() {
        Calendar c = Calendar.getInstance();
        curYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH) + 1;
        int curDate = c.get(Calendar.DATE);

        //默认数据
        tvStartTime.setText(curYear + "-" + curMonth + "-" + curDate);
        NumericWheelAdapter numericWheelAdapterStart1 = new NumericWheelAdapter(this, 2000, 2100);
        numericWheelAdapterStart1.setLabel(" ");
        wl_start_year.setViewAdapter(numericWheelAdapterStart1);
        numericWheelAdapterStart1.setTextColor(getResources().getColor(R.color.black_color));
        wl_start_year.setCyclic(true);//是否可循环滑动
        wl_start_year.addScrollingListener(startScrollListener);
        wl_start_year.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                initStartDayAdapter(curYear, curMonth);
            }
        });

        NumericWheelAdapter numericWheelAdapterStart2 = new NumericWheelAdapter(this, 1, 12, "%02d");
        numericWheelAdapterStart2.setLabel(" ");
        wl_start_month.setViewAdapter(numericWheelAdapterStart2);
        numericWheelAdapterStart2.setTextColor(getResources().getColor(R.color.black_color));
        wl_start_month.setCyclic(true);
        wl_start_month.addScrollingListener(startScrollListener);
        wl_start_month.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                curMonth = newValue + 1;
                initStartDayAdapter(curYear, curMonth);
            }
        });
        initStartDayAdapter(curYear, curMonth);
        wl_start_year.setCurrentItem(curYear - 2000);
        wl_start_month.setCurrentItem(curMonth - 1);
        wl_start_day.setCurrentItem(curDate - 1);
    }

    /**
     * 结束时间
     */
    private int curYearEnd;
    private int curMonthEnd;

    private void intDayTimeEnd() {
        Calendar c = Calendar.getInstance();
        curYearEnd = c.get(Calendar.YEAR);
        curMonthEnd = c.get(Calendar.MONTH) + 1;
        int curDate = c.get(Calendar.DATE);
        //默认数据
        tvEndTime.setText(curYearEnd + "-" + curMonthEnd + "-" + curDate);
        NumericWheelAdapter numericWheelAdapterEnd1 = new NumericWheelAdapter(this, 2000, 2100);
        numericWheelAdapterEnd1.setLabel(" ");
        wl_end_year.setViewAdapter(numericWheelAdapterEnd1);
        numericWheelAdapterEnd1.setTextColor(getResources().getColor(R.color.black_color));
        wl_end_year.setCyclic(true);//是否可循环滑动
        wl_end_year.addScrollingListener(endScrollListener);
        wl_end_year.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                curYearEnd = newValue + 2000;
                initEndDayAdapter(curYearEnd, curMonthEnd);
            }
        });

        NumericWheelAdapter numericWheelAdapterEnd2 = new NumericWheelAdapter(this, 1, 12, "%02d");
        numericWheelAdapterEnd2.setLabel(" ");
        wl_end_month.setViewAdapter(numericWheelAdapterEnd2);
        numericWheelAdapterEnd2.setTextColor(getResources().getColor(R.color.black_color));
        wl_end_month.setCyclic(true);
        wl_end_month.addScrollingListener(endScrollListener);
        wl_end_month.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                curMonthEnd = newValue + 1;
                initEndDayAdapter(curYearEnd, curMonthEnd);
            }
        });
        initEndDayAdapter(curYearEnd, curMonthEnd);

        wl_end_year.setCurrentItem(curYearEnd - 2000);
        wl_end_month.setCurrentItem(curMonthEnd - 1);
        wl_end_day.setCurrentItem(curDate - 1);

    }

    private void initStartDayAdapter(int curYear, int curMonth) {
        NumericWheelAdapter numericWheelAdapterStart3 = new NumericWheelAdapter(this, 1, getDay(curYear, curMonth), "%02d");
        numericWheelAdapterStart3.setLabel(" ");
        wl_start_day.setViewAdapter(numericWheelAdapterStart3);
        numericWheelAdapterStart3.setTextColor(getResources().getColor(R.color.black_color));
        wl_start_day.setCyclic(true);
        wl_start_day.addScrollingListener(startScrollListener);
    }

    private void initEndDayAdapter(int curYear, int curMonth) {
        NumericWheelAdapter numericWheelAdapterEnd3 = new NumericWheelAdapter(this, 1, getDay(curYear, curMonth), "%02d");
        numericWheelAdapterEnd3.setLabel(" ");
        wl_end_day.setViewAdapter(numericWheelAdapterEnd3);
        numericWheelAdapterEnd3.setTextColor(getResources().getColor(R.color.black_color));
        wl_end_day.setCyclic(true);
        wl_end_day.addScrollingListener(endScrollListener);
    }

    OnWheelScrollListener startScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = wl_start_year.getCurrentItem() + 2000;//年
            int n_month = wl_start_month.getCurrentItem() + 1;//月
            int n_day = wl_start_day.getCurrentItem() + 1;//日
            tvStartTime.setText(n_year + "-" + n_month + "-" + n_day);
        }
    };
    OnWheelScrollListener endScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = wl_end_year.getCurrentItem() + 2000;//年
            int n_month = wl_end_month.getCurrentItem() + 1;//月
            int n_day = wl_end_day.getCurrentItem() + 1;//日
            tvEndTime.setText(n_year + "-" + n_month + "-" + n_day);
        }
    };

    /**
     * 根据年月获得 这个月总共有几天
     *
     * @param year
     * @param month
     * @return
     */
    public int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

}
