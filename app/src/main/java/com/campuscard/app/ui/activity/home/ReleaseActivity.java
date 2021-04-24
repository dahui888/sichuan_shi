package com.campuscard.app.ui.activity.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.base.frame.utils.XToastUtil;
import com.base.frame.weigt.XGridViewForScrollView;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.base.BaseActivity;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.entity.DeleteSuccessEntity;
import com.campuscard.app.ui.entity.EventDeleteImageEntity;
import com.campuscard.app.ui.entity.LostFoundEntity;
import com.campuscard.app.ui.entity.PictureIdEntity;
import com.campuscard.app.ui.entity.ReleasePostEntity;
import com.campuscard.app.ui.entity.UploadEntity;
import com.campuscard.app.ui.holder.UploadFileAdapter;
import com.campuscard.app.view.ClassTypeDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布
 */
@ContentView(R.layout.activity_release)
public class ReleaseActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    @ViewInject(R.id.tv_class_type)
    protected TextView tvClassType;
    @ViewInject(R.id.et_addresss)
    protected EditText etAddresss;
    @ViewInject(R.id.et_content)
    protected EditText etContent;
    @ViewInject(R.id.mGridView)
    protected XGridViewForScrollView mGridView;
    @ViewInject(R.id.mRadioGroup)
    protected RadioGroup mRadioGroup;
    @ViewInject(R.id.et_mobile)
    private EditText etMobile;
    @ViewInject(R.id.bt_sure)
    private Button btSure;
    @ViewInject(R.id.bt_type)
    private LinearLayout btType;

    //图片
    private UploadFileAdapter uploadFileAdapter;
    private List<UploadEntity> imgs = new ArrayList<>();
    private List<LocalMedia> videos;
    private String lost = "LOST", typeId;
    private ReleasePostEntity releasePostEntity;//提交数据实体类
    private ClassTypeDialog classTypeDialog;//分类选择

    //编辑
    private LostFoundEntity lostFoundEntity;

    @Override
    public void initView() {
        toolbar.setNavigationIcon(null);

        mGridView.setOnItemClickListener(this);
        uploadFileAdapter = new UploadFileAdapter(this);
        mGridView.setAdapter(uploadFileAdapter);
        btSure.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        releasePostEntity = new ReleasePostEntity();
        btType.setOnClickListener(this);
    }

    @Override
    public void getData() {
        classTypeDialog = new ClassTypeDialog(this);
        classTypeDialog.setOnClickDataListener(new ClassTypeDialog.OnClickDataListener() {
            @Override
            public void onSuccess() {
                tvClassType.setText(classTypeDialog.getWork().getName());
                typeId = classTypeDialog.getWork().getId();
            }

            @Override
            public void onDismiss() {
            }
        });


        //编辑时候展示
        lostFoundEntity = EventBus.getDefault().getStickyEvent(LostFoundEntity.class);
        if (lostFoundEntity != null) {
            typeId = lostFoundEntity.getLostFoundThingsTypeInfoId();
            if (lostFoundEntity.getLostFoundType().equals("LOST")) {
                lost = "LOST";
                mRadioGroup.check(R.id.rb_sw);
            } else {
                lost = "FOUND";
                mRadioGroup.check(R.id.rb_zl);
            }
            tvClassType.setText(lostFoundEntity.getLostFoundThingType());
            etMobile.setText(lostFoundEntity.getContactPhoneNo());
            etAddresss.setText(lostFoundEntity.getAddress());
            etContent.setText(lostFoundEntity.getContent());
            //展示图片
            if (lostFoundEntity.getLostAndFoundPictureVOS() != null && lostFoundEntity.getLostAndFoundPictureVOS().size() > 0) {
                uploadFileAdapter = new UploadFileAdapter(ReleaseActivity.this);
                mGridView.setAdapter(uploadFileAdapter);
                for (int i = 0; i < lostFoundEntity.getLostAndFoundPictureVOS().size(); i++) {
                    UploadEntity uploadEntity = new UploadEntity();
                    uploadEntity.path = lostFoundEntity.getLostAndFoundPictureVOS().get(i).getImgURL();
                    uploadFileAdapter.addPath(uploadEntity);
                    uploadFileAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == uploadFileAdapter.getCount() - 1) {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .theme(R.style.picture_default_style)
                    .isCamera(true)//不显示拍照按钮
                    .maxSelectNum(6)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .selectionMode(PictureConfig.MULTIPLE)//多选
                    .imageSpanCount(4)// 每行显示个数
                    .previewImage(false)//是否预览
                    .enableCrop(false)// 是否裁剪
                    .compress(true)// 是否压缩
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_sw:
                //失物
                lost = "LOST";
                break;
            case R.id.rb_zl:
                //招领
                lost = "FOUND";
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_type:
                classTypeDialog.show();
                break;
            case R.id.bt_sure:
                //发布
                if (TextUtils.isEmpty(lost)) {
                    XToastUtil.showToast(this, "请选择发布类型");
                    return;
                }
                if (TextUtils.isEmpty(tvClassType.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.choice_class));
                    return;
                }
                if (TextUtils.isEmpty(etMobile.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_mobiles));
                    return;
                }
                if (TextUtils.isEmpty(etAddresss.getText().toString())) {
                    XToastUtil.showToast(this, getString(R.string.input_lost_area));
                    return;
                }
                if (TextUtils.isEmpty(etContent.getText().toString())) {
                    XToastUtil.showToast(this, "请输入物品描述");
                    return;
                }
                if (uploadFileAdapter.getPath() != null && uploadFileAdapter.getPath().size() > 0) {
                    List<ReleasePostEntity.PicturesBean> pictures = new ArrayList<>();
                    for (int i = 0; i < uploadFileAdapter.getPath().size(); i++) {
                        ReleasePostEntity.PicturesBean picturesBean = new ReleasePostEntity.PicturesBean();
                        picturesBean.setImgURL("http://easy-parking.oss-cn-shenzhen.aliyuncs.com/657fd408826e446bbb22915e1190c5e8.jpg");
                        picturesBean.setSortNo("0");
                        pictures.add(picturesBean);
                    }
                    releasePostEntity.setPictures(pictures);
                } else {
                    XToastUtil.showToast(this, "请上传图片");
                    return;
                }
                releasePostEntity.setAddress(etAddresss.getText().toString());
                releasePostEntity.setContactPhoneNo(etMobile.getText().toString());
                releasePostEntity.setContent(etContent.getText().toString());
                releasePostEntity.setLostFoundThingsTypeInfoId(typeId);
                releasePostEntity.setLostFoundType(lost);
                String url = "";
                if (lostFoundEntity != null) {
                    url = Constant.UPDATERELEASE;
                    releasePostEntity.setId(lostFoundEntity.getId());
                } else {
                    url = Constant.RELEASE;
                }
                HttpRequestParams params = new HttpRequestParams(url);
                HttpUtils.postJson(this, params, new Gson().toJson(releasePostEntity).toString(), new HttpResponseCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ResultData>() {
                        }.getType();
                        ResultData resultData = gson.fromJson(result, type);
                        if (resultData.getStatus() == 200) {
                            XToastUtil.showToast(ReleaseActivity.this, "发布成功");
                            //通知列表界面刷新
                            EventBus.getDefault().post(new DeleteSuccessEntity());
                            finish();
                        } else {
                            XToastUtil.showToast(ReleaseActivity.this, resultData.getDesc());
                        }
                    }

                    @Override
                    public void onFailed(int code, String failedMsg) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            videos = PictureSelector.obtainMultipleResult(data);
            if (null != videos) {
                if (null != imgs) {
                    imgs.clear();
                }
                for (LocalMedia media : videos) {
                    uploadImg(media.getCompressPath());
                }
            }
        }
    }

    //上传图片
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
                    UploadEntity uploadEntity = new UploadEntity();
                    uploadEntity.path = resultData.getData().getUrl();
                    uploadEntity.pathId = resultData.getData().getId();
                    imgs.add(uploadEntity);
                    uploadFileAdapter.addPath(uploadEntity);
                    uploadFileAdapter.notifyDataSetChanged();
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
    protected void onDestroy() {
        super.onDestroy();
        if (lostFoundEntity != null) {
            EventBus.getDefault().removeStickyEvent(lostFoundEntity);
        }
    }
}
