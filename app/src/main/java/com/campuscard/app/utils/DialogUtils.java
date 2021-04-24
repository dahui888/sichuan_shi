package com.campuscard.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.base.frame.utils.IntentUtil;
import com.base.frame.utils.XAppUtil;
import com.base.frame.utils.XToastUtil;
import com.campuscard.app.Constant;
import com.campuscard.app.R;
import com.campuscard.app.http.HttpRequestParams;
import com.campuscard.app.http.HttpResponseCallBack;
import com.campuscard.app.http.HttpUtils;
import com.campuscard.app.http.ResultData;
import com.campuscard.app.ui.activity.RegistActivity;
import com.campuscard.app.ui.activity.my.BindCardActivity;
import com.campuscard.app.ui.entity.TxyamEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DialogUtils {

    /**
     * 加载进度框
     *
     * @param context
     * @param msg     提示信息
     * @return
     */
    public static Dialog showLoadDialog(Context context, String msg) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(view);
        TextView tv_msg = (TextView) view.findViewById(R.id.message);
        if (!TextUtils.isEmpty(msg)) {
            tv_msg.setText(msg);
        }
        return dialog;
    }

    /**
     * 中间弹出对话框
     *
     * @param context
     * @param content
     */
    public static void showCenterDialog(final Context context, String content, final OnResult onResult) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        View viewNotice = LayoutInflater.from(context).inflate(R.layout.dialog_center, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(viewNotice);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView tvContent = (TextView) viewNotice.findViewById(R.id.tv_content);
        Button bCcancel = (Button) viewNotice.findViewById(R.id.bt_cancel);
        Button btSure = (Button) viewNotice.findViewById(R.id.bt_sure);
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
        btSure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onResult.onResult("");
                dialog.dismiss();
            }
        });
        bCcancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 图形验证码
     */
    public static void showImageValidateDialog(final Context context, String imag, final YzmCallBack callBack) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        View viewNotice = LayoutInflater.from(context).inflate(R.layout.dialog_validate_image_view, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(viewNotice);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button btCancel = (Button) viewNotice.findViewById(R.id.bt_cancel);
        Button btConfirm = (Button) viewNotice.findViewById(R.id.bt_confirm);
        final ImageView ivValidateImg = (ImageView) viewNotice.findViewById(R.id.iv_validate_img);
        final EditText etValidateImage = (EditText) viewNotice.findViewById(R.id.et_validate_image);
        ivValidateImg.setImageBitmap(ImageUtil.stringtoBitmap(imag));
        ivValidateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTxyam(context, ivValidateImg);
            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (etValidateImage.getText().toString().isEmpty()) {
                    XToastUtil.showToast(context, "请填写图形验证码");
                } else {
                    dialog.dismiss();
                    callBack.callback(etValidateImage.getText().toString());
                }

            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 绑定校园卡
     *
     * @param context
     */
    public static void bindCardShow(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        View viewNotice = LayoutInflater.from(context).inflate(R.layout.dialog_bind_card, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(viewNotice);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button btConfirm = (Button) viewNotice.findViewById(R.id.bt_confirm);
        ImageView ivDelete = (ImageView) viewNotice.findViewById(R.id.iv_delete);

        //绑定
        btConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentUtil.redirectToNextActivity(context, BindCardActivity.class);
                SharedCacheUtils.put(Constant.BIND_SHOW, false);
                dialog.dismiss();
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedCacheUtils.put(Constant.BIND_SHOW, false);
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SharedCacheUtils.put(Constant.BIND_SHOW, false);
                dialog.dismiss();
            }
        });
    }

    /**
     * 失物招领筛选
     */
    public static void filterShow(final Context mContext, View view, String type, final OnResult onResult,final PopuDimssCallBack callBack) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_filter, null);
        final PopupWindow popupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout lin_sw = (LinearLayout) rootView.findViewById(R.id.lin_sw);
        LinearLayout lin_zl = (LinearLayout) rootView.findViewById(R.id.lin_zl);
        final TextView tv_sw = (TextView) rootView.findViewById(R.id.tv_sw);
        final ImageView iv_sw = (ImageView) rootView.findViewById(R.id.iv_sw);
        final TextView tv_zl = (TextView) rootView.findViewById(R.id.tv_zl);
        final ImageView iv_zl = (ImageView) rootView.findViewById(R.id.iv_zl);
        final View bgView = (View) rootView.findViewById(R.id.bg_view);


        popupWindow.setContentView(rootView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(view);

        if (!TextUtils.isEmpty(type)) {
            if (TextUtils.equals("1", type)) {
                tv_sw.setTextColor(Color.parseColor("#00b464"));
                iv_sw.setImageResource(R.mipmap.ic_gou);
                tv_zl.setTextColor(Color.parseColor("#333333"));
                iv_zl.setImageResource(R.mipmap.ic_gou_f);
            } else {
                tv_zl.setTextColor(Color.parseColor("#00b464"));
                iv_zl.setImageResource(R.mipmap.ic_gou);
                tv_sw.setTextColor(Color.parseColor("#333333"));
                iv_sw.setImageResource(R.mipmap.ic_gou_f);
            }
        }

        lin_sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sw.setTextColor(Color.parseColor("#00b464"));
                iv_sw.setImageResource(R.mipmap.ic_gou);
                tv_zl.setTextColor(Color.parseColor("#333333"));
                iv_zl.setImageResource(R.mipmap.ic_gou_f);
                onResult.onResult("1");
                popupWindow.dismiss();
            }
        });
        lin_zl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_zl.setTextColor(Color.parseColor("#00b464"));
                iv_zl.setImageResource(R.mipmap.ic_gou);

                tv_sw.setTextColor(Color.parseColor("#333333"));
                iv_sw.setImageResource(R.mipmap.ic_gou_f);
                onResult.onResult("2");
                popupWindow.dismiss();
            }
        });
        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                callBack.callback();
            }
        });
    }

    /**
     * 时间筛选
     */
    public static void filterShowTime(final Context mContext, View view, String typeCode, final OnResult onResult) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_filtertime, null);
        final PopupWindow popupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout lin_rzd = (LinearLayout) rootView.findViewById(R.id.lin_rzd);
        LinearLayout lin_zzd = (LinearLayout) rootView.findViewById(R.id.lin_zzd);
        LinearLayout lin_yzd = (LinearLayout) rootView.findViewById(R.id.lin_yzd);
        final TextView tv_rzd = (TextView) rootView.findViewById(R.id.tv_rzd);
        final ImageView iv_rzd = (ImageView) rootView.findViewById(R.id.iv_rzd);
        final TextView tv_zzd = (TextView) rootView.findViewById(R.id.tv_zzd);
        final ImageView iv_zzd = (ImageView) rootView.findViewById(R.id.iv_zzd);
        final TextView tv_yzd = (TextView) rootView.findViewById(R.id.tv_yzd);
        final ImageView iv_yzd = (ImageView) rootView.findViewById(R.id.iv_yzd);
        final View bgView = (View) rootView.findViewById(R.id.bg_view);

        popupWindow.setContentView(rootView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(view);

        switch (typeCode) {
            case "1":
                tv_rzd.setTextColor(Color.parseColor("#00b464"));
                iv_rzd.setImageResource(R.mipmap.ic_gou);
                tv_zzd.setTextColor(Color.parseColor("#333333"));
                iv_zzd.setImageResource(R.mipmap.ic_gou_f);
                tv_yzd.setTextColor(Color.parseColor("#333333"));
                iv_yzd.setImageResource(R.mipmap.ic_gou_f);
                break;
            case "2":
                tv_zzd.setTextColor(Color.parseColor("#00b464"));
                iv_zzd.setImageResource(R.mipmap.ic_gou);

                tv_rzd.setTextColor(Color.parseColor("#333333"));
                iv_rzd.setImageResource(R.mipmap.ic_gou_f);
                tv_yzd.setTextColor(Color.parseColor("#333333"));
                iv_yzd.setImageResource(R.mipmap.ic_gou_f);
                break;
            case "3":
                tv_yzd.setTextColor(Color.parseColor("#00b464"));
                iv_yzd.setImageResource(R.mipmap.ic_gou);
                tv_rzd.setTextColor(Color.parseColor("#333333"));
                iv_rzd.setImageResource(R.mipmap.ic_gou_f);
                tv_zzd.setTextColor(Color.parseColor("#333333"));
                iv_zzd.setImageResource(R.mipmap.ic_gou_f);
                break;
        }
        lin_rzd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_rzd.setTextColor(Color.parseColor("#00b464"));
                iv_rzd.setImageResource(R.mipmap.ic_gou);
                tv_zzd.setTextColor(Color.parseColor("#333333"));
                iv_zzd.setImageResource(R.mipmap.ic_gou_f);
                tv_yzd.setTextColor(Color.parseColor("#333333"));
                iv_yzd.setImageResource(R.mipmap.ic_gou_f);
                onResult.onResult("1");
                popupWindow.dismiss();
            }
        });
        lin_zzd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_zzd.setTextColor(Color.parseColor("#00b464"));
                iv_zzd.setImageResource(R.mipmap.ic_gou);
                tv_rzd.setTextColor(Color.parseColor("#333333"));
                iv_rzd.setImageResource(R.mipmap.ic_gou_f);
                tv_yzd.setTextColor(Color.parseColor("#333333"));
                iv_yzd.setImageResource(R.mipmap.ic_gou_f);
                onResult.onResult("2");
                popupWindow.dismiss();
            }
        });
        lin_yzd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_yzd.setTextColor(Color.parseColor("#00b464"));
                iv_yzd.setImageResource(R.mipmap.ic_gou);
                tv_rzd.setTextColor(Color.parseColor("#333333"));
                iv_rzd.setImageResource(R.mipmap.ic_gou_f);
                tv_zzd.setTextColor(Color.parseColor("#333333"));
                iv_zzd.setImageResource(R.mipmap.ic_gou_f);
                onResult.onResult("3");
                popupWindow.dismiss();
            }
        });
        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 挂失校园卡
     *
     * @param context
     */
    public static void lossCardShow(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyle);
        View viewNotice = LayoutInflater.from(context).inflate(R.layout.dialog_loss_card, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(viewNotice);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Button btConfirm = (Button) viewNotice.findViewById(R.id.bt_confirm);

        //绑定
        btConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }

    //刷新图形验证码
    public static void getTxyam(final Context context, final ImageView imageView) {
        HttpRequestParams params = new HttpRequestParams(Constant.GET_TXYZM);
        HttpUtils.get(context, params, new HttpResponseCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResultData<TxyamEntity>>() {
                }.getType();
                final ResultData<TxyamEntity> resultData = gson.fromJson(result, type);
                if (resultData.getStatus() == 200) {
                    imageView.setImageBitmap(ImageUtil.stringtoBitmap(resultData.getData().getImgBase64()));
                } else {
                    XToastUtil.showToast(context, resultData.getDesc());
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
     * 回掉
     */
    public interface OnResult {
        void onResult(String code);
    }
}
