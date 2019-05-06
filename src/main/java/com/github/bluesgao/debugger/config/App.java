package com.github.bluesgao.debugger.config;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class App implements Cloneable {
    private String rowCfg;//原始配置字符串
    private String appName;//应用名称
    private Map<String, Service> services = new ConcurrentHashMap<String, Service>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("App{").append("appName='").append(appName).append("',");
        sb.append("services=[");
        Iterator i = services.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, Service> entry = (Map.Entry) i.next();
            sb.append("{").append(entry.getValue()).append("}");
            sb.append(",");
        }
        sb.append("]}");
        return sb.toString();
    }

    public String getRowCfg() {
        return rowCfg;
    }

    public void setRowCfg(String rowCfg) {
        this.rowCfg = rowCfg;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Map<String, Service> getServices() {
        return services;
    }

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }
}
