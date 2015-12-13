package com.wekan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wekan.R;
import com.wekan.utils.DisplayUtil;

/**
 * Created by yuanyuan06 on 2015/12/13.
 */
public class LogView extends ScrollView implements View.OnTouchListener {
    private TextView msgView;
    private Button btn1;
    private final int MAX_LINES = 1000;
    private int maxHeight = 300;
    private int minHeight = 30;
    private boolean unfold = false;

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_log, this);
        msgView = (TextView) findViewById(R.id.textView);
        msgView.setMaxLines(MAX_LINES);
        btn1 = (Button) findViewById(R.id.btn1);
        this.setOnTouchListener(this);
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unfold = false;
                changStatus(unfold);
                btn1.setVisibility(GONE);
            }
        });
    }

    public void showMessage(String tag, String message) {
        showMessage("[" + tag + "]" + message + "\n");
        this.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void showMessage(String message) {
        msgView.append(message + "\n");
        this.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!unfold) {
            unfold = !unfold;
            changStatus(unfold);
            btn1.setVisibility(VISIBLE);
        }
        return false;
    }

    public void changStatus(boolean unfold) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = unfold ? DisplayUtil.dip2px(getContext(), maxHeight) : DisplayUtil.dip2px(getContext(), minHeight);
        this.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }


    public LogView(Context context) {
        super(context);
        init(context);
    }

    public LogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


}
