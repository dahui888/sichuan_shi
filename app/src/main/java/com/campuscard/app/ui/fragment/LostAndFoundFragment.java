package com.campuscard.app.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

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
import com.campuscard.app.ui.entity.DeleteSuccessEntity;
import com.campuscard.app.ui.entity.ItemEvent;
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
 * 失物招领
 */
@ContentView(R.layout.fragment_list)
public class LostAndFoundFragment extends BaseFragment implements XRefreshLayout.PullLoadMoreListener {
    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.lin_data)
    private LinearLayout lin_data;
    private int page = 1, totalPage = 0;
    private String id, lostFoundType = "LOST", keyWorld = "", pageType = "NORMAL_PAGE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
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
        ParamsMap params = new ParamsMap();
        params.put("pageNo", page);
        params.put("pageSize", Constant.PAGE_SIZE);
        if (!TextUtils.isEmpty(keyWorld)) {
            params.put("keyWorld ", keyWorld);
        }
        if (!TextUtils.isEmpty(id)) {
            params.put("lostFoundThingsTypeInfoId", id);
        }
        params.put("lostFoundType", lostFoundType);
        params.put("pageType", pageType);
        HttpUtils.post(getActivity(), Constant.SWZL, params, new HttpResponseCallBack() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(String code) {
        if (!TextUtils.isEmpty(code)) {
            if (TextUtils.equals("1", code)) {
                lostFoundType = "LOST";
            } else if (TextUtils.equals("2", code)) {
                lostFoundType = "FOUND";
            } else {
                keyWorld = code;
            }
            lazyLoad();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemEvent itemEvent) {
        if (itemEvent != null && itemEvent.getActivity() == ItemEvent.ACTIVITY.ACTIVITY_NEWS) {
            lazyLoad();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeleteSuccessEntity deleteSuccessEntity) {
        if (deleteSuccessEntity != null) {
            lazyLoad();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        lazyLoad();
    }

    @Override
    public boolean onLoadMore() {
        if (page < totalPage) {
            page++;
            lazyLoad();
            return true;
        } else {
            XToastUtil.showToast(getActivity(), getString(R.string.no_data));
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(getActivity())) {
            EventBus.getDefault().unregister(this);
        }
    }
}
