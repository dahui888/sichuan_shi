package com.campuscard.app.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
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
import com.campuscard.app.http.ParamsMap;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.PictureIdEntity;
import com.campuscard.app.ui.entity.UserInfoEntity;
import com.campuscard.app.utils.UserInfoUtils;
import com.campuscard.app.utils.XImageUtils;
import com.campuscard.app.view.XRoundImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 个人资料
 */
@ContentView(R.layout.activity_personal_data)
public class PersonalInfoActivity extends BaseActivity {
    @ViewInject(R.id.iv_head)
    protected XRoundImageView ivHead;
    @ViewInject(R.id.ll_head)
    protected LinearLayout llHead;
    @ViewInject(R.id.tv_selectname)
    protected TextView tvSelectname;
    @ViewInject(R.id.ll_name)
    protected LinearLayout llName;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);

    }

    @Override
    public void getData() {
        getPersonalData();
    }

    @Event(value = {R.id.ll_name, R.id.ll_head}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_name://修改姓名
                if (TextUtils.isEmpty(tvSelectname.getText().toString())) {
                    IntentUtil.redirectToNextActivity(this, ModifyNameActivity.class);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", tvSelectname.getText().toString());
                    IntentUtil.redirectToNextActivity(this, ModifyNameActivity.class, bundle);
                }
                break;
            case R.id.ll_head://修改头像
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_default_style)
                        .isCamera(true)//不显示拍照按钮
                        .maxSelectNum(1)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .previewImage(false)//是否预览
                        .enableCrop(true)// 是否裁剪
                        .compress(true)// 是否压缩
                        .cropWH(300, 300)
                        .circleDimmedLayer(true)
                        .showCropFrame(false)// 圆形裁剪时建议设为false
                        .showCropGrid(false)
                        .withAspectRatio(5, 5)
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .rotateEnabled(false) // 裁剪是否可旋转图片
                        .scaleEnabled(false)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
        }
    }

    //获取个人数据
    public void getPersonalData() {
        HttpRequestParams httpRequestParams = new HttpRequestParams(Constant.GETPERSONAL);
        HttpUtils.get(this, httpRequestParams, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<UserInfoEntity>>() {
                }.getType();
                ResultData<UserInfoEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    if (resultData.getData() != null) {
                        UserInfoUtils.cacheUserInfo(resultData.getData());
                        tvSelectname.setText(resultData.getData().getUserName());
                        if (resultData.getData().getHeadPortrait() != null && resultData.getData().getHeadPortrait().length() > 0) {
                            XImageUtils.loadCircle(PersonalInfoActivity.this, resultData.getData().getHeadPortrait(), ivHead);
                        } else {
                            ivHead.setImageResource(R.mipmap.ic_head);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            List<LocalMedia> videos = PictureSelector.obtainMultipleResult(data);
            if (null != videos) {
                for (final LocalMedia media : videos) {
                    if (media.isCut()) {
                        uploadImg(media.getCompressPath());
                    }
                }
            }
        }
    }

    //上传头像
    private void uploadImg(final String filePath) {
        String url = "?serviceName=" + "end-user-service" + "&" + "fileType=" + "PICTURE";
        HttpRequestParams params = new HttpRequestParams(Constant.UPLOAD_IMG + url);
        params.setMultipart(true);
        params.addBodyParameter("file", new File(filePath));
        HttpUtils.post(this, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<PictureIdEntity>>() {
                }.getType();
                ResultData<PictureIdEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    updataName(resultData.getData().getUrl());
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
     * 更新头像
     */
    public void updataName(final String url) {
        if (TextUtils.isEmpty(url)) {
            XToastUtil.showToast(this, getString(R.string.input_nikname));
            return;
        }
        ParamsMap paramsMap = new ParamsMap();
        paramsMap.put("headPortrait", url);
        HttpUtils.post(this, Constant.CHANGHEADPICTURE, paramsMap, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData>() {
                }.getType();
                ResultData resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    XToastUtil.showToast(PersonalInfoActivity.this, "设置成功");
                    getPersonalData();
                } else {
                    XToastUtil.showToast(PersonalInfoActivity.this, resultData.getDesc());
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

    @Override
    public void onResume() {
        super.onResume();
        UserInfoEntity userInfoEntity = UserInfoUtils.getUserInfo();
        if (userInfoEntity != null && !TextUtils.isEmpty(userInfoEntity.getToken())) {
            tvSelectname.setText(userInfoEntity.getUserName());
        }
    }
}
