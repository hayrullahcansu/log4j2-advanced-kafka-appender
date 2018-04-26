package org.apache.logging.log4j.kafka.appender;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Starter {
    private static final Logger logger = LogManager.getLogger("XMLConfigTest");

    public static void main(String args[]) {
        System.out.println("test started");
        /*Appender appender= org.apache.log4j2.kafka.appender.AdvancedKafkaAppender.createAppender("Hayro",null, null,null,false);
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        config.addAppender(appender);
        appender.start();
        AppenderRef ref = AppenderRef.createAppenderRef("Hayro", null, null);
        AppenderRef[] refs = new AppenderRef[] {ref};
        LoggerConfig loggerConfig = LoggerConfig.createLogger(false,Level.ALL, org.apache.logging.log4j.kafka.appender.Starter.class.getName(),"org.apache.logging.log4j",refs,null,config,null);
        loggerConfig.addAppender(appender, null, null);
        config.addLogger(org.apache.logging.log4j.kafka.appender.Starter.class.getName(), loggerConfig);
        ctx.updateLoggers();
        */
        logger.debug("Debug Message Logged !!!");
        logger.info("Info Message Logged !!!");
        logger.error("Error Message Logged !!!");
        System.out.println("test stopped");
        return;
    }
}
