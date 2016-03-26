package cz.kramolis.tests.mvc;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

/**
 * Application scoped bean.
 */
@ApplicationScoped
public class Messages {

    private Date startupTime = new Date();

    public String getGreeting() {
        return "Hello";
    }

    public Date getStartupTime() {
        return startupTime;
    }

}
