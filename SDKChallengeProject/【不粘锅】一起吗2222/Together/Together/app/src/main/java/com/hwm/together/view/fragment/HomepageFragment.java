package com.hwm.together.view.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwm.together.R;
import com.hwm.together.view.activity.WatchMovieActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
public class HomepageFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final String TAG = "HomepageFragment";

    private LocationManager myLocationManager;
    private String locationProvider;
    private TextView tvSZX,tvLZG,tvBSNLCH,tvXLZ;

//    private Banner banner;
//    private TextView weatherTv;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        initView(view);
//        loadTestData();
        //        getWeather();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
//        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
//        banner.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loadTestData() {
        List<Drawable> imgList = new ArrayList();
        imgList.add(getResources().getDrawable(R.drawable.banner1));
        imgList.add(getResources().getDrawable(R.drawable.banner2));
        imgList.add(getResources().getDrawable(R.drawable.banner3));
        imgList.add(getResources().getDrawable(R.drawable.banner4));
//        setBanner(imgList);

    }

    private void initView(View mView) {
        tvSZX = mView.findViewById(R.id.fg_homepage_movie_tv_szx);
        tvSZX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWatch("https://www.bilibili.com/bangumi/play/ss12116/?from=search&seid=10322471219568039545");
            }
        });

        tvLZG = mView.findViewById(R.id.fg_homepage_movie_tv_lzg);
        tvLZG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWatch("https://www.bilibili.com/bangumi/play/ss10085/?from=search&seid=10322471219568039545");
            }
        });

        tvBSNLCH = mView.findViewById(R.id.fg_homepage_movie_tv_bsnlch);
        tvBSNLCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWatch("https://www.bilibili.com/bangumi/play/ss33531/?from=search&seid=10322471219568039545");
            }
        });

        tvXLZ = mView.findViewById(R.id.fg_homepage_movie_tv_xlz);
        tvXLZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWatch("https://www.bilibili.com/bangumi/play/ss12103/?from=search&seid=10322471219568039545");
            }
        });




//        weatherTv = mView.findViewById(R.id.homepage_local_weather_tv);
//        banner = mView.findViewById(R.id.homepage_banner);


        //弹出键盘不覆盖editText
        /*
        View decorView = getActivity().getWindow().getDecorView();
        View contentView = getActivity().findViewById(Window.ID_ANDROID_CONTENT);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));
         */
    }

    /*
    private void setBanner(List<Drawable> images) {
        //监听点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        banner.start();
    }
    */

    //动态的ImageView
    private ImageView getImageView(String imgUrl){
        ImageView imageView = new ImageView(getActivity());
        //宽高
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //imageView设置图片
        Glide.with(this).load(imgUrl).into(imageView);
        return imageView;
    }

    /**
     * 弹出键盘不覆盖editText
     * @param decorView
     * @param contentView
     * @return
     */
    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fg_homepage_movie_tv_szx:
            case R.id.fg_homepage_movie_img_szx:
                startWatch("https://www.bilibili.com/bangumi/play/ss12116/?from=search&seid=10322471219568039545");
                break;
            case R.id.fg_homepage_movie_tv_lzg:
            case R.id.fg_homepage_movie_img_lzg:
                startWatch("https://www.bilibili.com/bangumi/play/ss10085/?from=search&seid=10322471219568039545");
                break;
            case R.id.fg_homepage_movie_tv_bsnlch:
            case R.id.fg_homepage_movie_img_bsnlch:
                startWatch("https://www.bilibili.com/bangumi/play/ss33531/?from=search&seid=10322471219568039545");
                break;
            case R.id.fg_homepage_movie_tv_xlz:
            case R.id.fg_homepage_movie_img_xlz:
                startWatch("https://www.bilibili.com/bangumi/play/ss12103/?from=search&seid=10322471219568039545");
                break;
        }
    }

    private void startWatch(String url){
        Intent intent = new Intent(getActivity(),WatchMovieActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

}

