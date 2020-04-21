package com.baibei.shiyi.gateway.websocket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by keegan on 04/08/2017.
 */
@Component
public class ChannelMessageCache {
    @Getter
    @Setter
    private volatile JSONObject marketData;
    @Getter
    @Setter
    private volatile JSONObject topOrderPrice4Buy;
    @Getter
    @Setter
    private volatile JSONObject topOrderPrice4Sell;
    @Getter
    @Setter
    private volatile Map<String,JSONArray> quoteData = new HashMap<String,JSONArray>();
}
