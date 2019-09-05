//  @(#) $Id:  $


package org.ekberg.timer.utility;


import java.io.File;


// ----------------------------------------------------------------------------
/**
 * This class implements a log file for trace data. Current log data is written
 * to the file name specified from the configuration file. Archive old log
 * files is managed by the ArchiveLog class.
 **/
// ----------------------------------------------------------------------------
class TraceDeviceFile extends TraceDevice {
    /** Use this to manage archiving the trace file. */
    ArchiveLog archive = null;

    String parentDirectory = null;

    // ---------------------------------------------------------------------
    /**
     * This constructor passes its arguments to its base class and then gets
     * the config data for this specific type of trace device.
     *
     * @param  name  user assigned name of the device.
     * @param  filter  TraceFilter object to filter events through.
     *
     * @see TraceDevice
     */
    // ---------------------------------------------------------------------
    TraceDeviceFile(String name, TraceFilter filter) {
        super(name, filter);

        // Get and set the log file name.
        String currentFileName = Config.getString(configPrefix + CONFIG_FILE_NAME, 
                    name + ".log");
        File simpleFile = new File(currentFileName);
      
        parentDirectory = simpleFile.getParent();
        archive = ArchiveLog.getInstance(parentDirectory,
                    simpleFile.getName());
    }


    // ---------------------------------------------------------------------
    /**
     * Send the trace string to the file.
     *
     * @param traceStr  the trace message to be written.
     **/
    // ---------------------------------------------------------------------
    public void outputString(String traceStr) {
        archive.outputString(traceStr);
    }
}
