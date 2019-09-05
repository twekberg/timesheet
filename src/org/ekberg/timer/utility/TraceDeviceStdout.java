//  @(#) $Id:  $


package org.ekberg.timer.utility;


// ----------------------------------------------------------------------------
/**
 *  This class implements standard out for trace data
 **/
// ----------------------------------------------------------------------------
class TraceDeviceStdout extends TraceDevice {
    // ------------------------------------------------------------------------
    /**
     * This constructor passes its arguments to its base class and then gets
     * the config data for this specific type of trace device.
     *
     *  @param  name  user assigned name of the device.
     *  @param  filter  TraceFilter object to filter events through.
     *
     *  @see TraceDevice
     */
    // ------------------------------------------------------------------------
    TraceDeviceStdout(String name, TraceFilter filter) {
        super(name, filter);
    }
    

    //-------------------------------------------------------------------------
    /**
     * Send the trace string to the file.
     *
     * @param  traceStr  the message to output.
     */
    //-------------------------------------------------------------------------
    public void outputString(String traceStr) {
        System.out.println(traceStr);
    }
}
