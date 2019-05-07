package com.github.bluesgao.asm.transformer;

import com.github.bluesgao.asm.annotation.TraceClass;
import com.github.bluesgao.asm.annotation.TraceMethod;
import com.github.bluesgao.asm.bytecode.BytecodeWriter;
import com.github.bluesgao.asm.util.AgentUtils;
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

public class AsmTransformer implements ClassFileTransformer {
    private static final Logger LOGGER = Logger.getLogger(AsmTransformer.class.getCanonicalName());
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
            //return transformByAnnotation(loader, ctClass);
        }
        return classfileBuffer;
    }

    /**
     * 基于注解的转换
     * @param loader
     * @return
     */
 /*   private byte[] transformByAnnotation(ClassLoader loader, CtClass ctClass) {
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
                return BytecodeWriter.rewriteByteCode(loader, ctClass, traceMethods);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
*/




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
