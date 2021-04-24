package com.campuscard.app.ui.activity.my;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.utils.IntentUtil;
import com.base.frame.weigt.recycle.XRecyclerView;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.QuestionEntity;
import com.campuscard.app.ui.holder.OperationGuideHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.List;

/*
 * 帮助与反馈
 * */
@ContentView(R.layout.activity_help_feedback)
public class HelpFeedbackActivity extends BaseActivity {

    @ViewInject(R.id.btn_right)
    protected TextView btnRight;
    @ViewInject(R.id.mXRecyclerView)
    protected XRecyclerView mXRecyclerView;
    @ViewInject(R.id.lin_data)
    private LinearLayout linearLayout;
    @ViewInject(R.id.lin)
    private LinearLayout lin;

    @Override
    public void initView() {
        btnRight.setText("去反馈");
        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.getAdapter().bindHolder(new OperationGuideHolder());
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.QUESTION_LIST);
        HttpUtils.get(this, httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<List<QuestionEntity>>>() {
                }.getType();
                ResultData<List<QuestionEntity>> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null && resultData.getData().size() > 0) {
                        mXRecyclerView.getAdapter().setData(0, resultData.getData());
                        linearLayout.setVisibility(View.GONE);
                        lin.setVisibility(View.VISIBLE);
                    } else {
                        lin.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
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

    @Event(value = {R.id.btn_right}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                //去反馈
                IntentUtil.redirectToNextActivity(this, FeedbackActivity.class);
                break;
        }
    }
}