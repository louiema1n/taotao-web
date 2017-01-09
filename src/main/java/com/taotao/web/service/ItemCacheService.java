package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.service.RedisService;

@Service
public class ItemCacheService {

    @Autowired
    private RedisService redisService;

    /**
     * 删除redis缓存
     * 
     * @param itemId
     */
    public void del(Long itemId) {
        String key = ItemService.REDIS_ITEM_KEY + itemId;
        this.redisService.del(key);
    }

}
