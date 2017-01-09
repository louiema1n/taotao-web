package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.web.service.ItemCacheService;

@Controller
@RequestMapping("item/cache")
public class ItemCacheController {
    
    @Autowired
    private ItemCacheService itemCacheService;

    @RequestMapping(value = "{itemId}", method = RequestMethod.POST)
    public ResponseEntity<Void> delCache(@PathVariable("itemId") Long itemId) {
        try {
            this.itemCacheService.del(itemId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
