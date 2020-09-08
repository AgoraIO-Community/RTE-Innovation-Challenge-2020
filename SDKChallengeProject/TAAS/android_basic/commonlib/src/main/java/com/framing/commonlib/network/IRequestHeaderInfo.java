package com.framing.commonlib.network;


import java.util.HashMap;

/**
 * header 添加处理接口
 */
public interface IRequestHeaderInfo {
    HashMap<String, String> getRequestHeaderMap();
    boolean isDebug();
}
