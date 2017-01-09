package com.taotao.web.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.manage.pojo.Item;

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Value("${TAOTAO_MANAGE_URL}")
    private static String TAOTAO_MANAGE_URL;
    
    public Item queryItemById(Long itemId) {
        String url = TAOTAO_MANAGE_URL +"/rest/api/item/" + itemId;
        try {
            String str = this.apiService.doGet(url );
            if (StringUtils.isEmpty(str)) {
                return null;
            }
            // 将json反序列化成对象返回
            return MAPPER.readValue(str, Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
