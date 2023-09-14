package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http2.Http2Protocol;
import java.io.File;

@Slf4j
public class Main {
    private static final String DEFAULT_SERVLET = "DEFAULT";
    private static final String DISPLAY_NAME = "Grpc-Client";
    private static final AppConfig config = AppConfigReader.getAppConfig();

    public static void main(String[] args) throws LifecycleException {
        var workingDirPath = ".";
        var file  = new File(workingDirPath);
        var hostName = "localhost";
        var port = config.httpPort();
        Tomcat tomcat = new Tomcat();
        tomcat.setHostname(hostName);
        tomcat.setPort(port);
        var connector = tomcat.getConnector();
        setConnectorAttributes(connector);
        var context = tomcat.addContext("/grpc_client", file.getAbsolutePath());
        Tomcat.addServlet(context, DEFAULT_SERVLET, new DefaultServlet());
        Tomcat.addServlet(context, "ServerSubsetServlet", new ServerSubsetTestServlet());
        context.addServletMappingDecoded("/", DEFAULT_SERVLET);
        context.addServletMappingDecoded("/serverSubset", "ServerSubsetServlet");
        context.setDisplayName(DISPLAY_NAME);
        context.addLifecycleListener(new Tomcat.FixContextListener());
        context.addApplicationListener(ApplicationListener.class.getName());
        tomcat.start();
        log.info("Started Tomcat Server on port: {}", port);
        tomcat.getServer().await();
    }

    private static void setConnectorAttributes(Connector connector) {
        connector.setProperty("maxThreads", config.httpMaxThread());
        connector.setProperty("acceptCount", "20");
        connector.setProperty("minSpareThreads", config.httpMinSpareThread());
        connector.addUpgradeProtocol(new Http2Protocol());
        connector.setProperty("keepAliveTimeout", "-1");
        connector.setProperty("maxKeepAliveRequests", "-1");
        connector.setProperty("connectionTimeout", config.httpConnectionTimeout());
    }
}