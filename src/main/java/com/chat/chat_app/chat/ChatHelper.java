package com.chat.chat_app.chat;

import java.util.HashMap;
import java.util.Map;

public class ChatHelper {
    public  static  String extractValueFromStringCookie(String key,String stringCookie){
        if(stringCookie!=null){
            Map<String,String> keyValueMapCookies = new HashMap<>();
            String[] splitCookie = stringCookie.split(";");
            for (String cookie : splitCookie) {
                if (cookie.trim().contains("=")) {
                    String[] keyValCookie = cookie.split("=");
                    String k = keyValCookie.length>0?keyValCookie[0].trim():null;
                    String v = keyValCookie.length>1?keyValCookie[1].trim():null;
                    assert k!=null;
                    keyValueMapCookies.put(k,v);
                }
            }
            return keyValueMapCookies.get(key.trim());
        }
        return null;
    }
}
