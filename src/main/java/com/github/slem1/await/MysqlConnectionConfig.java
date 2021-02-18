package com.github.slem1.await;

/**
 * Plugin configuration for TCP connection
 *
 * @author dooley
 */
public class MysqlConnectionConfig implements MojoConnectionConfig {

    private String host;

    private int port;

    private String database;

    private String username;

    private String password;

    private String query;

    private int priority = Integer.MAX_VALUE;

    private boolean skip;

    /**
     * Default constructor used by maven.
     */
    public MysqlConnectionConfig() {

    }

    /**
     * Convenient constructor to create instance.
     *
     * @param host     mysql host.
     * @param port     mysql port.
     * @param priority the connection priority. 0 is the highest priority.
     * @param skip     whether to skip this connection check
     * @param database the mysql database
     * @param username the mysql username
     * @param password the mysql password
     * @param query the mysql query
     */
    public MysqlConnectionConfig(String host, Integer port, int priority, boolean skip, String database, String username, String password, String query) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.query = query;
        this.priority = priority;
        this.skip = skip;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isSkip() {
        return skip;
    };

//
//    protected String getHost() {
//        return host;
//    }
//
//    protected int getPort() {
//        return port;
//    }
//
//    protected String getDatabase() {
//        return database;
//    }
//
//    protected String getUsername() {
//        return username;
//    }
//
//    protected String getPassword() {
//        return password;
//    }
//
//    protected String getQuery() {
//        return query;
//    }

    @Override
    public Service buildService() {
        return new MysqlService(host, port, database, username, password, query);
    }
}
