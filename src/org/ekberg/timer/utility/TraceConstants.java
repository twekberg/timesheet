//  @(#) $Id:  $


package org.ekberg.timer.utility;


// ----------------------------------------------------------------------------
/**
 * Constants used by all Trace classes.
 */
// ----------------------------------------------------------------------------
interface TraceConstants {
    /** Default runtime trace level for filtering. */
    static final int RUNTIME_LEVEL = Trace.WARNING;
    
    /** Prefix for Trace classes to use when getting config data. */
    static final String CONFIG_PREFIX = "trace.";
    
    /** Trace config string for output device. */
    static final String CONFIG_DEVICES = "devices";
    
    /** Trace config string for output device type. */
    static final String CONFIG_DEVICE_TYPE = "type";
    
    /** Trace config string for level to output. */
    static final String CONFIG_LEVEL = "level";
    
    /** Trace config string for level to output. */
    static final String CONFIG_THREAD_MODE = "threaded";
    
    /** Trace config string for packages to include in log. */
    static final String CONFIG_INCL_PACKAGE = "include.package";
    
    /** Trace config string for classes to include in log. */
    static final String CONFIG_INCL_CLASS = "include.class";
    
    /** Trace config string for packages to exclude from log. */
    static final String CONFIG_EXCL_PACKAGE = "exclude.package";
    
    /** Trace config string for classes to exclude from log. */
    static final String CONFIG_EXCL_CLASS = "exclude.class";
    
    /** Trace config string for fields to output. */
    static final String CONFIG_INCL_FIELD = "fields";
    
    /** Trace config string for stdout device type. */
    static final String CONFIG_DEVICE_TYPE_STDOUT = "stdout";

    /** Trace config string for file device type. */
    static final String CONFIG_DEVICE_TYPE_FILE = "file";
    
    /** Trace config string for file name. */
    static final String CONFIG_FILE_NAME = "filename";
}
