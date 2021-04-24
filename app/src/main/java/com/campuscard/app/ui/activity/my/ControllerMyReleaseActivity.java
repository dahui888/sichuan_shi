package com.campuscard.app.ui.activity.my;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.weigt.recycle.XRecyclerView;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.ui.holder.ControllerMyReleaseHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


/*
 *
 * 管理我的发布
 * */
@ContentView(R.layout.activity_controller_my_release)
public class ControllerMyReleaseActivity extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.mXRecyclerView)
    XRecyclerView mXRecyclerView;
    @ViewInject(R.id.btn_right)
    TextView btnRight;
    @ViewInject(R.id.iv_quanxuan)
    ImageView ivQuanxuan;
    @ViewInject(R.id.tv_shanchu)
    TextView tvShanchu;
    @ViewInject(R.id.iv_release)
    ImageView ivRelease;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);

        mXRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        ControllerMyReleaseHolder holder = new ControllerMyReleaseHolder();
        mXRecyclerView.getAdapter().bindHolder(holder);
    }

    @Override
    public void getData() {

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }


}