package com.campuscard.app.ui.activity.card;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.base.frame.utils.XDateUtil;
import com.base.frame.utils.XToastUtil;
import com.base.frame.weigt.recycle.XRecyclerView;
import com.base.frame.weigt.recycle.XRefreshLayout;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultPageData;
import com.campuscard.app.ui.entity.CardRecordEntity;
import com.campuscard.app.ui.holder.CardRecordHolder;
import com.campuscard.app.utils.DateTimeUtils;
import com.campuscard.app.utils.NetUtil;
import com.campuscard.app.utils.PageNumUtils;
import com.campuscard.app.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 存款信息
 */
@ContentView(R.layout.activity_save_money_record)
public class SaveMoneyRecordActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener {

    @ViewInject(R.id.iv_back)
    protected TextView ivBack;
    @ViewInject(R.id.tv_title)
    protected TextView tvTitle;
    @ViewInject(R.id.tv_right)
    protected TextView tvRight;
    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.tv_money_info)
    protected TextView tvMoneyInfo;
    @ViewInject(R.id.tv_money)
    protected TextView tvMoney;
    @ViewInject(R.id.tv_danwei)
    private TextView tvDanWei;
    @ViewInject(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    private int page = 1, totalPage;
    @ViewInject(R.id.lin_data)
    private LinearLayout linearLayout;

    //侧滑控件部分
    @ViewInject(R.id.tv_starttime)
    private TextView tvStartTime;
    @ViewInject(R.id.tv_endtime)
    private TextView tvEndTime;
    @ViewInject(R.id.bt_week)
    private RadioButton btWeek;
    @ViewInject(R.id.bt_one_yue)
    private RadioButton btOneYue;
    @ViewInject(R.id.bt_san_yue)
    private RadioButton btSanYue;
    @ViewInject(R.id.reset)
    private TextView btReset;
    @ViewInject(R.id.tv_search)
    private TextView btSearch;
    private double money;


    //无数据页面
    @ViewInject(R.id.iv_img)
    private ImageView iv_img;
    @ViewInject(R.id.tv_ms)
    private TextView tv_ms;

    @Override
    public void initView() {
        tvTitle.setText("存款信息");
        tvMoneyInfo.setText("充值余额");
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new CardRecordHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);
        money = getIntent().getDoubleExtra("money", 0);
        String moneyAll = StringUtil.douToString(money);
        if (!TextUtils.isEmpty(moneyAll)) {
            String[] moneyArrys = moneyAll.split("\\.");
            if (moneyArrys.length > 0) {
                tvMoney.setText(moneyArrys[0]);
                tvDanWei.setText("." + moneyArrys[1] + "元");
            }
        }
    }

    @Override
    public void getData() {
        if (!NetUtil.isNetworkAvalible(this)) {
            linearLayout.setVisibility(View.VISIBLE);
            iv_img.setImageResource(R.mipmap.ic_zwwl);
            tv_ms.setText("哎呀，网络竟然不稳定");
        } else {
            ParamsMap params = new ParamsMap();
            params.put("pageNo", page);
            params.put("pageSize", Constant.PAGE_SIZE);
            if (!TextUtils.isEmpty(tvEndTime.getText().toString())) {
                params.put("endTime", tvEndTime.getText().toString());
            }
            if (!TextUtils.isEmpty(tvStartTime.getText().toString())) {
                params.put("startTime", tvStartTime.getText().toString());
            }
            HttpUtils.post(this, Constant.CARD_RECORD, params, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultPageData<CardRecordEntity>>() {
                    }.getType();
                    ResultPageData<CardRecordEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        totalPage = PageNumUtils.getAllPageCount(resultData.getDetail().getTotalCount(), Constant.PAGE_SIZE);
                        if (resultData.getData() != null && resultData.getData().size() > 0) {
                            linearLayout.setVisibility(View.GONE);
                            mXRecyclerView.setVisibility(View.VISIBLE);
                            if (page == 1) {
                                mXRecyclerView.getAdapter().setData(0, resultData.getData());
                            } else {
                                mXRecyclerView.getAdapter().addDataAll(0, resultData.getData());
                            }
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(tvEndTime.getText().toString())) {
                                iv_img.setImageResource(R.mipmap.ic_zwxx);
                                tv_ms.setText(getString(R.string.no_kong_data));
                            } else {
                                iv_img.setImageResource(R.mipmap.ic_zwbb);
                                tv_ms.setText(getString(R.string.no_query_data));
                            }
                            mXRecyclerView.setVisibility(View.GONE);
                        }
                    }
                    mXRecyclerView.setPullLoadMoreCompleted();
                }
                @Override
                public void onFailed(int code, String failedMsg) {
                }
                @Override
                public void onFinished() {
                }
            });
        }
    }

    @Event(value = {R.id.iv_back, R.id.tv_right, R.id.tv_starttime, R.id.tv_endtime, R.id.bt_week, R.id.bt_one_yue, R.id.bt_san_yue,
            R.id.reset, R.id.tv_search}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //侧滑--筛选
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
            case R.id.tv_starttime:
                //侧滑--开始时间
                btWeek.setChecked(false);
                btOneYue.setChecked(false);
                btSanYue.setChecked(false);
                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = new SimpleDateFormat(XDateUtil.dateFormatYMD).format(date);
                        tvStartTime.setText(time);
                    }
                }).setType(new boolean[]{true, true, true, false, false, false})
                        .setCancelColor(R.color.black)
                        .setSubmitColor(R.color.black)
                        .build();
                pvTime.show();
                break;
            case R.id.tv_endtime:
                //侧滑--结束时间
                btWeek.setChecked(false);
                btOneYue.setChecked(false);
                btSanYue.setChecked(false);
                TimePickerView pvTimeEnd = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = new SimpleDateFormat(XDateUtil.dateFormatYMD).format(date);
                        tvEndTime.setText(time);
                    }
                }).setType(new boolean[]{true, true, true, false, false, false})
                        .setCancelColor(R.color.black)
                        .setSubmitColor(R.color.black)
                        .build();
                pvTimeEnd.show();
                break;
            case R.id.bt_week:
                //侧滑--一周
                btWeek.setChecked(true);
                btOneYue.setChecked(false);
                btSanYue.setChecked(false);
                tvStartTime.setText(DateTimeUtils.getInstance().weekTime());
                tvEndTime.setText(DateTimeUtils.getInstance().newTime());
                break;
            case R.id.bt_one_yue:
                //侧滑--一月
                btWeek.setChecked(false);
                btOneYue.setChecked(true);
                btSanYue.setChecked(false);
                tvStartTime.setText(DateTimeUtils.getInstance().oneMothTime());
                tvEndTime.setText(DateTimeUtils.getInstance().newTime());
                break;
            case R.id.bt_san_yue:
                //侧滑--三月
                btWeek.setChecked(false);
                btOneYue.setChecked(false);
                btSanYue.setChecked(true);
                tvStartTime.setText(DateTimeUtils.getInstance().threeMothTime());
                tvEndTime.setText(DateTimeUtils.getInstance().newTime());
                break;
            case R.id.reset:
                //侧滑--重置
                btWeek.setChecked(false);
                btOneYue.setChecked(false);
                btSanYue.setChecked(false);
                tvStartTime.setText("");
                tvEndTime.setText("");
                break;
            case R.id.tv_search:
                //侧滑--查询
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
                getData();
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }

    @Override
    public boolean onLoadMore() {
        if (page < totalPage) {
            page++;
            getData();
            return true;
        } else {
            XToastUtil.showToast(this, getString(R.string.no_data));
        }
        return false;
    }
}
