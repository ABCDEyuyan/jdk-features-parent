package com.nf.mvc.configuration;

/**
 * @author cj
 */
@ConfigurationProperties("server")
public class ServerConfiguration {
    private int port = 8080;
    private String contextPath ="";
    private String urlPattern ="/";

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
