package com.taotao.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class IndexService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    @Value("${INDEX_AD1_URL}")
    private String INDEX_AD1_URL;

    @Value("${INDEX_AD2_URL}")
    private String INDEX_AD2_URL;

    /**
     * 大广告
     * 
     * @return
     */
    public String queryIndexAD1() {
        try {
            String url = TAOTAO_MANAGE_URL + INDEX_AD1_URL;
            String json = this.apiService.doGet(url);
            if (json == null) {
                return null;
            }
            // 反序列化json
            JsonNode jsonNde = MAPPER.readTree(json);
            ArrayNode rows = (ArrayNode) jsonNde.get("rows");
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            // 遍历rows中的每一个元素
            // "srcB": "http://image.taotao.com/images/2015/03/03/2015030304333327002286.jpg",
            // "height": 240,
            // "alt": "",
            // "width": 670,
            // "src": "http://image.taotao.com/images/2015/03/03/2015030304333327002286.jpg",
            // "widthB": 550,
            // "href": "http://sale.jd.com/act/xcDvNbzAqK0CoG7I.html?cpdad=1DLSUE",
            // "heightB": 240
            for (JsonNode row : rows) {
                // 添加到map
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("srcB", row.get("pic").asText());
                map.put("height", 240);
                map.put("alt", row.get("title").asText());
                map.put("width", 670);
                map.put("src", row.get("pic").asText());
                map.put("widthB", 550);
                map.put("href", row.get("url").asText());
                map.put("heightB", 240);
                list.add(map);
            }
            return MAPPER.writeValueAsString(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String queryIndexAD2() {
        try {
            String url = TAOTAO_MANAGE_URL + INDEX_AD2_URL;
            String json = this.apiService.doGet(url);
            if (json == null) {
                return null;
            }
            // 反序列化json
            JsonNode jsonNde = MAPPER.readTree(json);
            ArrayNode rows = (ArrayNode) jsonNde.get("rows");
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            // "width": 310,
            // "height": 70,
            // "src": "/images/5440ce68Na00d019e.jpg",
            // "href":"http://c.fa.jd.com/adclick?sid=2&cid=601&aid=3614&bid=4196&unit=35984&advid=109277&guv=&url=http://sale.jd.com/mall/FQLUNlG53wbX7m.html",
            // "alt": "",
            // "widthB": 210,
            // "heightB": 70,
            // "srcB":"http://img14.360buyimg.com/da/jfs/t334/155/1756719493/14371/e367c503/5440ce6dNd056ce39.jpg"
            for (JsonNode row : rows) {
                // 添加到map
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("width", 310);
                map.put("height", 70);
                map.put("src", row.get("pic").asText());
                map.put("href", row.get("url").asText());
                map.put("alt", row.get("title").asText());
                map.put("widthB", 210);
                map.put("heightB", 70);
                map.put("srcB", row.get("pic").asText());
                list.add(map);
            }
            return MAPPER.writeValueAsString(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
