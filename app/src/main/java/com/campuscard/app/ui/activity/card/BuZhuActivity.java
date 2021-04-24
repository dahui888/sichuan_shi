package com.campuscard.app.ui.activity.card;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.base.frame.weigt.recycle.XRecyclerView;
import com.base.frame.weigt.recycle.XRefreshLayout;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.TranerEntity;
import com.campuscard.app.ui.holder.BuZhuHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 补助查询
 */
@ContentView(R.layout.activity_list)
public class BuZhuActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener {

    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.lin_data)
    private LinearLayout linearLayout;

    @Override
    public void initView() {
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new BuZhuHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);
        toolbar.setNavigationIcon(null);
    }

    @Override
    public void getData() {
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("waitTransferRecType", "ALLOWANCE");
        HttpUtils.post(this, Constant.TRANSFER_RECORD, paramsMap, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<TranerEntity>>() {
                }.getType();
                ResultData<TranerEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        if (resultData.getData().getRechargeRecs() != null && resultData.getData().getRechargeRecs().size() > 0) {
                            mXRecyclerView.getAdapter().setData(0, resultData.getData().getRechargeRecs());
                            linearLayout.setVisibility(View.GONE);
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }else {
                    linearLayout.setVisibility(View.VISIBLE);
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
        getData();
    }

    @Override
    public boolean onLoadMore() {
        return false;
    }
}
