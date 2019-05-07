package com.github.bluesgao.asm.config;

import com.github.bluesgao.asm.annotation.TraceClass;
import com.github.bluesgao.asm.annotation.TraceMethod;
import javassist.CtClass;
import javassist.CtMethod;

import java.util.Map;
import java.util.Set;

public class AnnotationConfig implements Config {
    //应用名称
    private String appName;
    //classname:set<methodname>
    private Map<String, Set<String>> monitorMethods;

    public Object getConfig() {
        return null;
    }

    /**
     * 获取被监控的类
     *
     * @param ctClass
     * @return
     */
    private TraceClass getTraceClass(CtClass ctClass) {
        TraceClass tc = null;
        try {
            tc = (TraceClass) ctClass.getAnnotation(TraceClass.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //接口不需要监控
        if (tc != null && !ctClass.isInterface()) {
            return tc;
        } else {
            return null;
        }
    }

    /**
     * 获取被监控的方法
     *
     * @param ctMethod
     * @return
     */
    private TraceMethod getTraceMethod(CtMethod ctMethod) {
        TraceMethod tm = null;
        try {
            tm = (TraceMethod) ctMethod.getAnnotation(TraceMethod.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tm;
    }
}
