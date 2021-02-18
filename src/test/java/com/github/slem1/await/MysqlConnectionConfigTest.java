package com.github.slem1.await;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MysqlConnectionConfigTest {

    @Test
    public void shouldCreateDefaultInstance() {
        MysqlConnectionConfig mysqlConnectionConfig = new MysqlConnectionConfig();
        Assert.assertEquals(Integer.MAX_VALUE,mysqlConnectionConfig.getPriority());
//        Assert.assertEquals(3306, mysqlConnectionConfig.getPort());
//        Assert.assertEquals("SELECT 1",mysqlConnectionConfig.getQuery());
//        Assert.assertEquals("localhost",mysqlConnectionConfig.getHost());
//        Assert.assertEquals("",mysqlConnectionConfig.getDatabase());
        Assert.assertFalse(mysqlConnectionConfig.isSkip());
    }

    @Test
    public void shouldCreateInitializedInstance() {
        MysqlConnectionConfig mysqlConnectionConfig = new MysqlConnectionConfig("localhost", 3306, 0, true, "mysql", "root","password", "show tables");
        Assert.assertEquals(0, mysqlConnectionConfig.getPriority());
        Assert.assertTrue(mysqlConnectionConfig.isSkip());
        Service service = mysqlConnectionConfig.buildService();
        Assert.assertEquals(MysqlService.class, service.getClass());
    }
}
