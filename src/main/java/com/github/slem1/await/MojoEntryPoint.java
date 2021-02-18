package com.github.slem1.await;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The plugin entry point. Retrieves the plugin configuration and runs the underlying tasks.
 *
 * @author slemoine
 *
 */
@Mojo(name = "Await")
public class MojoEntryPoint extends AbstractMojo {

    private static final PollingConfig DEFAULT_POLLING_CONFIG = new PollingConfig();

    private static final Comparator<PollingTask> TASK_PRIORITY_COMPARATOR = new Comparator<PollingTask>() {
        @Override
        public int compare(PollingTask o1, PollingTask o2) {
            return Integer.compare(o1.getPriority(), o2.getPriority());
        }
    };

    @Parameter
    private List<TCPConnectionConfig> tcpConnections;

    @Parameter
    private List<HttpConnectionConfig> httpConnections;

    @Parameter
    private List<MysqlConnectionConfig> mysqlConnections;

    @Parameter
    private PollingConfig poll;

    @Parameter(defaultValue="false")
    private boolean skip;

    @Parameter(defaultValue="0")
    private int initialWait;

    //for unit testing
    void setTcpConnections(List<TCPConnectionConfig> tcpConnections) {
        this.tcpConnections = tcpConnections;
    }

    //for unit testing
    void setHttpConnections(List<HttpConnectionConfig> httpConnections) {
        this.httpConnections = httpConnections;
    }

    //for unit testing
    void setMysqlConnections(List<MysqlConnectionConfig> mysqlConnections) {
        this.mysqlConnections = mysqlConnections;
    }

    //for unit testing
    void setPoll(PollingConfig pollingConfig) {
        this.poll = pollingConfig;
    }

    //for unit testing
    void setSkip(boolean skip) {
        this.skip = skip;
    }

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoFailureException, MojoExecutionException {

        List<MojoConnectionConfig> configs = new ArrayList<>();

        if (tcpConnections != null) {
            configs.addAll(tcpConnections);
        }

        if (httpConnections != null) {
            configs.addAll(httpConnections);
        }

        if (mysqlConnections != null) {
            configs.addAll(mysqlConnections);
        }

        if (!skip) {

            try {
                List<PollingTask> tasks = toPollingTasks(configs);

                if (initialWait > 0) {
                    try {
                        Thread.sleep(initialWait);
                    } catch (InterruptedException ignored) {}
                }

                if (tasks.isEmpty()) {
                    getLog().warn("No tasks found");
                } else {
                    for (PollingTask task : tasks) {
                        task.run();
                    }

                }
            } catch (IllegalArgumentException e) {
                throw new MojoFailureException(e.getMessage(), e);
            }
        }
    }

    private List<PollingTask> toPollingTasks(List<MojoConnectionConfig> configs) {

        List<PollingTask> pollingTasks = new ArrayList<>();

        PollingConfig pollingConfig = poll == null ?
                DEFAULT_POLLING_CONFIG : poll.validate();

        for (MojoConnectionConfig config : configs) {
            Service service = config.buildService();

            if (config.isSkip()) {
                getLog().warn(String.format("Skipping task %s",service.toString()));
            } else {
                PollingTask task = new PollingTask(service,
                        pollingConfig.getAttempts(),
                        pollingConfig.getSleep(),
                        config.getPriority());
                pollingTasks.add(task);
            }
        }

        pollingTasks.sort(TASK_PRIORITY_COMPARATOR);

        return pollingTasks;
    }
}
