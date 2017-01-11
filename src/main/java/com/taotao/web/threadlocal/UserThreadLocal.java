package com.taotao.web.threadlocal;

import com.taotao.web.bean.User;

public class UserThreadLocal {

    private static final ThreadLocal<User> LOCAL = new ThreadLocal<User>();
    
    // 构造方法私有化(不能new)
    public UserThreadLocal() {
    }
    
    public static User get() {
        return LOCAL.get();
    }
    
    public static void set(User user) {
        LOCAL.set(user);
    }
    
}
