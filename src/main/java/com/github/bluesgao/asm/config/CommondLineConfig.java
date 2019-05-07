package com.github.bluesgao.asm.config;

import java.util.Map;
import java.util.Set;

/**
 * 从命令行参数中获取配置信息
 */
public class CommondLineConfig implements Config {
    //原始配置字符串
    private String rowCfg;
    //应用名称
    private String appName;
    //classname:set<methodname>
    private Map<String, Set<String>> monitorMethods;

    static {
        //todo 从命令行参数中获取配置信息
    }

    public Object getConfig() {
        return null;
    }
}
