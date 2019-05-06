package com.github.bluesgao.debugger.config;

import java.util.Map;

public class Method {
    private String methodName;//方法名称
    private String code;//方法返回码
    private String codeDesc;//方法返回码描述
    private int status;//方法状态
    private Map<String, Object> inputs;//方法入参
    private Map<String, Object> outputs;//方法出参
}
