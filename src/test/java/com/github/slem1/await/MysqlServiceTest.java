package com.github.slem1.await;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;

@RunWith(JUnit4.class)
public class MysqlServiceTest {

    @Test
    public void shouldExecute() throws IOException, ServiceUnavailableException {
        MysqlService MysqlServiceChecker = new MysqlService("localhost", 3306, "", "root","changeit", "select 1");
        MysqlServiceChecker.execute();
    }

    @Test(expected = ServiceUnavailableException.class)
    public void shouldThrowServiceUnreachableException() throws IOException, ServiceUnavailableException {

        try (ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(0)) {
            int port = serverSocket.getLocalPort();
            serverSocket.close();

            MysqlService MysqlServiceChecker = new MysqlService("localhost", port, "mysql", "root","password", "select 1");
            MysqlServiceChecker.execute();
        }
    }

    @Test
    public void assertToString(){
        MysqlService MysqlService = new MysqlService("localhost", 3306, "mysql", "root","password", "select 1");
        Assert.assertEquals("jdbc:mysql://localhost:3306/mysql?user=root&password=****&q=select+1 (MySQL)", MysqlService.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCheckContructorArgsHost(){
        new MysqlService(null, 3306, "mysql", "root","password", "select 1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCheckContructorArgsDatabase(){
        new MysqlService("localhost", 3306, null, "root","password", "select 1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCheckContructorArgsUsername(){
        new MysqlService("localhost", 3306, "mysql", null,"password", "select 1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCheckContructorArgsPassword(){
        new MysqlService("localhost", 3306, "mysql", "root",null, "select 1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCheckContructorArgsQuery(){
        new MysqlService("localhost", 3306, "mysql", "root","password", null);
    }

}
