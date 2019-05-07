package com.github.bluesgao.debugger.agent;

import com.github.bluesgao.debugger.annotation.TraceClass;
import com.github.bluesgao.debugger.annotation.TraceMethod;
import com.github.bluesgao.debugger.util.AgentUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.bytecode.AccessFlag;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ClassTransformer implements ClassFileTransformer {
    private static final Logger LOGGER = Logger.getLogger(ClassTransformer.class.getCanonicalName());
    private static final Map<String, TraceMethod> TRACE_METHODS;
    private static final Map<ClassLoader, ClassPool> CLASS_POOL_MAP;

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        CtClass ctClass = null;
        try {
            ctClass = getCtClass(loader, classfileBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ctClass != null) {
            return transformByAnnotation(loader, ctClass);
        }
        return classfileBuffer;
    }

    /**
     * 基于注解的转换
     * @param loader
     * @param ctClass
     * @return
     */
    private byte[] transformByAnnotation(ClassLoader loader, CtClass ctClass) {
        try {
            //获取被监控的类
            TraceClass traceClass = getTraceClass(ctClass);
            //监控类的所有的声明方法
            CtMethod[] ctMethods = ctClass.getDeclaredMethods();
            //需要被监控的方法
            List<CtMethod> traceMethods = new ArrayList();

            //如果对类进行监控，意味着对类的所有方法进行监控
            if (traceClass != null) {
                for (int i = 0; i < ctMethods.length; i++) {
                    CtMethod m = ctMethods[i];
                    traceMethods.add(m);
                    //String key = ctClass.getName() + "$" + m.getName() + "$" + m.getSignature();
                    //TRACE_METHODS.put(key, m);
                }
            } else {//只对某个类的某些方法进行监控
                //获取被监控的方法
                for (int i = 0; i < ctMethods.length; i++) {
                    CtMethod m = ctMethods[i];
                    TraceMethod tm = getTraceMethod(m);
                    if (tm != null) {
                        traceMethods.add(m);
                        //类名$方法名$方法签名
                        //String key = ctClass.getName() + "$" + m.getName() + "$" + m.getSignature();
                        //TRACE_METHODS.put(key, tm);
                    }
                }
            }

            if (traceMethods.size() > 0) {
                return rewriteByteCode(loader, ctClass, traceMethods);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加监控语句
     *
     * @param ctClass
     * @param ctMethods
     * @param loader
     * @return
     * @throws Exception
     */
    private static byte[] rewriteByteCode(ClassLoader loader, CtClass ctClass, List<CtMethod> ctMethods) throws Exception {
        for (CtMethod ctMethod : ctMethods) {
            try {
                if (AccessFlag.isPackage(ctMethod.getModifiers())) {
                    final String className = ctClass.getName();
                    final String methodName = AgentUtils.getMethodDesc(ctMethod.getName(), ctMethod.getParameterTypes());

                    final StringBuffer beforeCode = new StringBuffer();
                    beforeCode.append(String.format("System.out.println(%s);", String.format("debugger appName:%s, className:%s, methodName:%s, input:%s ", "testapp", className, methodName)));

                    final StringBuffer afterCode = new StringBuffer();
                    afterCode.append(String.format("System.out.println(%s);", String.format("debugger appName:%s, className:%s, methodName:%s, output:%s ", "testapp", className, methodName)));

                    final StringBuffer catchCode = new StringBuffer();
                    afterCode.append(String.format("System.out.println(%s);", String.format("debugger appName:%s, className:%s, methodName:%s, e:%s ", "testapp", className, methodName)));

                    final CtClass throwableClass = ctClass.getClassPool().get(Throwable.class.getCanonicalName());
                    ctMethod.insertBefore(beforeCode.toString());
                    ctMethod.insertAfter(afterCode.toString(), false);
                    ctMethod.addCatch(catchCode.toString(), throwableClass);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        byte[] bytes = ctClass.toBytecode();
        ctClass.defrost();
        return bytes;
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

    public static ClassPool getClassPool(ClassLoader loader) {
        if (loader == null) {
            return new ClassPool(null);
        } else {
            ClassPool pool = CLASS_POOL_MAP.get(loader);
            if (pool == null) {
                pool = new ClassPool(true);
                pool.appendClassPath(new LoaderClassPath(loader));
                CLASS_POOL_MAP.put(loader, pool);
            }
            return pool;
        }
    }

    private CtClass getCtClass(ClassLoader classLoader, byte[] classFileBuffer) throws IOException {
        ClassPool classPool = getClassPool(classLoader);
        CtClass clazz = classPool.makeClass(new ByteArrayInputStream(classFileBuffer), false);
        clazz.defrost();
        return clazz;
    }


    static {
        CLASS_POOL_MAP = Collections.synchronizedMap(new WeakHashMap());
        TRACE_METHODS = new ConcurrentHashMap<String, TraceMethod>();
    }
}
