package com.campuscard.app.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.ClassEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 楼栋
 */
public class BuildingDialog extends Dialog {

    private Context mContext;
    private View rootView;
    private WheelStringView mWheelView;
    private TextView btSure, btCancel;
    private OnClickDataListener onClickDataListener;

    public BuildingDialog(Context context, String campusName) {
        super(context, R.style.MyDialogStyle);
        mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_school, null);
        initView();
        setContentView(rootView);
        setData(campusName);
    }

    public void show() {
        super.show();
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = window.getAttributes();  //获取对话框当前的参数值
        p.width = d.getWidth();    //宽度设置为屏幕的0.8
        window.setAttributes(p);     //设置生效
    }

    /**
     * 初始化
     */
    private void initView() {
        mWheelView = (WheelStringView) rootView.findViewById(R.id.mWheelView);
        btSure = (TextView) rootView.findViewById(R.id.bt_sure);
        btCancel = (TextView) rootView.findViewById(R.id.bt_cancel);
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onClickDataListener.onSuccess();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onClickDataListener.onDismiss();
            }
        });
    }

    /**
     * 设置数据
     */
    private void setData(String campusName) {
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("campusName", campusName);
        HttpUtils.post(mContext, Constant.FOLLER, paramsMap, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<List<String>>>() {
                }.getType();
                ResultData<List<String>> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null && resultData.getData().size() > 0) {
                        mWheelView.refreshData((ArrayList<String>) resultData.getData());
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

    public String getWork() {
        if (mWheelView.getWork() == null) {
            return new String();
        } else {
            return mWheelView.getWork();
        }
    }

    public void setOnClickDataListener(OnClickDataListener onClickDataListener) {
        this.onClickDataListener = onClickDataListener;
    }

    /**
     * 点击回调
     */
    public interface OnClickDataListener {
        void onSuccess();

        void onDismiss();
    }
}
