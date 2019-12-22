package com.github.slem1.await;

import java.net.URL;

/**
 * Plugin configuration POJO for http service.
 *
 * @author slemoine
 */
public class HttpConnectionConfig implements MojoConnectionConfig {

    private URL url;

    private int statusCode;

    private int priority = Integer.MAX_VALUE;

    private boolean skipSSLCertVerification;

    private boolean skip;

    /**
     * Default constructor used by maven.
     */
    public HttpConnectionConfig() {

    }

    /**
     * Convenient constructor to create instance.
     *
     * @param url        url of the service.
     * @param statusCode expected response status code.
     * @param priority   the connection priority.
     * @param skipSSLCertVerification   https connections ignore certs.
     * @param skip       whether to skip this connection check
     */

    public HttpConnectionConfig(URL url, int statusCode, int priority, boolean skipSSLCertVerification, boolean skip) {
        this.url = url;
        this.statusCode = statusCode;
        this.priority = priority;
        this.skipSSLCertVerification = skipSSLCertVerification;
        this.skip = skip;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isSkip() {
        return skip;
    };

    @Override
    public Service buildService() {
        return new HttpService(url, statusCode, skipSSLCertVerification);
    }

}
