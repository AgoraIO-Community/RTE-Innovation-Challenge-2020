package com.framing.commonlib.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	/**
	 * 将字符串MD5加密
	 * @param msg
	 * @return
	 */
	public static String strToMd5(String msg){
		try {
			MessageDigest messDigest = MessageDigest.getInstance("MD5");
			
			byte[] md5Byte = messDigest.digest(msg.getBytes());
			
			return bytes2hex(md5Byte);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/** 
     * 将字节数组转成字符串类型（16进制）
     * @param bytes 
     * @return 
     */  
    public static String bytes2hex(byte[] bytes)
    {  
        final String HEX = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)  
        {  
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数  
            sb.append(HEX.charAt((b >> 4) & 0x0f));  
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数  
            sb.append(HEX.charAt(b & 0x0f));  
        }  
        return sb.toString();  
    }  
}
