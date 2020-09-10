package com.hwm.together.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hwm.together.R;
import com.hwm.together.util.SoftKeyboardUtil;
import com.hwm.together.util.StatusBarUtil;

public class SetNoteNameActivity extends AppCompatActivity {
    private static final String TAG = "SetNoteNameActivity";
    private ImageButton ibBack;
    private TextView tvTitle;
    private EditText etNoteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTranslucent(this);
        setContentView(R.layout.activity_set_note_name);
    }

    private void initView(){
        ibBack = findViewById(R.id.set_noteName_in_top_bar).findViewById(R.id.include_title_black_back);
        tvTitle = findViewById(R.id.set_noteName_in_top_bar).findViewById(R.id.include_title_black_title);

        tvTitle.setText("设置好友备注");
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击空白隐藏软键盘
        View view = findViewById(R.id.set_noteName_cl_parentView);
        SoftKeyboardUtil.updateUI(this,view);
    }

    public void setNoteName(View view){

    }
}