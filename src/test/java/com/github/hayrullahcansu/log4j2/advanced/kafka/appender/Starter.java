package com.github.hayrullahcansu.log4j2.advanced.kafka.appender;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Starter {
    private static final Logger logger = LogManager.getLogger("XMLConfigTest");

    public static void main(String args[]) {
        System.out.println("test started");
        logger.debug("Debug Message Logged !!!");
        logger.info("Info Message Logged !!!");
        logger.error("Error Message Logged !!!");
        System.out.println("test stopped");

    }
}
