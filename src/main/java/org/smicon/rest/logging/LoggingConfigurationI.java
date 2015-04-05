package org.smicon.rest.logging;

public interface LoggingConfigurationI
{
	
	public static final boolean default_debug_enabled = false;
	public static final boolean default_info_enabled = true;
	public static final boolean default_warn_enabled = true;
	public static final boolean default_error_enabled = true;
	
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
	
    /**
     * @return true if the <b>debug</b> error level is enabled
     */
    boolean isDebugEnabled();

    /**
     * @return true if the <b>info</b> error level is enabled
     */
    boolean isInfoEnabled();

    /**
     * @return true if the <b>warn</b> error level is enabled
     */
    boolean isWarnEnabled();

    /**
     * @return true if the <b>error</b> error level is enabled
     */
    boolean isErrorEnabled();
    
}
