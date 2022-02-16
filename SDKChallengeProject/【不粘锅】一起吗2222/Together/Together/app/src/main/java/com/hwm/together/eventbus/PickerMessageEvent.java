package com.hwm.together.eventbus;

/**
 * <Picker选择器消息通知类>
 *
 * @author: hanweiming
 * @date: 2020/6/17
 */
public class PickerMessageEvent {

    private int pickerType;//1-性别，2-行业，3-现居地，4-日期
    private String message;

    public int getPickerType() {
        return pickerType;
    }

    public void setPickerType(int pickerType) {
        this.pickerType = pickerType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PickerMessageEvent(int pickerType, String message) {
        this.pickerType = pickerType;
        this.message = message;
    }
}
