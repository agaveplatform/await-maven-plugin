package com.github.slem1.await;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;

/**
 * Handler for testing availability of a MySQL server. Ensures that a jdbc connection can be opened
 * and a given query run against a specific host and port with a given username and password.
 *
 * @author dooley
 */
public class MysqlService implements Service {

    private final String host;

    private final Integer port;

    private final String database;

    private final String username;

    private final String password;

    private final String query;

    /**
     * Create new instance configured with {@code host} and {@code port}.
     *
     * @param host     mysql host.
     * @param port     mysql port.
     * @param database the mysql database
     * @param username the mysql username
     * @param password the mysql password
     * @param query the mysql query
     */
    public MysqlService(final String host, final Integer port, final String database, final String username, final String password, final String query) {

        if (port == null) {
            throw new IllegalArgumentException("MySQL port is mandatory");
        }

        if (host == null) {
            throw new IllegalArgumentException("MySQL host is mandatory");
        }

        if (database == null) {
            throw new IllegalArgumentException("MySQL database is mandatory");
        }

        if (username == null) {
            throw new IllegalArgumentException("MySQL username is mandatory");
        }

        if (password == null) {
            throw new IllegalArgumentException("MySQL password is mandatory");
        }

        if (query == null) {
            throw new IllegalArgumentException("MySQL query is mandatory");
        }

        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.query = query;
    }

    @Override
    public String toString() {
        return String.format("jdbc:mysql://%s:%d/%s?user=%s&password=****&q=%s (MySQL)",
                host, port, database, username, getURLEncodedQuery());
    }


    @Override
    public void execute() throws ServiceUnavailableException {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            conn = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s",
                    host, port, database, username, password));

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
        }
        catch (Throwable ex) {
            throw new ServiceUnavailableException(String.format(
                    "jdbc:mysql://%s:%d/%s?user=%s&password=****&q=%s (MySQL) is unreachable",
                    host, port, database, username, getURLEncodedQuery()), ex);
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {}

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignored) {}

                stmt = null;
            }
        }
    }

    /**
     * Encodes the query, falling back on the original query if an exception is thrown
     * @return url encoded query.
     */
    protected String getURLEncodedQuery() {
        String q = query;
        try { q = URLEncoder.encode(query, "UTF-8"); } catch (UnsupportedEncodingException ignored) {}

        return q;
    }
}
