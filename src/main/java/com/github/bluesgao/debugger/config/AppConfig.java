package com.github.bluesgao.debugger.config;

import java.util.Map;
import java.util.Set;

public class AppConfig implements Cloneable {
    //原始配置字符串
    private String rowCfg;
    //应用名称
    private String appName;
    //classname:set<methodname>
    private Map<String, Set<String>> monitorMethods;

    static {
        //todo 从命令行参数中获取配置信息
    }
}
