package com.springmodernisationlab.orders.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Startup guard adopted from the shared platform toolkit. It refuses to start
 * the service on a container that does not provide the expected servlet API,
 * so that misconfigured deployments fail fast instead of at first request.
 */
@Component
public class ServletContainerCompatibilityCheck implements InitializingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ServletContainerCompatibilityCheck.class);

    private static final String[] REQUIRED_SERVLET_TYPES = {
            "javax.servlet.http.HttpServletRequest",
            "javax.servlet.http.HttpServletResponse",
            "javax.servlet.Filter"
    };

    @Override
    public void afterPropertiesSet() {
        ClassLoader classLoader = getClass().getClassLoader();

        for (String typeName : REQUIRED_SERVLET_TYPES) {
            try {
                Class.forName(typeName, false, classLoader);
            } catch (ClassNotFoundException exception) {
                throw new IllegalStateException(
                        "Unsupported servlet container: required type " + typeName + " is not available",
                        exception);
            }
        }

        LOGGER.info("Servlet container compatibility check passed");
    }
}
