package com.xylib.base.widgets;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xylib.base.R;
import com.zzhoujay.richtext.RichText;


/**
 * 折叠/展开ViewGroup
 *
 * @USER Edwin
 * @DATE 16/6/5 上午1:57
 */
public class CollapseView extends LinearLayout {
    private final Context mContext;
    private long duration = 555;//展开/折叠的时间(s)
    private TextView mTitleTextView;
    private RelativeLayout mContentRelativeLayout;
    private ImageView mArrowImageView;
    private int parentWidthMeasureSpec;
    private int parentHeightMeasureSpec;

    public CollapseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.collapse_layout, this);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentWidthMeasureSpec = widthMeasureSpec;
        parentHeightMeasureSpec = heightMeasureSpec;
    }

    private void init() {
        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mContentRelativeLayout = (RelativeLayout) findViewById(R.id.contentRelativeLayout);
        mArrowImageView = (ImageView) findViewById(R.id.arrowImageView);
        mTitleTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateArrow();
            }
        });

        collapse(mContentRelativeLayout);
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitleTextView.setText(title);
        }
    }

    public void setContent(int resID) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(Color.parseColor("#E7E7EF"));
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);

        ImageView view = new ImageView(mContext);
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        view.setImageResource(resID);
        layout.addView(view);
        mContentRelativeLayout.addView(layout);
    }

    public void setContent(String content) {
        TextView view = new TextView(mContext);
        view.setGravity(TEXT_ALIGNMENT_CENTER);
        view.setPadding(10, 0, 0, 10);
        view.setText(content);
        try {
            RichText.fromHtml(content).into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mContentRelativeLayout.addView(view);
    }

    private void rotateArrow() {
        int angle = 0;
        if (mArrowImageView.getTag() == null || mArrowImageView.getTag().equals(true)) {
            mArrowImageView.setTag(false);
            angle = -90;
            //TODO 展开
            expand(mContentRelativeLayout);
        } else {
            angle = 0;
            mArrowImageView.setTag(true);
            //TODO 折叠
            collapse(mContentRelativeLayout);
        }
        mArrowImageView.animate().setDuration(duration).rotation(angle);
    }


    /**
     * 折叠
     *
     * @param view 视图
     */
    private void collapse(final View view) {
        final int measuredHeight = view.getMeasuredHeight();
        Animation animation = new Animation() {
            /*绘制动画的过程中会反复的调用applyTransformation 函数，每次调用参数interpolatedTime值都会变化，
                该参数从0渐变为1，当该参数为1时表明动画结束。*/
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                Log.e("TAG", "interpolatedTime = " + interpolatedTime);
                if (interpolatedTime == 1) {
                    mContentRelativeLayout.setVisibility(GONE);
                } else {
                    view.getLayoutParams().height = measuredHeight - (int) (measuredHeight * interpolatedTime);
                    view.requestLayout();
                }
            }
        };
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 展开
     *
     * @param view 视图
     */
    private void expand(final View view) {
        view.measure(parentWidthMeasureSpec, parentHeightMeasureSpec);
        final int measuredHeight = view.getMeasuredHeight();
        view.setVisibility(View.VISIBLE);
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                Log.e("TAG", "interpolatedTime = " + interpolatedTime);
                if (interpolatedTime == 1) {
                    view.getLayoutParams().height = measuredHeight;
                } else {
                    view.getLayoutParams().height = (int) (measuredHeight * interpolatedTime);
                }
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }

        };
        animation.setDuration(duration);
        view.startAnimation(animation);
    }
}


