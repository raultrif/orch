package com.colt.orchestrator.engine;

import org.apache.camel.spring.Main;

/**
 * A Main to let you easily start the application from a IDE.
 * Usually you can just right click and choose Run
 *
 * @version 
 */
public final class Orchestrator extends Main {
    public static void main(String ... args) throws Exception {
        Orchestrator main = new Orchestrator();
        main.setApplicationContextUri("classpath:META-INF/spring/application-context.xml");
        main.enableHangupSupport();
        main.run();
    }
}
