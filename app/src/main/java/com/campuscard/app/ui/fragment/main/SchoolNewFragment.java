package com.campuscard.app.ui.fragment.main;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.base.frame.utils.SystemBarTintManager;
import com.base.frame.utils.XToastUtil;
import com.base.frame.weigt.recycle.XRecyclerView;
import com.base.frame.weigt.recycle.XRefreshLayout;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseFragment;
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
 * 校园新闻
 */
@ContentView(R.layout.fragment_news)
public class SchoolNewFragment extends BaseFragment implements XRefreshLayout.PullLoadMoreListener {

    @ViewInject(R.id.mXRecyclerView)
    private XRecyclerView mXRecyclerView;
    @ViewInject(R.id.statusBar)
    private View statusBar;
    private int page = 1, totalPage;
    @ViewInject(R.id.lin_data)
    private LinearLayout linData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager XSystemBarTintManager = new SystemBarTintManager(getActivity());
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            layoutParams.height = XSystemBarTintManager.getStatusBarHeight(getActivity());
            statusBar.setLayoutParams(layoutParams);
            statusBar.setBackgroundColor(getResources().getColor(R.color.white));
        }
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        mXRecyclerView.getAdapter().bindHolder(new NoticeHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);
        load();
    }

    @Override
    protected void lazyLoad() {
    }

    protected void load() {
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("pageNo", page);
        paramsMap.put("pageSize", Constant.PAGE_SIZE);
        HttpUtils.post(getActivity(), Constant.NOTICE_PAGE, paramsMap, new HttpResponseCallBack() {
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
                            linData.setVisibility(View.GONE);
                            if (page == 1) {
                                mXRecyclerView.getAdapter().setData(0, resultData.getData());
                            } else {
                                mXRecyclerView.getAdapter().addDataAll(0, resultData.getData());
                            }
                        } else {
                            linData.setVisibility(View.VISIBLE);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemEvent itemEvent) {
        if (itemEvent != null && itemEvent.getActivity() == ItemEvent.ACTIVITY.ACTIVITY_NEWS) {
            load();
        }
    }


    @Override
    public void onRefresh() {
        page = 1;
        load();
    }

    @Override
    public boolean onLoadMore() {
        if (page < totalPage) {
            page++;
            load();
            return true;
        } else {
            XToastUtil.showToast(getActivity(), getString(R.string.no_data));
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
