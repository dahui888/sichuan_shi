package com.campuscard.app.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.base.frame.weigt.recycle.XRecyclerView;
import com.base.frame.weigt.recycle.XRefreshLayout;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseFragment;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultPageData;
import com.campuscard.app.ui.entity.DeleteSuccessEntity;
import com.campuscard.app.ui.entity.LostFoundEntity;
import com.campuscard.app.ui.holder.LostAndFoundHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 招领
 */
@ContentView(R.layout.fragment_list)
public class FoundFragment extends BaseFragment implements XRefreshLayout.PullLoadMoreListener {
    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    private int page = 1;

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
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        mXRecyclerView.getAdapter().bindHolder(new LostAndFoundHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);
    }

    @Override
    protected void lazyLoad() {
        getData();
    }

    public void getData() {
        ParamsMap params = new ParamsMap();
        params.put("pageNo", page);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("lostFoundType", "FOUND");
        HttpUtils.post(getActivity(), Constant.MY_RELEASE, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultPageData<LostFoundEntity>>() {
                }.getType();
                ResultPageData<LostFoundEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null && resultData.getData().size() > 0) {
                        if (page == 1) {
                            mXRecyclerView.getAdapter().setData(0, resultData.getData());
                        } else {
                            mXRecyclerView.getAdapter().addDataAll(0, resultData.getData());
                        }
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
        lazyLoad();
    }

    @Override
    public boolean onLoadMore() {
        page++;
        lazyLoad();
        return true;
    }

    //删除成功后刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeleteSuccessEntity message) {
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
