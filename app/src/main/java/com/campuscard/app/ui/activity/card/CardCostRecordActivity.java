package com.campuscard.app.ui.activity.card;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

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
import com.campuscard.app.ui.entity.CardCostEntity;
import com.campuscard.app.ui.holder.CardMoreHolder;
import com.campuscard.app.utils.PageNumUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 消费记录
 */
@ContentView(R.layout.activity_list)
public class CardCostRecordActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener {


    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.lin_data)
    private LinearLayout linearLayout;
    private int page = 1, totalPage;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new CardMoreHolder());
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
                    if (resultData.getData() != null) {
                        totalPage = PageNumUtils.getAllPageCount(resultData.getDetail().getTotalCount(), Constant.PAGE_SIZE);
                        if (resultData.getData() != null && resultData.getData().size() > 0) {
                            linearLayout.setVisibility(View.GONE);
                            if (page == 1) {
                                mXRecyclerView.getAdapter().setData(0, resultData.getData());
                            } else {
                                mXRecyclerView.getAdapter().addDataAll(0, resultData.getData());
                            }
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
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
