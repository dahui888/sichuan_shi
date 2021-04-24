package com.campuscard.app.ui.activity.my;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
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
import com.campuscard.app.ui.DataFactory;
import com.campuscard.app.ui.activity.home.ReleaseActivity;
import com.campuscard.app.ui.entity.DeleteSuccessEntity;
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.LostFoundEntity;
import com.campuscard.app.ui.holder.LostAndFoundReleaseHolder;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.PageNumUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的发布
 */
@ContentView(R.layout.activity_my_release)
public class MyReleaseActivity extends BaseActivity implements XRefreshLayout.PullLoadMoreListener {

    @ViewInject(R.id.btn_right)
    protected TextView btnRight;
    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.iv_release)
    protected ImageView ivRelease;
    @ViewInject(R.id.tv_lost)
    protected TextView tvLost;
    @ViewInject(R.id.iv_lost)
    protected ImageView ivLost;
    @ViewInject(R.id.bt_lost)
    protected RelativeLayout btLost;
    @ViewInject(R.id.tv_found)
    protected TextView tvFound;
    @ViewInject(R.id.iv_found)
    protected ImageView ivFound;
    @ViewInject(R.id.bt_found)
    protected RelativeLayout btFound;
    @ViewInject(R.id.bt_del)
    protected TextView btDel;
    @ViewInject(R.id.bt_bottom)
    private LinearLayout btBottom;
    @ViewInject(R.id.lin_data)
    private LinearLayout linearLayout;
    @ViewInject(R.id.mCheckBox)
    private CheckBox mCheckBox;
    private String status = "LOST";//FOUND
    private boolean isEdit = true;//编辑/完成
    private int page = 1, totalPage;
    private List<LostFoundEntity> lostFoundEntityList;
    private int num = 0;

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        btnRight.setText("管理");
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new LostAndFoundReleaseHolder());
        mXRecyclerView.setOnPullLoadMoreListener(this);
        titleSelected(0);
        toolbar.setNavigationIcon(null);
    }

    @Override
    public void getData() {
        ParamsMap params = new ParamsMap();
        params.put("pageNo", page);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("lostFoundType", status);
        HttpUtils.post(this, Constant.MY_RELEASE, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultPageData<LostFoundEntity>>() {
                }.getType();
                ResultPageData<LostFoundEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    totalPage = PageNumUtils.getAllPageCount(resultData.getDetail().getTotalCount(), Constant.PAGE_SIZE);
                    if (resultData.getData() != null && resultData.getData().size() > 0) {
                        lostFoundEntityList = resultData.getData();
                        linearLayout.setVisibility(View.GONE);
                        mXRecyclerView.setVisibility(View.VISIBLE);
                        if (page == 1) {
                            mXRecyclerView.getAdapter().setData(0, resultData.getData());
                        } else {
                            mXRecyclerView.getAdapter().addDataAll(0, resultData.getData());
                        }
                    } else {
                        mXRecyclerView.setVisibility(View.GONE);
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

    @SuppressLint("StringFormatInvalid")
    @Event(value = {R.id.btn_right, R.id.iv_release, R.id.mCheckBox, R.id.bt_del, R.id.bt_lost, R.id.bt_found}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_lost:
                //失物
                titleSelected(0);
                status = "LOST";
                getData();
                break;
            case R.id.bt_found:
                //招领
                titleSelected(1);
                status = "FOUND";
                getData();
                break;
            case R.id.btn_right:
                //管理
                if (isEdit) {
                    btnRight.setText("完成");
                    btBottom.setVisibility(View.VISIBLE);
                    editAndComplete(true);
                    ivRelease.setVisibility(View.GONE);
                } else {
                    btnRight.setText("编辑");
                    btBottom.setVisibility(View.GONE);
                    editAndComplete(false);
                    ivRelease.setVisibility(View.VISIBLE);
                }
                isEdit = !isEdit;
                break;
            case R.id.iv_release:
                //发布
                IntentUtil.redirectToNextActivity(this, ReleaseActivity.class);
                break;
            case R.id.mCheckBox:
                //全选
                choiceAll();
                break;
            case R.id.bt_del:
                //删除
                final List<String> stringList = new ArrayList<>();
                for (int i = 0; i < mXRecyclerView.getAdapter().getData(0).size(); i++) {
                    LostFoundEntity collectEntity = (LostFoundEntity) mXRecyclerView.getAdapter().getData(0).get(i);
                    if (collectEntity.isSelect()) {
                        stringList.add(collectEntity.getId());
                    }
                }
                if (stringList != null && stringList.size() > 0) {
                    //删除收藏
                    DialogUtils.showCenterDialog(MyReleaseActivity.this, getString(R.string.sure_delete_info), new DialogUtils.OnResult() {
                        @Override
                        public void onResult(String code) {
                            DataFactory.deleteRelease(MyReleaseActivity.this, listToString(stringList), new DataFactory.OnResult() {
                                @Override
                                public void onSuccess() {
                                    XToastUtil.showToast(MyReleaseActivity.this, "删除成功");
                                    getData();
                                }
                            });
                        }
                    });
                    //全选时候--删除完所有的商品--重置状态
                    if (mCheckBox.isChecked()) {
                        mCheckBox.setChecked(false);
                        btBottom.setVisibility(View.GONE);
                        isEdit = false;
                        btnRight.setText("编辑");
                    }
                } else {
                    XToastUtil.showToast(this, getString(R.string.choice_goods));
                }
                break;
        }
    }

    //全选
    private void choiceAll() {
        List<LostFoundEntity> dataBeen = lostFoundEntityList;
        if (dataBeen != null) {
            for (int i = 0; i < dataBeen.size(); i++) {
                dataBeen.get(i).setSelect(mCheckBox.isChecked());
                dataBeen.get(i).setShowCheck(true);
            }
            mXRecyclerView.getAdapter().notifyDataSetChanged();
            num = 0;
        }
    }

    //编辑--完成时候展示
    private void editAndComplete(boolean isEdit) {
        List<LostFoundEntity> dataBeen = lostFoundEntityList;
        if (dataBeen != null) {
            for (int i = 0; i < dataBeen.size(); i++) {
                dataBeen.get(i).setShowCheck(isEdit);
            }
            mXRecyclerView.getAdapter().notifyDataSetChanged();
            num = 0;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LostFoundEntity lostFoundEntity) {
        if (lostFoundEntity != null) {
            List<LostFoundEntity> datas = lostFoundEntityList;
            if (null == datas)
                return;
            for (int i = 0; i < datas.size(); i++) {
                LostFoundEntity item = datas.get(i);
                if (item.isSelect()) {
                    num++;
                }
            }
            if (lostFoundEntityList.size() > 0 && num == lostFoundEntityList.size()) {
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeleteSuccessEntity itemEvent) {
        if (itemEvent != null) {
            getData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemEvent itemEvent) {
        if (itemEvent != null && itemEvent.getActivity() == ItemEvent.ACTIVITY.ACTIVITY_NEWS) {
            getData();
        }
    }

    /**
     * 逗号分隔
     */
    private String listToString(List<String> stringList) {
        StringBuilder sb = new StringBuilder();
        if (stringList != null && stringList.size() > 0) {
            for (int i = 0; i < stringList.size(); i++) {
                sb.append(stringList.get(i).toString()).append(",");
            }
            return sb.toString().substring(0, sb.toString().length() - 1);
        } else {
            return "";
        }
    }


    //页面数据选择
    private void titleSelected(int index) {
        ivLost.setVisibility(View.INVISIBLE);
        ivFound.setVisibility(View.INVISIBLE);
        tvLost.setTextColor(getResources().getColor(R.color.black));
        tvFound.setTextColor(getResources().getColor(R.color.black));
        switch (index) {
            case 0:
                ivLost.setVisibility(View.VISIBLE);
                tvLost.setTextColor(getResources().getColor(R.color.color_00b464));
                tvFound.setTextColor(getResources().getColor(R.color.black));
                tvLost.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvFound.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case 1:
                ivFound.setVisibility(View.VISIBLE);
                tvFound.setTextColor(getResources().getColor(R.color.color_00b464));
                tvLost.setTextColor(getResources().getColor(R.color.black));
                tvFound.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvLost.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
