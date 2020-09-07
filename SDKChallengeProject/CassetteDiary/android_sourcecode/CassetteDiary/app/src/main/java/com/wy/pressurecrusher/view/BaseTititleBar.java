package com.wy.cassettediary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.wy.cassettediary.R;


public class BaseTititleBar extends LinearLayout {
    private TextView mTitileTv;
    private ImageView mBackImg;
    private ImageView mRightImg;

    public BaseTititleBar(Context context) {
        super(context);
        initView();
    }

    public BaseTititleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BaseTititleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_my_title_bar,this);
        mTitileTv = findViewById(R.id.title_tv);
        mBackImg = findViewById(R.id.back_img);
        mRightImg = findViewById(R.id.right_img);
    }

    public ImageView getLeftView(){
        return  mBackImg;
    }

    public void setLeftGone(){
        if(mBackImg==null){
            return;
        }
        mBackImg.setVisibility(View.GONE);
    }

    public void setLeftClickListener(OnClickListener clickListener){
        if(mBackImg==null){
            return;
        }
        mBackImg.setOnClickListener(clickListener);
    }

    public void setRightClickListener(OnClickListener clickListener){
        if(mRightImg==null){
            return;
        }
        mRightImg.setOnClickListener(clickListener);
    }

    public void setRightImgRes(int resID){
        if(mRightImg == null){
            return;
        }
        mRightImg.setImageResource(resID);
    }




    public void setTitleMid(String title){
        if(mTitileTv==null){
            return;
        }
        mTitileTv.setText(title==null?"":title);
    }


}
