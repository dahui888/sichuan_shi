package com.campuscard.app.ui.fragment.main;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.SystemBarTintManager;
import com.base.frame.weigt.recycle.XRecyclerView;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseFragment;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.http.ResultPageData;
import com.campuscard.app.ui.activity.card.BuZhuActivity;
import com.campuscard.app.ui.activity.card.CardCostRecordActivity;
import com.campuscard.app.ui.activity.card.CardLossActivity;
import com.campuscard.app.ui.activity.card.DepositCircleActivity;
import com.campuscard.app.ui.activity.card.PickUpCardActivity;
import com.campuscard.app.ui.activity.card.SaveMoneyRecordActivity;
import com.campuscard.app.ui.entity.CardCostEntity;
import com.campuscard.app.ui.entity.CardUserInfoEntity;
import com.campuscard.app.ui.holder.CardHolder;
import com.campuscard.app.utils.DialogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 校园卡
 */
@ContentView(R.layout.fragment_card)
public class CardFragment extends BaseFragment {

    @ViewInject(R.id.tv_name)
    protected TextView tvName;
    @ViewInject(R.id.tv_state)
    private TextView tvState;
    @ViewInject(R.id.tv_xuehao)
    protected TextView tvXuehao;
    @ViewInject(R.id.tv_from_info)
    protected TextView tvFromInfo;
    @ViewInject(R.id.re_bg)
    private RelativeLayout reBg;
    @ViewInject(R.id.bt_ck)
    protected TextView btCk;
    @ViewInject(R.id.bt_qc)
    protected TextView btQc;
    @ViewInject(R.id.bt_loss)
    protected TextView btLoss;
    @ViewInject(R.id.bt_bz)
    protected TextView btBz;
    @ViewInject(R.id.bt_jk)
    private TextView btJk;
    @ViewInject(R.id.bt_more)
    protected RelativeLayout btMore;
    @ViewInject(R.id.mListView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.statusBar)
    private View statusBar;
    @ViewInject(R.id.tv_no_data)
    private TextView linearLayout;
    private int page = 1;
    private boolean isBind;
    private double money;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager XSystemBarTintManager = new SystemBarTintManager(getActivity());
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            layoutParams.height = XSystemBarTintManager.getStatusBarHeight(getActivity());
            statusBar.setLayoutParams(layoutParams);
            statusBar.setBackgroundColor(getResources().getColor(R.color.trance));
        }
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        mXRecyclerView.getAdapter().bindHolder(new CardHolder());
        mXRecyclerView.getRecyclerView().setNestedScrollingEnabled(false);
        initView();
    }

    @Override
    protected void lazyLoad() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.CARD_USE_INFO);
        HttpUtils.get(getActivity(), httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<CardUserInfoEntity>>() {
                }.getType();
                ResultData<CardUserInfoEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        isBind = true;
                        money = resultData.getData().getBalance();
                        if (!TextUtils.isEmpty(resultData.getData().getStatus())) {
                            switch (resultData.getData().getStatus()) {
                                //校园卡状态 ----NORMAL：正常 1； LOSS,挂失 3；FROZEN, 冻结 4；GRAY, 灰色 5；DISCONTINUATION,停用 6 ；NOCARD,未发卡 99
                                case "NORMAL":
                                    tvName.setText(resultData.getData().getName());
                                    tvXuehao.setText("学号：" + resultData.getData().getEcardNo());
                                    tvFromInfo.setText("学院：" + resultData.getData().getAcademy() + "     " + "专业：" + resultData.getData().getMajor());
                                    reBg.setBackgroundResource(R.mipmap.ic_bg_card);
                                    tvState.setText("正常");
                                    tvState.setBackgroundResource(R.drawable.bg_orange_fillet);
                                    tvState.setVisibility(View.VISIBLE);
                                    break;
                                case "LOSS":
                                    tvState.setBackgroundResource(R.drawable.bg_gray_fillet5);
                                    tvState.setText("已挂失");
                                    tvName.setText(resultData.getData().getName());
                                    tvXuehao.setText("学号：" + resultData.getData().getEcardNo());
                                    tvFromInfo.setText("学院：" + resultData.getData().getAcademy() + "     " + "专业：" + resultData.getData().getMajor());
                                    reBg.setBackgroundResource(R.mipmap.ic_bg_card_f);
                                    tvState.setVisibility(View.VISIBLE);
                                    break;
                                case "FROZEN":
                                    tvState.setBackgroundResource(R.drawable.bg_gray_fillet5);
                                    tvState.setText("冻结");
                                    tvName.setText(resultData.getData().getName());
                                    tvXuehao.setText("学号：" + resultData.getData().getEcardNo());
                                    tvFromInfo.setText("学院：" + resultData.getData().getAcademy() + "     " + "专业：" + resultData.getData().getMajor());
                                    reBg.setBackgroundResource(R.mipmap.ic_bg_card_f);
                                    tvState.setVisibility(View.VISIBLE);
                                    break;
                                case "GRAY":
                                    break;
                                case "DISCONTINUATION":
                                    tvState.setBackgroundResource(R.drawable.bg_gray_fillet5);
                                    tvState.setText("停用");
                                    tvName.setText(resultData.getData().getName());
                                    tvXuehao.setText("学号：" + resultData.getData().getEcardNo());
                                    tvFromInfo.setText("学院：" + resultData.getData().getAcademy() + "     " + "专业：" + resultData.getData().getMajor());
                                    reBg.setBackgroundResource(R.mipmap.ic_bg_card_f);
                                    tvState.setVisibility(View.VISIBLE);
                                    break;
                                case "NOCARD":
                                    //未发卡
                                    tvName.setText("未发卡");
                                    tvFromInfo.setText("学院：" + "\t\t\t\t" + "专业：");
                                    reBg.setBackgroundResource(R.mipmap.ic_bg_card);
                                    tvState.setVisibility(View.GONE);
                                    break;
                            }
                        }
                    }
                } else {
                    initView();
                    isBind = false;
                }
            }
            @Override
            public void onFailed(int code, String failedMsg) {
            }
            @Override
            public void onFinished() {
            }
        });

        //消费记录
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("pageNo", page);
        paramsMap.put("pageSize", "4");
        HttpUtils.post(getActivity(), Constant.CARD_RECODE, paramsMap, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultPageData<CardCostEntity>>() {
                }.getType();
                ResultPageData<CardCostEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        if (resultData.getData().size() > 0) {
                            mXRecyclerView.getAdapter().setData(0, resultData.getData());
                            linearLayout.setVisibility(View.GONE);
                            mXRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                            mXRecyclerView.setVisibility(View.GONE);
                        }
                    } else {
                        linearLayout.setVisibility(View.VISIBLE);
                        mXRecyclerView.setVisibility(View.GONE);
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

    @Event(value = {R.id.bt_ck, R.id.bt_qc, R.id.bt_loss, R.id.bt_bz, R.id.bt_more, R.id.bt_jk}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_ck:
                //存款
                Bundle bundle = new Bundle();
                bundle.putDouble("money", money);
                IntentUtil.redirectToNextActivity(getActivity(), SaveMoneyRecordActivity.class, bundle);
                break;
            case R.id.bt_loss:
                //挂失
                if (isBind) {
                    IntentUtil.redirectToNextActivity(getActivity(), CardLossActivity.class);
                } else {
                    DialogUtils.bindCardShow(getActivity());
                }
                break;
            case R.id.bt_qc:
                //圈存
                IntentUtil.redirectToNextActivity(getActivity(), DepositCircleActivity.class);
                break;
            case R.id.bt_bz:
                //补助
                IntentUtil.redirectToNextActivity(getActivity(), BuZhuActivity.class);
                break;
            case R.id.bt_jk:
                //捡卡
                IntentUtil.redirectToNextActivity(getActivity(), PickUpCardActivity.class);
                break;
            case R.id.bt_more:
                //查看更多--消费记录
                IntentUtil.redirectToNextActivity(getActivity(), CardCostRecordActivity.class);
                break;
        }
    }

    private void initView() {
        tvName.setText("未绑卡");
        tvXuehao.setText("学号：");
        tvFromInfo.setText("学院：" + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "专业：");
        tvState.setVisibility(View.GONE);
        reBg.setBackgroundResource(R.mipmap.ic_bg_card);
    }

}
