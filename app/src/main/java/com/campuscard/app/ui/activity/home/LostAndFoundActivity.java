package com.campuscard.app.ui.activity.home;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.frame.pager.XViewPager;
import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.SystemBarTintManager;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.ClassEntity;
import com.campuscard.app.ui.fragment.LostAndFoundFragment;
import com.campuscard.app.ui.holder.MomentListAdapter;
import com.campuscard.app.utils.DialogUtils;
import com.campuscard.app.utils.PopuDimssCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 失物招领
 */
@ContentView(R.layout.activity_lost_found)
public class LostAndFoundActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.tv_title)
    protected TextView tvTitle;
    @ViewInject(R.id.iv_title)
    private ImageView ivTitle;
    @ViewInject(R.id.lin_title)
    private LinearLayout linTitle;
    @ViewInject(R.id.btn_right)
    protected ImageView btnRight;
    @ViewInject(R.id.re_search)
    protected RelativeLayout reSearch;
    @ViewInject(R.id.mXViewPager)
    protected XViewPager mXViewPager;
    @ViewInject(R.id.statusBar)
    private View statusBar;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private List<String> titles = new ArrayList<String>();
    private String typeTag = "1";

    @ViewInject(R.id.lin_search)
    private LinearLayout linSearch;
    @ViewInject(R.id.et_search)
    private EditText etSearch;
    @ViewInject(R.id.bg_view)
    private View bgView;

    @ViewInject(R.id.rgChannel)
    private RadioGroup rgChannel = null;
    @ViewInject(R.id.hvChannel)
    private HorizontalScrollView hvChannel;
    int i = 1;//表示箭头的显示，1为灰色向下，2绿色向上

    @Override
    public void initView() {
        tvTitle.setText("失物");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            layoutParams.height = systemBarTintManager.getStatusBarHeight(this);
            statusBar.setLayoutParams(layoutParams);
            statusBar.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void getData() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.WUPIN_CLASS);
        HttpUtils.get(this, httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<List<ClassEntity>>>() {
                }.getType();
                ResultData<List<ClassEntity>> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null && resultData.getData().size() > 0) {

                        ClassEntity bean = new ClassEntity();
                        bean.setId("");
                        bean.setName("全部");
                        resultData.getData().add(0, bean);
                        for (int i = 0; i < resultData.getData().size(); i++) {
                            final RadioButton rb = (RadioButton) LayoutInflater.from(LostAndFoundActivity.this).
                                    inflate(R.layout.tab_rb, null);
                            rb.setId(i);
                            rb.setText(resultData.getData().get(i).getName());
                            RadioGroup.LayoutParams params = new
                                    RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                                    RadioGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(15, 0, 15, 0);
                            rgChannel.addView(rb, params);
                        }
                        for (int i = 0; i < resultData.getData().size(); i++) {
                            LostAndFoundFragment lostAndFoundFragment = new LostAndFoundFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("id", resultData.getData().get(i).getId());
                            lostAndFoundFragment.setArguments(bundle);
                            fragmentList.add(lostAndFoundFragment);
                            titles.add(resultData.getData().get(i).getName());
                        }
                        mXViewPager.setAdapter(new MomentListAdapter(getSupportFragmentManager(), fragmentList));
                        rgChannel.check(0);
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
        rgChannel.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        mXViewPager.setCurrentItem(checkedId);
                    }
                });
        mXViewPager.setOnPageChangeListener(this);
    }

    @Event(value = {R.id.btn_right, R.id.iv_back, R.id.lin_title, R.id.re_search, R.id.bg_view}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.btn_right:
                //发布
                IntentUtil.redirectToNextActivity(this, ReleaseActivity.class);
                break;
            case R.id.lin_title:
                //标题筛选
                ivTitle.setImageResource(R.mipmap.ic_on);
                DialogUtils.filterShow(this, linTitle, typeTag, new DialogUtils.OnResult() {
                    @Override
                    public void onResult(String code) {
                        if (code.equals("1")) {
                            tvTitle.setText("失物");
                            ivTitle.setImageResource(R.mipmap.ic_on);
                            typeTag = "1";
                        } else {
                            tvTitle.setText("招领");
                            ivTitle.setImageResource(R.mipmap.ic_on);
                            typeTag = "2";
                        }
                        ivTitle.setImageResource(R.mipmap.ic_up);
                        EventBus.getDefault().post(code);
                    }
                }, new PopuDimssCallBack() {
                    @Override
                    public void callback() {
                        ivTitle.setImageResource(R.mipmap.ic_up);
                    }
                });
                break;
            case R.id.re_search:
                //搜索
                IntentUtil.redirectToNextActivity(this, SearchResultActivity.class);
                break;
        }
    }

    /**
     * 滑动ViewPager时调整ScroollView的位置以便显示按钮
     *
     * @param idx
     */
    private void setTab(int idx) {
        final RadioButton rb = (RadioButton) rgChannel.getChildAt(idx);
        rb.setChecked(true);
        int left = rb.getLeft();
        int width = rb.getMeasuredWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int len = left + width / 2 - screenWidth / 2;
        hvChannel.smoothScrollTo(len, 0);
        // 滑动ScroollView
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
