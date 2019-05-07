package com.github.bluesgao.asm.bytecode;

import com.github.bluesgao.asm.util.AgentUtils;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AccessFlag;

import java.util.List;

public class BytecodeWriter {
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
                    beforeCode.append(String.format("System.out.println(%s);", String.format("asm appName:%s, className:%s, methodName:%s, input:%s ", "testapp", className, methodName)));

                    final StringBuffer afterCode = new StringBuffer();
                    afterCode.append(String.format("System.out.println(%s);", String.format("asm appName:%s, className:%s, methodName:%s, output:%s ", "testapp", className, methodName)));

                    final StringBuffer catchCode = new StringBuffer();
                    afterCode.append(String.format("System.out.println(%s);", String.format("asm appName:%s, className:%s, methodName:%s, e:%s ", "testapp", className, methodName)));

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
}
