package com.campuscard.app.ui.activity.home;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.utils.XAppUtil;
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
import com.campuscard.app.ui.entity.LostFoundEntity;
import com.campuscard.app.ui.holder.LostAndFoundSearchHolder;
import com.campuscard.app.utils.NetUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 搜索结果
 */
@ContentView(R.layout.activity_search_result)
public class SearchResultActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener, TextWatcher, EditText.OnEditorActionListener {

    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.iv_img)
    protected ImageView ivImg;
    @ViewInject(R.id.tv_ms)
    protected TextView tvMs;
    @ViewInject(R.id.lin_data)
    protected LinearLayout linData;
    @ViewInject(R.id.et_search)
    protected EditText etSearch;
    @ViewInject(R.id.btn_right)
    protected TextView btnRight;
    private int page = 1, totalPage = 0;
    private String pageType = "SEARCH";

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new LostAndFoundSearchHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);
        etSearch.addTextChangedListener(this);
        etSearch.setOnEditorActionListener(this);
        btnRight.setText("取消");
        ivBack.setVisibility(View.GONE);
    }

    @Override
    public void getData() {
    }

    public void getLoadData() {
        if (!NetUtil.isNetworkAvalible(this)) {
            linData.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_zwwl);
            tvMs.setText("哎呀，网络竟然不稳定");
        } else {
            ParamsMap params = new ParamsMap();
            params.put("pageNo", page);
            params.put("pageSize", Constant.PAGE_SIZE);
            params.put("keyWorld", etSearch.getText().toString());
            params.put("lostFoundType", "LOST");
            params.put("pageType", pageType);
            params.put("lostFoundThingsTypeInfoId", "");
            HttpUtils.post(this, Constant.SWZL, params, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultPageData<LostFoundEntity>>() {
                    }.getType();
                    ResultPageData<LostFoundEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        if (resultData.getData() != null) {
                            totalPage = resultData.getDetail().getTotalCount();
                            if (resultData.getData() != null && resultData.getData().size() > 0) {
                                mXRecyclerView.setVisibility(View.VISIBLE);
                                linData.setVisibility(View.GONE);
                                if (page == 1) {
                                    mXRecyclerView.getAdapter().setData(0, resultData.getData());
                                } else {
                                    mXRecyclerView.getAdapter().addDataAll(0, resultData.getData());
                                }
                            } else {
                                mXRecyclerView.setVisibility(View.GONE);
                                linData.setVisibility(View.VISIBLE);
                                showNoData();
                            }
                        }
                    }
                    mXRecyclerView.setPullLoadMoreCompleted();
                }

                @Override
                public void onFailed(int code, String failedMsg) {
                    mXRecyclerView.setPullLoadMoreCompleted();
                }

                @Override
                public void onFinished() {
                    mXRecyclerView.setPullLoadMoreCompleted();
                }
            });
        }
    }

    /**
     * 展示无数据
     */
    private void showNoData() {
        ivImg.setImageResource(R.mipmap.ic_zwbb);
        tvMs.setText(getString(R.string.no_query_data));
    }

    @Event(type = View.OnClickListener.class, value = {R.id.btn_right})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                finish();
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s)) {
            ivBack.setVisibility(View.VISIBLE);
            btnRight.setVisibility(View.GONE);
        } else {
            ivBack.setVisibility(View.GONE);
            btnRight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (TextUtils.isEmpty(etSearch.getText().toString())) {
            XToastUtil.showToast(this, getString(R.string.search_info));
            return false;
        }
        getLoadData();
        XAppUtil.closeSoftInput(this);
        return false;
    }
}
