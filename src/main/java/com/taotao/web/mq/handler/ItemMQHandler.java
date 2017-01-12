package com.taotao.web.mq.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;

public class ItemMQHandler {
    
    @Autowired
    private RedisService redisService;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 接收队列消息，清空缓存，完成数据同步
     * 
     * @param msg
     */
    public void execute(String msg) {
        // 反序列化msg
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            long itemId = jsonNode.get("itemId").asLong();
            String key = ItemService.REDIS_ITEM_KEY + itemId;
            this.redisService.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
