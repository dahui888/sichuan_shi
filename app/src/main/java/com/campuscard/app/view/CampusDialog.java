package com.campuscard.app.view;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.campuscard.app.R;
import com.campuscard.app.ui.holder.CampusHolder;

import java.util.List;


/**
 * @Description:
 * @Author xiaoTan
 * @Date Created in 19:42 2018/7/30
 */
public class CampusDialog extends Dialog implements CampusHolder.OnRecycleListener {
    private Context context;

    private List<String> list;

    private OnSelectListener onSelectListener;

    private CampusDialog(@NonNull Context context, int themeResId,List<String> list) {
        super(context, themeResId);
        this.context = context;
        this.list = list;
        init();
    }

    public static CampusDialog build(Context context,List<String> list) {
        return new CampusDialog(context, R.style.CustomDialog,list);

    }

    private void init() {
        View view = View.inflate(context,R.layout.dialog_campus,null);

        RecyclerView recyclerView = view.findViewById(R.id.rv_campus);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        CampusHolder campusHolder = new CampusHolder(list,context);
        campusHolder.setOnRecycleListener(this);
        recyclerView.setAdapter(campusHolder);

        if (getWindow() != null) {
            getWindow().setWindowAnimations(R.style.BottomInAndOutWindow);
        }
        setContentView(view);
    }
    public void showWindow() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            WindowManager windowManager = window.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            lp.width =  display.getWidth();
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
        }
        super.show();
    }
    public CampusDialog setListener(OnSelectListener onSelectListener){
        this.onSelectListener = onSelectListener;
        return this;
    }

    @Override
    public void onRecycleListener(int postion) {
        onSelectListener.onSelectListener(list.get(postion));
        dismiss();
    }

    public interface OnSelectListener {
        void onSelectListener(String name);
    }
}
