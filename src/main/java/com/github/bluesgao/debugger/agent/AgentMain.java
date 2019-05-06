package com.github.bluesgao.debugger.agent;

import java.lang.instrument.Instrumentation;

public class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        init(agentArgs, inst);
    }

    private static void init(String agentArgs, Instrumentation inst) {
        System.out.println("AgentMain init...");
        inst.addTransformer(new ClassTransformer(), true);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                //todo 应用关闭,清理工作
                System.out.println("应用关闭钩子函数执行...");
            }
        }));
    }
}
