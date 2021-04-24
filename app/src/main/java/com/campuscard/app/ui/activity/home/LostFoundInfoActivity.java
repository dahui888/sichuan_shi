package com.campuscard.app.ui.activity.home;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.DataFactory;
import com.campuscard.app.ui.entity.DeleteSuccessEntity;
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.LostFoundEntity;
import com.campuscard.app.ui.holder.LostFoundImgInfoAdapter;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.TelUtils;
import com.campuscard.app.utils.XImageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;

/**
 * 失物招领详情
 */
@ContentView(R.layout.activity_lost_found_info)
public class LostFoundInfoActivity extends BaseActivity {

    protected ImageView civHead;
    protected TextView tvName;
    protected TextView tvTime;
    protected ImageView ivLost;
    protected TextView tvPhone;
    protected TextView tvAddress;
    protected TextView tvLook;
    protected TextView tvContent;
    protected RecyclerView mXRecyclerView;
    protected TextView tvSc;
    protected TextView tvBj;
    @ViewInject(R.id.lin_del_edit)
    private LinearLayout linDelEdit;
    private String id, type;

    private LostFoundEntity lostFoundEntity;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        civHead = (ImageView) findViewById(R.id.civ_head);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        ivLost = (ImageView) findViewById(R.id.iv_lost);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvLook = (TextView) findViewById(R.id.tv_look);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvSc = (TextView) findViewById(R.id.tv_sc);
        tvBj = (TextView) findViewById(R.id.tv_bj);
        mXRecyclerView = (RecyclerView) findViewById(R.id.mXRecyclerView);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (TextUtils.isEmpty(type)) {
            linDelEdit.setVisibility(View.GONE);
        } else {
            linDelEdit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getData() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.SWZL_INFO + id);
        HttpUtils.get(this, httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<LostFoundEntity>>() {
                }.getType();
                ResultData<LostFoundEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        lostFoundEntity = resultData.getData();
                        XImageUtils.loadCircle(LostFoundInfoActivity.this, resultData.getData().getHeadPortrait(), civHead, R.mipmap.ic_head);
                        tvName.setText(resultData.getData().getUserName());
                        tvContent.setText(resultData.getData().getContent());
                        tvPhone.setText(resultData.getData().getContactPhoneNo());
                        tvAddress.setText(resultData.getData().getAddress());
                        tvLook.setText(resultData.getData().getViewCount());
                        if (resultData.getData().getLostFoundType().equals("LOST")) {
                            ivLost.setImageResource(R.mipmap.ic_lost);
                        } else {
                            ivLost.setImageResource(R.mipmap.ic_found);
                        }
                        //图片
                        LostFoundImgInfoAdapter contentImageAdapter = new LostFoundImgInfoAdapter(LostFoundInfoActivity.this, resultData.getData().getLostAndFoundPictureVOS());
                        mXRecyclerView.setAdapter(contentImageAdapter);
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

    @Event(type = View.OnClickListener.class, value = {R.id.tv_sc, R.id.tv_bj, R.id.tv_phone})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sc://删除
                DialogUtils.showCenterDialog(this, getString(R.string.sure_delete_info), new DialogUtils.OnResult() {
                    @Override
                    public void onResult(String code) {
                        DataFactory.deleteRelease(LostFoundInfoActivity.this, id, new DataFactory.OnResult() {
                            @Override
                            public void onSuccess() {
                                XToastUtil.showToast(LostFoundInfoActivity.this, "删除成功");
                                //通知界面刷新
                                EventBus.getDefault().post(new DeleteSuccessEntity());
                                finish();
                            }
                        });
                    }
                });
                break;
            case R.id.tv_bj://编辑
                if (lostFoundEntity != null) {
                    EventBus.getDefault().postSticky(lostFoundEntity);
                    IntentUtil.redirectToNextActivity(this, ReleaseActivity.class);
                }
                break;
            case R.id.tv_phone:
                if (lostFoundEntity != null && !TextUtils.isEmpty(lostFoundEntity.getContactPhoneNo())) {
                    TelUtils.callPhoneDialog(this, lostFoundEntity.getContactPhoneNo());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new ItemEvent(ItemEvent.ACTIVITY.ACTIVITY_NEWS));
    }
}
