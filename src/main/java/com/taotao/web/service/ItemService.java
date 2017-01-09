package com.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.bean.Item;

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;
    
    @Autowired
    private RedisService redisService;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    public static final String REDIS_ITEM_KEY = "TAOTAO_WEB_ITEM_";
    public static final String REDIS_ITEM_DESC_KEY = "TAOTAO_WEB_ITEM_DESC_";
    private static final Integer REDIS_TIME = 60 * 60 * 24;
    
    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;
    
    public Item queryItemById(Long itemId) {
        
        String key = REDIS_ITEM_KEY + itemId;
        try {
            // 从redis缓存中命中
            String cacheDate = this.redisService.get(key );
            if (StringUtils.isNotEmpty(cacheDate)) {
                // 命中
                return MAPPER.readValue(cacheDate, Item.class);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        String url = TAOTAO_MANAGE_URL +"/rest/api/item/" + itemId;
        try {
            String str = this.apiService.doGet(url );
            if (StringUtils.isEmpty(str)) {
                return null;
            }
            
            try {
                // 存入redis缓存
                this.redisService.set(key, str, REDIS_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // 将json反序列化成对象返回
            return MAPPER.readValue(str, Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemDesc queryItemDescById(Long itemId) {
        
        String key = REDIS_ITEM_DESC_KEY + itemId;
        try {
            // 从redis缓存中命中
            String cacheDate = this.redisService.get(key );
            if (StringUtils.isNotEmpty(cacheDate)) {
                // 命中
                return MAPPER.readValue(cacheDate, ItemDesc.class);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        String url = TAOTAO_MANAGE_URL +"/rest/api/item/desc/" + itemId;
        try {
            String str = this.apiService.doGet(url );
            if (StringUtils.isEmpty(str)) {
                return null;
            }
            
            try {
                // 存入redis缓存
                this.redisService.set(key, str, REDIS_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // 将json反序列化成对象返回
            return MAPPER.readValue(str, ItemDesc.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
