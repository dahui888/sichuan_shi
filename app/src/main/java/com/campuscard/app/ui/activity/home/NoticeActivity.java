package com.campuscard.app.ui.activity.home;

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
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.NoticeEntity;
import com.campuscard.app.ui.holder.NoticeHolder;
import com.campuscard.app.utils.PageNumUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 *公告
 */
@ContentView(R.layout.activity_list)
public class NoticeActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener {

    @ViewInject(R.id.mXRecyclerView)
    private XRecyclerView mXRecyclerView;
    @ViewInject(R.id.lin_data)
    private LinearLayout lin_data;
    private int page = 1, totalPage;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new NoticeHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void getData() {
        ParamsMap params = new ParamsMap();
        params.put("pageNo", page);
        params.put("pageSize", Constant.PAGE_SIZE);
        HttpUtils.post(this, Constant.NOTICE_PAGE, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultPageData<NoticeEntity>>() {
                }.getType();
                ResultPageData<NoticeEntity> resultData = gson.fromJson(result, type);
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
                        }
                        mXRecyclerView.setPullLoadMoreCompleted();
                    }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemEvent itemEvent) {
        if (itemEvent != null && itemEvent.getActivity() == ItemEvent.ACTIVITY.ACTIVITY_NEWS) {
            getData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
