package com.github.slem1.await;

/**
 * Plugin configuration for TCP connection
 *
 * @author slemoine
 */
public class TCPConnectionConfig implements MojoConnectionConfig {

    private String host;

    private Integer port;

    private int priority = Integer.MAX_VALUE;

    private boolean skip;

    /**
     * Default constructor used by maven.
     */
    public TCPConnectionConfig() {

    }

    /**
     * Convenient constructor to create instance.
     *
     * @param host     tcp host.
     * @param port     tcp port.
     * @param priority the connection priority. 0 is the highest priority.
     * @param skip     whether to skip this connection check
     */
    public TCPConnectionConfig(String host, Integer port, int priority, boolean skip) {
        this.host = host;
        this.port = port;
        this.priority = priority;
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
        return new TCPService(host, port);
    }
}
