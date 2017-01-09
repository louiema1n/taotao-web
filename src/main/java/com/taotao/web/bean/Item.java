package com.taotao.web.bean;

import org.apache.commons.lang3.StringUtils;

public class Item extends com.taotao.manage.pojo.Item {

    /**
     * 将父类中的字符串转化成字符数组
     * 
     * @return
     */
    public String[] getImages() {
        return StringUtils.split(super.getImage(), ',');
    }

}
