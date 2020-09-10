package com.xylib.base.view.navigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xylib.base.R;
import com.xylib.base.utils.StringUtil;

/**
 * Created by Zyj on 2016/6/4.
 */
public class NavigationView extends FrameLayout implements View.OnClickListener {

    public static final int SKIN_GREEN = 0;
    public static final int SKIN_WHITE = 1;
    public static final int SKIN_ALPHA = 2;
    private int mBackgroundColor;
    private int mTextColor;
    private int mTitleColor;
    private TextView mTitleView;
    private NavigationListener mNavigationListener;
    private TextView mLeftTextView;
    private TextView mLeftIconView;
    private LinearLayout mLeftLayout;
    private ImageView mRightIconView;
    private TextView mRightTextView;
    private LinearLayout mRightLayout;
    private TextView mRightSecondTextView;
    private ImageView mRightSecondIconView;
    private LinearLayout mRightSecondLayout;
    private boolean showBadge = false;
    private View mDivider;

    public NavigationView(Context context) {
        this(context, null);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View.inflate(getContext(), R.layout.navigation_layout, this);
        mLeftLayout = (LinearLayout) findViewById(R.id.left_layout);
        mLeftLayout.setOnClickListener(this);
        mLeftTextView = (TextView) findViewById(R.id.left_text);
        mLeftIconView = (TextView) findViewById(R.id.left_icon);
        mRightLayout = (LinearLayout) findViewById(R.id.right_layout);
        mRightLayout.setOnClickListener(this);
        mRightTextView = (TextView) findViewById(R.id.right_text);
        mRightIconView = (ImageView) findViewById(R.id.right_icon);
        mRightSecondLayout = (LinearLayout) findViewById(R.id.right_second_layout);
        mRightSecondLayout.setOnClickListener(this);
        mRightSecondTextView = (TextView) findViewById(R.id.right_second_text);
        mRightSecondIconView = (ImageView) findViewById(R.id.right_second_icon);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setOnClickListener(this);
        mDivider = findViewById(R.id.nav_divider);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NavigationView);
            int skin = typedArray.getInt(R.styleable.NavigationView_skin, 0);
            String mLeftText = typedArray.getString(R.styleable.NavigationView_text_left);
            String mLeftIcon = typedArray.getString(R.styleable.NavigationView_icon_left);
            String mRightText = typedArray.getString(R.styleable.NavigationView_text_right);
            String mRightIcon = typedArray.getString(R.styleable.NavigationView_icon_right);
            String mRightSecondText = typedArray.getString(R.styleable.NavigationView_text_right_second);
            String mRightSecondIcon = typedArray.getString(R.styleable.NavigationView_icon_right_second);
            String mTitleText = typedArray.getString(R.styleable.NavigationView_text_title);
            float iconSize = typedArray.getDimension(R.styleable.NavigationView_icon_size, 20);
            float titleSize = typedArray.getDimension(R.styleable.NavigationView_title_size, 18);
            float textSize = typedArray.getDimension(R.styleable.NavigationView_text_size, 14);
            boolean showDivider = typedArray.getBoolean(R.styleable.NavigationView_show_divider, false);
            setSkin(skin);
            if (showDivider) {
                setDividerVisibility(VISIBLE);
            } else if (skin == SKIN_WHITE) {
                setDividerVisibility(VISIBLE);
            } else {
                setDividerVisibility(GONE);
            }
            mTitleView.setTextSize(titleSize);
            mLeftIconView.setTextSize(iconSize);
            mLeftTextView.setTextSize(textSize);
            mRightTextView.setTextSize(textSize);
            mRightSecondTextView.setTextSize(textSize);
            setLeftText(mLeftText);
            setLeftIcon(mLeftIcon);
            setRightIcon(mRightIcon);
            setRightText(mRightText);
            setRightSecondIcon(mRightSecondIcon);
            setRightSecondText(mRightSecondText);
            setTextTitle(mTitleText);
            typedArray.recycle();
        }
    }

    public void setDividerVisibility(int visibility) {
        mDivider.setVisibility(visibility);
    }

    public void setSkin(int skin) {
        if (skin == SKIN_GREEN) {
            mBackgroundColor = getResources().getColor(R.color.theme_green);
            mTextColor = getResources().getColor(R.color.white);
            mTitleColor = getResources().getColor(R.color.white);
        } else if (skin == SKIN_WHITE) {
            mBackgroundColor = getResources().getColor(R.color.white);
            mTextColor = getResources().getColor(R.color.main_font);
            mTitleColor = getResources().getColor(R.color.main_font);
        } else if (skin == SKIN_ALPHA) {
            mBackgroundColor = getResources().getColor(R.color.transparent);
            mTextColor = getResources().getColor(R.color.main_font);
            mTitleColor = getResources().getColor(R.color.main_font);
        }
        setBackgroundColor(mBackgroundColor);
        mLeftTextView.setTextColor(mTextColor);
        mLeftIconView.setTextColor(mTextColor);
        mLeftIconView.setBackgroundResource(R.color.transparent);
        mRightTextView.setTextColor(mTextColor);
        mRightIconView.setBackgroundResource(R.color.transparent);
        mRightSecondTextView.setTextColor(mTextColor);
        mRightSecondIconView.setBackgroundResource(R.color.transparent);
        mTitleView.setTextColor(mTitleColor);
    }


    public void setLeftText(String text) {
        if (StringUtil.isNotBlank(text)) {
            mLeftTextView.setText(text);
            mLeftTextView.setVisibility(View.VISIBLE);
        } else {
            mLeftTextView.setVisibility(View.GONE);
        }
    }

    public void setLeftTextColor(int color) {
        mLeftTextView.setTextColor(color);
    }

    public void setLeftIcon(String text) {
        if (StringUtil.isNotBlank(text)) {
            mLeftIconView.setText(Html.fromHtml(text));
            mLeftIconView.setVisibility(View.VISIBLE);
        } else {
            mLeftIconView.setVisibility(View.GONE);
        }
    }

    public void setLeftIcon(int resId) {
        if (resId > 0) {
            mLeftIconView.setText(resId);
            mLeftIconView.setVisibility(View.VISIBLE);
        } else {
            mLeftIconView.setVisibility(View.GONE);
        }
    }

    public void setLeftIconColor(int color) {
        mLeftIconView.setTextColor(color);
    }

    public void setLeftIconBackground(int resId) {
        mLeftIconView.setBackgroundResource(resId);
    }

    public void setLeftLayoutBackground(int resId) {
        mLeftLayout.setBackgroundResource(resId);
    }

    public void setRightLayoutBackground(int resId) {
        mRightLayout.setBackgroundResource(resId);
    }

    public void setRightSecondLayoutBackground(int resId) {
        mRightSecondLayout.setBackgroundResource(resId);
    }

    public void setRightText(String text) {
        if (StringUtil.isNotBlank(text)) {
            mRightTextView.setText(text);
            mRightTextView.setVisibility(View.VISIBLE);
        } else {
            mRightTextView.setVisibility(View.GONE);
        }
    }

    public void setRightTextColor(int color) {
        mRightTextView.setTextColor(color);
    }

    public void setRightIcon(String text) {
        if (StringUtil.isNotBlank(text)) {
            mRightIconView.setVisibility(View.VISIBLE);
        } else {
            mRightIconView.setVisibility(View.GONE);
        }
    }

    public void setRightIcon(int resId) {
        if (resId > 0) {
            mRightIconView.setVisibility(View.VISIBLE);
        } else {
            mRightIconView.setVisibility(View.GONE);
        }
    }


    public void setRightIconBackground(int resId) {
        mRightIconView.setBackgroundResource(resId);
    }

    public void setRightSecondText(String text) {
        if (StringUtil.isNotBlank(text)) {
            mRightSecondTextView.setText(text);
            mRightSecondTextView.setVisibility(View.VISIBLE);
        } else {
            mRightSecondTextView.setVisibility(View.GONE);
        }
    }

    public void setRightSecondTextColor(int color) {
        mRightSecondTextView.setTextColor(color);
    }

    public void setRightSecondIcon(String text) {
        if (StringUtil.isNotBlank(text)) {
            mRightSecondIconView.setVisibility(View.VISIBLE);
        } else {
            mRightSecondIconView.setVisibility(View.GONE);
        }
    }

    public void setRightSecondIcon(int resId) {
        if (resId > 0) {
            mRightSecondIconView.setVisibility(View.VISIBLE);
        } else {
            mRightSecondIconView.setVisibility(View.GONE);
        }
    }


    public void setRightSecondIconBackground(int resId) {
        mRightSecondIconView.setBackgroundResource(resId);
    }

    public void setTextTitle(String text) {
        if (StringUtil.isNotBlank(text)) {
            mTitleView.setText(text);
            mTitleView.setVisibility(View.VISIBLE);
        } else {
            mTitleView.setVisibility(View.GONE);
        }
    }

    public void setTitleColor(int color) {
        mTitleView.setTextColor(color);
    }

    public void setOnNavigationListener(NavigationListener listener) {
        mNavigationListener = listener;
    }

    public String getTitle() {
        return mTitleView.getText().toString();
    }

    @Override
    public void onClick(View v) {
        if (mNavigationListener == null) {
            return;
        }
        if (R.id.left_layout == v.getId()) {
            mNavigationListener.onLeftClick();
        } else if (R.id.right_layout == v.getId()) {
            mNavigationListener.onRightClick();
        } else if (R.id.right_second_layout == v.getId()) {
            mNavigationListener.onRightSecondClick();
        }
    }

    public void setShowBadge(boolean showBadge) {
        this.showBadge = showBadge;
    }

}
