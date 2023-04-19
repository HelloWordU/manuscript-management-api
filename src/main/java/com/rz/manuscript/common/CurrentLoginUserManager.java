package com.rz.manuscript.common;

import com.rz.manuscript.entity.User;

import java.util.concurrent.ConcurrentHashMap;

public class CurrentLoginUserManager {

    public  static ConcurrentHashMap<Integer, User> currentLoginUser = new ConcurrentHashMap<>();
    public  static ConcurrentHashMap<Integer, User> currentLoginCustomer = new ConcurrentHashMap<>();
}
