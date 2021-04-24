package com.campuscard.app.ui.activity.my;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
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
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.MsgEntity;
import com.campuscard.app.ui.holder.MsgHolder;
import com.campuscard.app.utils.NetUtil;
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
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 消息列表界面
 */
@ContentView(R.layout.activity_list)
public class MessageListActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener {

    @ViewInject(R.id.mXRecyclerView)
    private XRecyclerView mXRecyclerView;
    @ViewInject(R.id.lin_data)
    private LinearLayout linearLayout;
    private String type;
    private int page = 1, totalPage;
    @ViewInject(R.id.iv_img)
    private ImageView iv_img;
    @ViewInject(R.id.tv_ms)
    private TextView tv_ms;

    @Override
    public void initView() {
        type = getIntent().getStringExtra("title");
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new MsgHolder(type));
        mXRecyclerView.setOnPullLoadMoreListener(this);
        EventBus.getDefault().register(this);
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case "我的消息":
                    loadData("PERSONAL");
                    break;
                case "系统消息":
                    loadData("SYSTEM");
                    break;
                case "捡卡通知":
                    loadData("PICKUP_NOTICE");
                    break;
            }
            setTitle(type);
        }
    }

    /**
     * 获取数据
     *
     * @param urlType
     */
    private void loadData(String urlType) {
        if (!NetUtil.isNetworkAvalible(this)) {
            linearLayout.setVisibility(View.VISIBLE);
            iv_img.setImageResource(R.mipmap.ic_zwwl);
            tv_ms.setText("哎呀，网络竟然不稳定");
        } else {
            ParamsMap paramsMap = new ParamsMap();
            paramsMap.put("messageType", urlType);
            paramsMap.put("pageNo", page);
            paramsMap.put("pageSize", Constant.PAGE_SIZE);
            HttpUtils.post(this, Constant.GET_MSG_LIST, paramsMap, new HttpResponseCallBack() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResultPageData<MsgEntity>>() {
                    }.getType();
                    ResultPageData<MsgEntity> resultData = gson.fromJson(result, type);
                    if (resultData.getStatus() == 200) {
                        totalPage = PageNumUtils.getAllPageCount(resultData.getDetail().getTotalCount(), Constant.PAGE_SIZE);
                        if (resultData.getData() != null) {
                            if (resultData.getData().size() > 0) {
                                linearLayout.setVisibility(View.GONE);
                                if (page == 1) {
                                    mXRecyclerView.getAdapter().setData(0, resultData.getData());
                                } else {
                                    mXRecyclerView.getAdapter().addDataAll(0, resultData.getData());
                                }
                            }
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                            iv_img.setImageResource(R.mipmap.ic_zwxx);
                            tv_ms.setText("暂无消息");
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemEvent itemEvent) {
        if (itemEvent != null && itemEvent.getActivity() == ItemEvent.ACTIVITY.ACTIVITY_MSG_NUM) {
            getData();
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
