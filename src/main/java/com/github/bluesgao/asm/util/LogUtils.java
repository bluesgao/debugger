package com.github.bluesgao.asm.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtils {
    public static final String ASM_AGENT_ERROR_PREFIX = "[AGENT ERROR]";
    public static final String ASM_AGENT_INFO_PREFIX = "[AGENT INFO]";

    public static void warn(Logger logger, String msg, Throwable thrown) {
        logger.log(Level.WARNING, "[AGENT ERROR]" + msg, thrown);
    }

    public static void warn(Logger logger, String msg) {
        logger.log(Level.WARNING, "[AGENT ERROR]" + msg);
    }

    public static void info(Logger logger, String msg) {
        logger.log(Level.INFO, "[AGENT INFO]" + msg);
    }

    public static void debug(Logger logger, String msg) {
        logger.log(Level.FINE, "[AGENT INFO]" + msg);
    }
}
