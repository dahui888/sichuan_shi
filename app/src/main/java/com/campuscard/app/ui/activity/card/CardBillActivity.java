package com.campuscard.app.ui.activity.card;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XToastUtil;
import com.base.frame.weigt.recycle.XRecyclerView;
import com.base.frame.weigt.recycle.XRefreshLayout;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultPageData;
import com.campuscard.app.ui.activity.home.TongJiChartActivity;
import com.campuscard.app.ui.entity.CardCostEntity;
import com.campuscard.app.ui.holder.CardBillHolder;
import com.campuscard.app.utils.PageNumUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 校园卡账单
 */
@ContentView(R.layout.activity_card_bill)
public class CardBillActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener {

    @ViewInject(R.id.btn_time)
    protected ImageView btnTime;

    @ViewInject(R.id.btn_right)
    protected ImageView btnRight;

    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;

    @ViewInject(R.id.lin_data)
    private LinearLayout lin_data;
    private int page = 1, totalPage;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new CardBillHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);
    }

    @Override
    public void getData() {
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("pageNo", page);
        paramsMap.put("pageSize", Constant.PAGE_SIZE);
        HttpUtils.post(this, Constant.CARD_RECODE, paramsMap, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultPageData<CardCostEntity>>() {
                }.getType();
                ResultPageData<CardCostEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    totalPage = PageNumUtils.getAllPageCount(resultData.getDetail().getTotalCount(), Constant.PAGE_SIZE);
                    if (resultData.getData() != null && resultData.getData().size() > 0) {
                        lin_data.setVisibility(View.GONE);
                        mXRecyclerView.setVisibility(View.VISIBLE);
                        if (page == 1) {
                            mXRecyclerView.getAdapter().setData(0, resultData.getData());
                        } else {
                            mXRecyclerView.getAdapter().addDataAll(0, resultData.getData());
                        }
                    } else {
                        lin_data.setVisibility(View.VISIBLE);
                    }
                } else {
                    lin_data.setVisibility(View.VISIBLE);
                    mXRecyclerView.setVisibility(View.GONE);
                }
                mXRecyclerView.setPullLoadMoreCompleted();
            }

            @Override
            public void onFailed(int code, String failedMsg) {
                mXRecyclerView.setPullLoadMoreCompleted();
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Event(value = {R.id.btn_time, R.id.btn_reset, R.id.btn_right}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_time:
                //饼图
                IntentUtil.redirectToNextActivity(this, TongJiChartActivity.class);
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
