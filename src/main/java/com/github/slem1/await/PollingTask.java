package com.github.slem1.await;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * Adds the polling logic to a {@link Service} execution.
 *
 * @author slemoine
 */
public class PollingTask {

    private static Log log = new SystemStreamLog();

    private final int maxAttempt;

    private final int waitTime;

    private final int priority;

    private final Service service;

    /**
     * Configure the polling runner for the service {@code service}.
     *
     * @param service    the service to run.
     * @param maxAttempt the maximum number of attempts to run a service.
     * @param waitTime   the waiting time between two attempts.
     * @param priority   the priority of the this task.
     */
    PollingTask(final Service service, int maxAttempt, final int waitTime, final int priority) {

        if (service == null) {
            throw new IllegalArgumentException("Service is mandatory");
        }

        if (maxAttempt < 1) {
            throw new IllegalArgumentException("Polling task should execute at least once");
        }

        if (waitTime < 0) {
            throw new IllegalArgumentException("waitTime value cannot be negative");
        }

        if (priority < 0){
            throw new IllegalArgumentException("priority value must be equal to or greater than 0");
        }

        this.service = service;
        this.maxAttempt = maxAttempt;
        this.waitTime = waitTime;
        this.priority = priority;
    }

    /**
     * Returns the priority of the task.
     *
     * @return the priority value. The lesser the value is, the higher the task priority is.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Start the polling of the {@code service} after the delay period.
     *
     * @throws MojoExecutionException in case of severe execution failure.
     * @throws MojoFailureException   if the service is still unavailable after all the attempts.
     */
    void run() throws MojoExecutionException, MojoFailureException {

        int n = 0;

        do {
            try {
                service.execute();
                log.info(service + " is available");
                return;
            } catch (ServiceUnavailableException e) {
                log.info(e.getMessage());
                n++;
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e1) {
                    throw new MojoExecutionException("Unexpected thread exception", e1);
                }
            }

        } while (n < maxAttempt);

        throw new MojoFailureException(String.format("Service unreachable after %d attempts: %s",
                maxAttempt, service));

    }

}
