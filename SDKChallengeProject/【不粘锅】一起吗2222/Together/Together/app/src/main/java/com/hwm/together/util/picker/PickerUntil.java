package com.hwm.together.util.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.hwm.together.R;
import com.hwm.together.eventbus.PickerMessageEvent;
import com.hwm.together.util.CommonUtil;
import com.hwm.together.util.ToastUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by warming on 2020/6/6.
 * 选择器工具类
 * 地址选择器
 * 性别选择器
 * 职业选择器
 * 日期选择器
 */
public class PickerUntil {

    private OptionsPickerView locationOptions,genderOptions,jobOptions;
    private TimePickerView pvCustomTime;

    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    public List<String> jobList = new ArrayList<>();
    public List<String> genderList = new ArrayList<>();

    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static boolean isLoaded = false;

    private final int MESSAGE_TYPE_GENDER = 1;//性别
    private final int MESSAGE_TYPE_PROVINCE = 2;//省
    private final int MESSAGE_TYPE_CITY = 3;//市
    private final int MESSAGE_TYPE_DATE = 4;//生日

    private static Context context;

//    private String checkedLocation;
    private String checkedProvince;
    private String checkedCity;
    private String checkedGender;
    private String checkedDate;

    boolean isChanged = false;//判断是否改变选择


    public PickerUntil(Context pContext) {
        context = pContext;
        genderList.add("女");
        genderList.add("男");
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

//                        Toast.makeText(context, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(context, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;
                case MSG_LOAD_FAILED:
                    Toast.makeText(context, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //性别选择器
    public void showGenderPicker(){
        isChanged = false;
        genderOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //


            }
        }).setLayoutRes(R.layout.picker_gender, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.picker_gender_inc_bg).findViewById(R.id.include_picker_tv_submit);
                final TextView tvCancle = v.findViewById(R.id.picker_gender_inc_bg).findViewById(R.id.include_picker_tv_cancle);
//                final TextView tvSubmit = (TextView) v.findViewById(R.id.picker_gender_tv_submit);
//                final TextView tvCancle = (TextView) v.findViewById(R.id.picker_gender_tv_cancle);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isChanged){//没有改变默认选择第一项
                            EventBus.getDefault().post(new PickerMessageEvent(MESSAGE_TYPE_GENDER,genderList.get(0)));
                        }else {
                            EventBus.getDefault().post(new PickerMessageEvent(MESSAGE_TYPE_GENDER,checkedGender));
                        }
                        genderOptions.dismiss();
                    }
                });
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {
                checkedGender = genderList.get(options1);
                isChanged = true;
            }
        })
                .setLineSpacingMultiplier(2.4f)
                .setItemVisibleCount(5)
                .setContentTextSize(17)
                .isRestoreItem(true)
                .isAlphaGradient(true)
                .build();

        genderOptions.setPicker(genderList);
        genderOptions.show();
    }

    //日期选择器
    public void showDatePicker(String birthday){
        Calendar currentDate = Calendar.getInstance();//系统当前时间
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        Calendar startDate = Calendar.getInstance();
        startDate.set(1930, 0, 1);
        Calendar selectedDate = Calendar.getInstance();
        startDate.set(1930, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(currentYear,currentMonth,currentDay);
        if (!TextUtils.isEmpty(birthday) && !TextUtils.equals("请选择生日",birthday)) {
            int year = CommonUtil.formatDate(birthday, "yyyy");
            int month = CommonUtil.formatDate(birthday, "MM") - 1;
            int day = CommonUtil.formatDate(birthday, "dd");
            selectedDate.set(year, month, day);
            checkedDate = birthday;
        } else {
            selectedDate.set(2001, 0, 1);
            checkedDate = "2001-01-01";
        }
        currentDate.set(currentYear,currentMonth,currentDay);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.picker_date, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.picker_date_tv_submit);
                        final TextView tvCancel = v.findViewById(R.id.picker_date_tv_cancle);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                pvCustomTime.returnData();
                                EventBus.getDefault().post(new PickerMessageEvent(MESSAGE_TYPE_DATE,checkedDate));
                                pvCustomTime.dismiss();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        checkedDate = format.format(date);
                    }
                })
                .setContentTextSize(17)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLineSpacingMultiplier(2.4f)
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setItemVisibleCount(3)
                .build();
//        .setTextXOffset(0, 0, 0, 40, 0, -40)
        pvCustomTime.show();
    }

    //显示地址选择器
    public void showLocationPicker() {// 弹出选择器
        if(!isLoaded){
            ToastUtil.makeShort(context,context.getResources().getString(R.string.loading_location_data));
            return;
        }
        isChanged = false;
        locationOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //

            }
        })
                .setLayoutRes(R.layout.picker_location, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.picker_location_inc_bg).findViewById(R.id.include_picker_tv_submit);
                        final TextView tvCancle = v.findViewById(R.id.picker_location_inc_bg).findViewById(R.id.include_picker_tv_cancle);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!isChanged){//没有改变默认选择第一项
                                    String firstLocation = options1Items.get(0).getPickerViewText() + "\t" +  options2Items.get(0).get(0);
                                    EventBus.getDefault().post(new PickerMessageEvent(MESSAGE_TYPE_PROVINCE,options1Items.get(0).getPickerViewText()));
                                    EventBus.getDefault().post(new PickerMessageEvent(MESSAGE_TYPE_CITY,options2Items.get(0).get(0)));
                                }else {
                                    EventBus.getDefault().post(new PickerMessageEvent(MESSAGE_TYPE_PROVINCE,checkedProvince));
                                    EventBus.getDefault().post(new PickerMessageEvent(MESSAGE_TYPE_CITY,checkedCity));
                                }
                                locationOptions.dismiss();
                            }
                        });
                    }
                })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String opt1tx = options1Items.size() > 0 ?
                                options1Items.get(options1).getPickerViewText() : "";

                        String opt2tx = options2Items.size() > 0
                                && options2Items.get(options1).size() > 0 ?
                                options2Items.get(options1).get(options2) : "";
//                        checkedLocation = opt1tx + "\t" + opt2tx;
                        checkedProvince = opt1tx;
                        checkedCity = opt2tx;
                        isChanged = true;
                    }
                })
                .setLineSpacingMultiplier(2.4f)//设置距离
                .setItemVisibleCount(3)//设置显示的数据数量
                .setContentTextSize(17)//设置标题字号
                .isRestoreItem(true)//是否每次从头开始显示数据
                .isAlphaGradient(true)
                .build();

        locationOptions.setPicker(options1Items, options2Items);
        locationOptions.show();
    }

    //初始化地区数据
    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(context, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
//                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
//                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
//                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
//            options3Items.add(province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    //解析地区json文件
    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    public void removeHandler(){
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

}
