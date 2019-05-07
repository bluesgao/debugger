package com.github.bluesgao.asm;

import com.github.bluesgao.asm.transformer.AsmTransformer;

import java.lang.instrument.Instrumentation;

public class AsmAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        init(agentArgs, inst);
    }

    private static void init(String agentArgs, Instrumentation inst) {
        System.out.println("AsmAgent init...");
        inst.addTransformer(new AsmTransformer(), true);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                //todo 应用关闭,清理工作
                System.out.println("应用关闭钩子函数执行...");
            }
        }));
    }
}
