package com.campuscard.app.ui.fragment.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.pager.XSlidingPlayView;
import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XDateUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseFragment;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.http.ResultPageData;
import com.campuscard.app.ui.activity.card.CardBillActivity;
import com.campuscard.app.ui.activity.home.LostAndFoundActivity;
import com.campuscard.app.ui.activity.home.NoticeActivity;
import com.campuscard.app.ui.activity.home.RechargeCardActivity;
import com.campuscard.app.ui.activity.home.RechargeElectricOneActivity;
import com.campuscard.app.ui.activity.my.BindCardActivity;
import com.campuscard.app.ui.entity.AdvEntity;
import com.campuscard.app.ui.entity.CardUserInfoEntity;
import com.campuscard.app.ui.entity.ItemEvent;
import com.campuscard.app.ui.entity.NoticeEntity;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.SharedCacheUtils;
import com.campuscard.app.utils.StringUtil;
import com.campuscard.app.utils.XImageUtils;
import com.campuscard.app.view.ScrollUpView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *    author : i??????
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : ??????
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    @ViewInject(R.id.tv_money)
    protected TextView tvMoney;
    @ViewInject(R.id.tv_danwei)
    private TextView tvDanWei;
    @ViewInject(R.id.bt_bind)
    protected TextView btBind;
    @ViewInject(R.id.tv_time)
    protected TextView tvTime;
    @ViewInject(R.id.lin_have_data)
    protected LinearLayout linHaveData;
    @ViewInject(R.id.mSlidingPlayView)
    protected XSlidingPlayView mXSlidingPlayView;
    @ViewInject(R.id.iv_resh)
    protected ImageView ivResh;
    @ViewInject(R.id.iv_close)
    protected ImageView ivClose;
    @ViewInject(R.id.bt_card_cz)
    protected TextView btCardCz;
    @ViewInject(R.id.bt_evelct_cz)
    protected TextView btEvelctCz;
    @ViewInject(R.id.bt_goudan_tj)
    protected TextView btGoudanTj;
    @ViewInject(R.id.bt_swzl)
    protected TextView btSwzl;
    @ViewInject(R.id.tv_info)
    private ScrollUpView marqueeView;
    @ViewInject(R.id.lin_notice)
    private LinearLayout linNotice;


    private boolean isShow = true;//????????????
    private CardUserInfoEntity cardUserInfoEntity;


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
        mXSlidingPlayView.startPlay();
        mXSlidingPlayView.setNavPageResources(R.drawable.circle_green_holl_adv, R.drawable.circle_gray_adv);
        mXSlidingPlayView.setNavHorizontalGravity(Gravity.CENTER);
        getBanner();
        laz();
    }

    @Override
    protected void lazyLoad() {
    }

    protected void laz() {
        ParamsMap params = new ParamsMap();
        params.put("pageNo", "1");
        params.put("pageSize", "4");
        HttpUtils.post(getActivity(), Constant.NOTICE_PAGE, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultPageData<NoticeEntity>>() {
                }.getType();
                ResultPageData<NoticeEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null && resultData.getData().size() > 0) {
                        //??????
                        initMarqueeView(resultData.getData(), marqueeView);
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
        getCardInfo();
    }

    @Event(value = {R.id.bt_bind, R.id.bt_card_cz, R.id.bt_evelct_cz, R.id.bt_goudan_tj, R.id.bt_swzl,
            R.id.bt_bind, R.id.lin_notice, R.id.iv_resh, R.id.iv_close}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_bind:
                //???????????????
                IntentUtil.redirectToNextActivity(getActivity(), BindCardActivity.class);
                break;
            case R.id.bt_card_cz:
                //???????????????
                IntentUtil.redirectToNextActivity(getActivity(), RechargeCardActivity.class);
                break;
            case R.id.bt_evelct_cz:
                //????????????
                IntentUtil.redirectToNextActivity(getActivity(), RechargeElectricOneActivity.class);
                break;
            case R.id.bt_goudan_tj:
                //????????????
                IntentUtil.redirectToNextActivity(getActivity(), CardBillActivity.class);
                break;
            case R.id.bt_swzl:
                //????????????
                IntentUtil.redirectToNextActivity(getActivity(), LostAndFoundActivity.class);
                break;
            case R.id.lin_notice:
                //??????
                IntentUtil.redirectToNextActivity(getActivity(), NoticeActivity.class);
                break;
            case R.id.iv_resh:
                //??????--???????????????
                getCardInfo();
                Animation circle_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_round_rotate);
                LinearInterpolator interpolator = new LinearInterpolator();  //????????????????????????xml??????????????????????????????
                circle_anim.setInterpolator(interpolator);
                if (circle_anim != null) {
                    ivResh.startAnimation(circle_anim);  //????????????
                }
                break;
            case R.id.iv_close:
                //????????????
                if (isShow) {
                    tvMoney.setText("****");
                    tvDanWei.setText("???");
                    ivClose.setImageResource(R.mipmap.ic_close);
                } else {
                    if (cardUserInfoEntity == null) {
                        return;
                    }
                    String money = StringUtil.douToString(cardUserInfoEntity.getBalance());
                    if (!TextUtils.isEmpty(money)) {
                        String[] moneyArrys = money.split("\\.");
                        if (moneyArrys.length > 0) {
                            tvMoney.setText(moneyArrys[0]);
                            tvDanWei.setText("." + moneyArrys[1] + "???");
                        }
                    }
                    ivClose.setImageResource(R.mipmap.ic_open);
                }
                isShow = !isShow;
                break;
        }
    }

    /**
     * ?????????????????????
     */
    private void getCardInfo() {
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
                        cardUserInfoEntity = resultData.getData();
                        EventBus.getDefault().postSticky(resultData.getData());
                        linHaveData.setVisibility(View.VISIBLE);
                        String money = StringUtil.douToString(cardUserInfoEntity.getBalance());
                        if (!TextUtils.isEmpty(money)) {
                            String[] moneyArrys = money.split("\\.");
                            if (moneyArrys.length > 0) {
                                tvMoney.setText(moneyArrys[0]);
                                tvDanWei.setText("." + moneyArrys[1] + "???");
                            }
                        }
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(XDateUtil.dateFormatYMDHMS);
                        Date date = new Date(System.currentTimeMillis());
                        tvTime.setText(simpleDateFormat.format(date));
                    }
                } else {
                    if (SharedCacheUtils.get(Constant.BIND_SHOW, true)) {
                        DialogUtils.bindCardShow(getActivity());
                    }
                    btBind.setVisibility(View.VISIBLE);
                    tvMoney.setText("????????????");
                    tvDanWei.setText("");
                    linHaveData.setVisibility(View.GONE);
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

    //?????????
    private void getBanner() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.ADV);
        HttpUtils.post(getActivity(), httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<List<AdvEntity>>>() {
                }.getType();
                ResultData<List<AdvEntity>> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null && resultData.getData().size() > 0) {
                        mXSlidingPlayView.removeAllViews();
                        for (int i = 0; i < resultData.getData().size(); i++) {
                            addImage(resultData.getData().get(i).getImgUrl());
                        }
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

    /**
     * ?????????
     */
    private void addImage(final String imgUrl) {
        ImageView imageView = new ImageView(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        XImageUtils.load(getActivity(), imgUrl, imageView);
        mXSlidingPlayView.addView(imageView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemEvent itemEvent) {
        if (itemEvent != null) {
            if (itemEvent.getActivity() == ItemEvent.ACTIVITY.ACTIVITY_UNBIND) {
                getCardInfo();
            }
        }
    }

    /**
     * ????????????????????????View
     * ??????????????????????????????View?????????????????????????????????????????????
     * ???:?????????????????????,?????????????????????????????????
     */
    private void initMarqueeView(final List<NoticeEntity> infos, ScrollUpView marqueeView) {
        List<View> mListNewsView = new ArrayList<>();
        for (int i = 0; i < infos.size(); i = i + 2) {
            //???????????????????????????
            View moreView = LayoutInflater.from(getActivity()).inflate(R.layout.item_scroll_up_view, null);
            //????????????????????????
            TextView tv1Content = (TextView) moreView.findViewById(R.id.content);
            TextView tv2Content = (TextView) moreView.findViewById(R.id.content_two);

            // tv1 ????????????-?????????????????????1 3 5 7 9
            tv1Content.setText(infos.get(i).getTitle());
            // tv2 ????????????,?????????tv????????????????????????????????? 2 4 6 8 10
            if (infos.size() > i + 1) {
                tv2Content.setText(infos.get(i + 1).getTitle());
                moreView.findViewById(R.id.lin_two).setVisibility(View.VISIBLE);
            } else {
                // ???????????????????????????,??????????????????????????????
                moreView.findViewById(R.id.lin_two).setVisibility(View.GONE);
            }
            //????????????
            marqueeView.setOnItemClickListener(new ScrollUpView.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    IntentUtil.redirectToNextActivity(getActivity(), NoticeActivity.class);
                }
            });
            // ???????????????????????????,?????????????????????????????? View????????????View??????
            mListNewsView.add(moreView);
            marqueeView.setViews(mListNewsView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCardInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
