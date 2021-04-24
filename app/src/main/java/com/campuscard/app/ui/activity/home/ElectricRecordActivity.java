package com.campuscard.app.ui.activity.home;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.campuscard.app.ui.entity.ElectricRecordEntity;
import com.campuscard.app.ui.holder.ElectricRecordHolder;
import com.campuscard.app.utils.NetUtil;
import com.campuscard.app.utils.PageNumUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 电费记录
 */
@ContentView(R.layout.activity_record)
public class ElectricRecordActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener {
    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.bt_look_more)
    protected LinearLayout btLookMore;
    private int page = 1, totalPage;
    @ViewInject(R.id.lin_data)
    private LinearLayout lin_data;
    @ViewInject(R.id.iv_img)
    private ImageView iv_img;
    @ViewInject(R.id.tv_ms)
    private TextView tv_ms;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new ElectricRecordHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);


    }

    @Override
    public void getData() {
        if (!NetUtil.isNetworkAvalible(this)) {
            lin_data.setVisibility(View.VISIBLE);
            iv_img.setImageResource(R.mipmap.ic_zwwl);
            tv_ms.setText("哎呀，网络竟然不稳定");
        } else {

            ParamsMap paramsMap = new ParamsMap();
            paramsMap.put("pageNo", page);
            paramsMap.put("pageSize", Constant.PAGE_SIZE);
            HttpUtils.post(this, Constant.ELECT_RECORD, paramsMap, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultPageData<ElectricRecordEntity>>() {
                    }.getType();
                    ResultPageData<ElectricRecordEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        if (resultData.getData() != null) {
                            totalPage = PageNumUtils.getAllPageCount(resultData.getDetail().getTotalCount(), Constant.PAGE_SIZE);
                            if (resultData.getData() != null && resultData.getData().size() > 0) {
                                lin_data.setVisibility(View.GONE);
                                if (page == 1) {
                                    mXRecyclerView.getAdapter().setData(0, resultData.getData());
                                } else {
                                    mXRecyclerView.getAdapter().addDataAll(0, resultData.getData());
                                }
                            } else {
                                lin_data.setVisibility(View.VISIBLE);
                                iv_img.setImageResource(R.mipmap.ic_kkry);
                                tv_ms.setText("空空如也");
                            }
                        }
                        mXRecyclerView.setPullLoadMoreCompleted();
                    }
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
