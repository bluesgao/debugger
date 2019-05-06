package com.github.bluesgao.debugger.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service {
    private String serviceName;//服务名称
    private int status;//服务状态
    private Map<String, Method> methods = new ConcurrentHashMap<String, Method>();
}
