package org.smicon.rest.data.logging;

public interface MutableLoggingConfigurationI
extends
LoggingConfigurationI
{
	
    /**
     * Set if the <b>debug</b> log level is enabled
     */
    void setDebugEnabled(boolean aIsEnabled);

    /**
     * Set if the <b>info</b> log level is enabled
     */
    void setInfoEnabled(boolean aIsEnabled);

    /**
     * Set if the <b>warn</b> log level is enabled
     */
    void setWarnEnabled(boolean aIsEnabled);

    /**
     * Set if the <b>error</b> log level is enabled
     */
    void setErrorEnabled(boolean aIsEnabled);
    
}
