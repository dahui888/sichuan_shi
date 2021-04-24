package com.campuscard.app.ui.activity.card;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.base.frame.weigt.recycle.XRecyclerView;
import com.base.frame.weigt.recycle.XRefreshLayout;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.LossRecordEntity;
import com.campuscard.app.ui.holder.CardLossRecordHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 挂失记录
 */
@ContentView(R.layout.activity_list)
public class CardLossRecordActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener {
    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.lin_data)
    private LinearLayout linearLayout;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new CardLossRecordHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);
    }

    @Override
    public void getData() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.ECARD_LOSEE_RECORD);
        HttpUtils.get(this, httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<List<LossRecordEntity>>>() {
                }.getType();
                final ResultData<List<LossRecordEntity>> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null && resultData.getData().size() > 0) {
                        linearLayout.setVisibility(View.GONE);
                        mXRecyclerView.getAdapter().setData(0, resultData.getData());
                    } else {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                } else {
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
