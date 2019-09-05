//  @(#) $Id:  $


package org.ekberg.timer.utility;


import java.util.StringTokenizer;

// ----------------------------------------------------------------------------
/**
 * This class contains the abstract trace device from which specific devices
 * are subclassed.
 *
 * @see Trace
 **/
// ----------------------------------------------------------------------------
abstract class TraceDevice implements TraceConstants {
    /** This is the trace filter object. **/
    private TraceFilter filter = null;
    
    /** Name of this device object. **/
    protected String name;

    /** Config prefix for this device. **/
    protected String configPrefix;


    // ------------------------------------------------------------------------
    /**
     * This constructor takes the name of the trace device (as defined in a
     * config file entry) and the filter to use.
     *
     * @param  name  user assigned name of the device.
     * @param  filter  TraceFilter object to filter events through.
     *
     * @see TraceFilter
     */
    // ------------------------------------------------------------------------
    TraceDevice(String name, TraceFilter filter) {
        this.name = name;
        this.filter = filter;
        configPrefix = CONFIG_PREFIX + name + ".";
    }


    // ------------------------------------------------------------------------
    /** 
     * Creates a particular TraceDevice depending on the type.
     *
     * @param  type  the type of TraceDevice needed. Values are
     * CONFIG_DEVICE_TYPE_STDOUT and CONFIG_DEVICE_TYPE_FILE.
     *
     * @param  name  user assigned name of the device.
     * @param  filter  TraceFilter object to filter events through.
     */
    // ------------------------------------------------------------------------
    static TraceDevice create(String type, String name, TraceFilter filter) {
        TraceDevice traceDevice = null;

        if (type.equalsIgnoreCase(CONFIG_DEVICE_TYPE_STDOUT)) {
            traceDevice = new TraceDeviceStdout(name, filter);
        }
        else if (type.equalsIgnoreCase(CONFIG_DEVICE_TYPE_FILE)) {
            traceDevice = new TraceDeviceFile(name, filter);
        }
        else {
            // Default is to return a file type.
            traceDevice = new TraceDeviceFile(name, filter);
        }
        return traceDevice;
    }
    

    //-------------------------------------------------------------------------
    /**
     * Return the trace level for this device.
     *
     * @return
     *   int - the trace level.
     */
    //-------------------------------------------------------------------------
    public int getLevel() {
        return filter.getLevel();
    }


    // ------------------------------------------------------------------------
    /**
     * Sends the trace event output through the filter and if it passed the
     * filter, calls the abstract outputString method.
     *
     *  @param  event  TraceEvent object.
     *
     *  @see TraceEvent
     */
    // ------------------------------------------------------------------------
    void outputEvent(TraceEvent event) {
        // Go through the filter.
        if (filter != null) {
            if (!filter.isEventWanted(event)) {
                return;
            }
        }
            
        StringBuffer tracePrefix = new StringBuffer();
        if (filter != null) {
            if (filter.isFieldToPrint("timeStamp"))
                tracePrefix.append(event.timeStamp + "|");
            if (filter.isFieldToPrint("seqNumber"))
                tracePrefix.append(event.seqNumber + "|");
            if (filter.isFieldToPrint("packageName"))
                tracePrefix.append(event.packageName + "|");
            if (filter.isFieldToPrint("threadName"))
                tracePrefix.append(event.threadName + "|");
            if (filter.isFieldToPrint("basePackageName"))
                tracePrefix.append(event.basePackageName + "|");
            if (filter.isFieldToPrint("className"))
                tracePrefix.append(event.className + "|");
            if (filter.isFieldToPrint("fileName"))
                tracePrefix.append(event.fileName + "|");
            if (filter.isFieldToPrint("function"))
                tracePrefix.append(event.function + "|");
            if (filter.isFieldToPrint("lineNumber"))
                tracePrefix.append(event.lineNumber + "|");
            if (filter.isFieldToPrint("traceLevel"))
                tracePrefix.append(event.traceLevel + "|");
        }
        else {
            // By default print all fields.
            tracePrefix.append(event.timeStamp + "|");
            tracePrefix.append(event.seqNumber + "|");
            tracePrefix.append(event.packageName + "|");
            tracePrefix.append(event.threadName + "|");
            tracePrefix.append(event.basePackageName + "|");
            tracePrefix.append(event.className + "|");
            tracePrefix.append(event.fileName + "|");
            tracePrefix.append(event.function + "|");
            tracePrefix.append(event.lineNumber + "|");
            tracePrefix.append(event.traceLevel + "|");
        }
        
        StringTokenizer st = new StringTokenizer(event.message, "\n");
        if (!st.hasMoreTokens()) {
            outputString(tracePrefix.toString());
        }
        else {
            while (st.hasMoreTokens()) {
                outputString(tracePrefix.toString() + st.nextToken());
            }
        }
    }


    // ------------------------------------------------------------------------
    /**
     * Abstract method to output a trace string to the physical device. This
     * method must be overloaded by a subclass that represents the device
     *
     *  @param  traceString  String to output.
     */
    protected abstract void outputString(String traceString);

    // ------------------------------------------------------------------------
    /**
     * Returns the device name
     */
    // ------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    // ------------------------------------------------------------------------
    /**
     * Set trace level 
     */
    // ------------------------------------------------------------------------
    public void setLevel(int level) {
        filter.setTraceLevel(level);
    }

    //-------------------------------------------------------------------------
    /**
     * Pretty printer for TraceDevice.
     *
     * @return
     *   String - nicely formatted for debugging.
     */
    //-------------------------------------------------------------------------
    public String toString() {
        return "TraceDevice[" +
                    "name=" + name +
                    "]";
    }
}
