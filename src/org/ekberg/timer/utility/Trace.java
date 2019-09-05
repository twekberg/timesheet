//  @(#) $Id:  $


package org.ekberg.timer.utility;


import org.ekberg.utility.Tracer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;


// ----------------------------------------------------------------------------
/**
 * This class implements a tracing facility for developers to use
 * to output data about the execution of a program.  Multiple
 * trace levels are supported: FATAL, ERROR, WARNING, NOTICE and INFO.
 *
 */
// ----------------------------------------------------------------------------
public class Trace implements TraceConstants, Runnable, Tracer {

    /** Fatal messages should indicate a grave condition in which the
     * programmer can not anticipate the integrity of the running system.
     **/
    public static final int FATAL = 1;

    /** Error messages should indicate a serious condition in which the
     * programmer has detected the problem and continue without system
     * integrity loss, usually with some special error-handling logic.
     **/
    public static final int ERROR = 2;

    /** Warning messages should indicate a condition in which the programmer
     * has detected an abnormal condition that does not affect processing.
     **/
    public static final int WARNING = 3;

    /** Notice messages should be used to indicate significant normal events in
     * a system, such as crossing a time boundary, completing some transaction
     * (or number of transactions), etc. **/
    public static final int NOTICE = 5;

    /** Profile measurement messages should be used to time specific
     * operations. Unlike the other levels, this one does not support tracing
     * objects. It does have multiple functions: the standard interface and
     * then two more for starting and ending a measure. **/
    public static final int PM = 7;

    /** Informational messages should be used for a programmer to help detect
     * problems by providing extensive data about a program's logic. **/
    public static final int INFO = 9;

    /** Monitor messages should be used for a programmer to help detect
     * problems with the monitor scanner/parser. **/
    public static final int MON = 10;

    
    /** This singleton allows the static methods to access the object. **/
    private static Trace instance = new Trace();

    /** This is the list of trace output device objects. **/
    private LinkedList<TraceDevice> deviceList = null;

    /** Queue of trace events from application contexts. */
    private LinkedList eventList = null; 
    
    /** The thread the Trace object runs on for dispatching events to devices.
     **/
    private Thread thread;

    /** Flag to indicate whether to use threaded mode. **/
    private boolean threadMode;
    
    /** Thread local storage for the profiling stack. */
    private static ThreadLocal<Stack<Date>> stackLocal = new ThreadLocal<Stack<Date>>();


    //==================================================================
    // Methods
    //==================================================================

    //-------------------------------------------------------------------------
    /**
     * Needed to allow a common class to use the Trace methods. The problem is
     * that the non-common code creates the Trace class so they can direct
     * trace output separately, but the common classes need to output Trace
     * messages when errors occur.
     *
     * @return
     *   Trace - the one and only Trace instance.
     */
    //-------------------------------------------------------------------------
    public static Trace getInstance() {
        return instance;
    }


    // ------------------------------------------------------------------------
    /**
     * Logs the given message and object marked with the given traceLevel.
     * traceLevel should be a constant defined above that denotes the severity
     * of the trace event.
     *
     * @param  traceLevel  trace level for this trace event.
     * @param  message  string to trace.
     * @param  object  object to trace.
     **/
    // ------------------------------------------------------------------------
    public static void trace(int traceLevel, String message, Object object) {
        instance.logTrace(traceLevel, message, object);
    }


    // ------------------------------------------------------------------------
    /**
     * Logs the given message marked with the given event type.  traceLevel is
     * assumed to be a constant defined in this class that defines the nature
     * of the given message.  For example, this could be FATAL, ERROR, WARNING,
     * etc.
     *
     * @param  traceLevel  user defined classification of log message.
     * @param  message  free form user message to log.
     **/
    // ------------------------------------------------------------------------
    public static void trace(int traceLevel, String message) {
        instance.logTrace(traceLevel, message, null);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated fatal error message and the object to the
     * configured log device.  This is equivalent to calling the log function
     * with FATAL as the event type.
     *
     * @param  message  to be logged to the log device.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    public static void fatal(String message, Object object) {
        instance.Fatal(message, object);
    }
    public void Fatal(String message, Object object) {
        logTrace(FATAL, message, object);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated fatal error message text to the configured log
     * device.  This is equivalent to calling the fatal function with a null
     * object.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public static void fatal(String message) {
        instance.Fatal(message);
    }
    public void Fatal(String message) {
        instance.logTrace(FATAL, message, null);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated error message and the object to the configured
     * log device.  This is equivalent to calling the log function with ERROR
     * as the event type.
     *
     * @param  message  to be logged to the log.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    public static void error(String message, Object object) {
        instance.Error(message, object);
    }
    public void Error(String message, Object object) {
        instance.logTrace(ERROR, message, object);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated error message text to the configured log device.
     * This is equivalent to calling the {@link #error(String, Object)}
     * function with a null object.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public static void error(String message) {
        instance.Error(message);
    }
    public void Error(String message) {
        instance.logTrace(ERROR, message, null);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated warning message and the object to the configured
     * log device.  This is equivalent to calling the log function with WARNING
     * as the event type.
     *
     * @param  message  to be logged to the log device.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    public static void warning(String message, Object object) {
        instance.Warning(message, object);
    }
    public void Warning(String message, Object object) {
        instance.logTrace(WARNING, message, object);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated warning message text to the configured log
     * device.  This is equivalent to calling the {@link #warning(String,
     * Object)} function with a null object.
     *
     * @param  message  to be logged to the log device.
     **/
     // -----------------------------------------------------------------------
    public static void warning(String message) {
        instance.Warning(message);
    }
    public void Warning(String message) {
        instance.logTrace(WARNING, message, null);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated notice message and the object to the configured
     * log device.  This is equivalent to calling the log function with NOTICE
     * as the event type.
     *
     * @param  message  to be logged to the log device.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    public static void notice(String message, Object object) {
        instance.Notice(message, object);
    }
    public void Notice(String message, Object object) {
        instance.logTrace(NOTICE, message, object);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated error message text to the configured log device.
     * This is equivalent to calling the {@link #notice(String, Object)}
     * function with a null object.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public static void notice(String message) {
        instance.Notice(message);
    }
    public void Notice(String message) {
        instance.logTrace(NOTICE, message, null);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated message text to the configured log device.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public static void pm(String message)
    {
        instance.logTrace(PM, "[PM] " + message, null);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated message text to the configured log device.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public static void pmStart(String message) {
        Date timestamp = new Date();

        Stack<Date> stack = (Stack<Date>)stackLocal.get();
        if (stack == null) {
            stack = new Stack<Date>();
            stackLocal.set(stack);
        }
        stack.push(timestamp);
        
        instance.logTrace(PM, "[PM START " + stack.size() + "] " 
                    + message, null);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated message text to the configured log device.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public static void pmStop(String message) {
        Date stopTimestamp = new Date();
        String timeDiff = "?";
        
        Stack stack = (Stack)stackLocal.get();
        if (stack == null) {
            instance.logTrace(ERROR, 
                        "### PROFILE STACK UNDERFLOW !! ###", null);
        }
        try {
            Date startTimestamp = (Date)stack.pop();
            timeDiff = "" + (stopTimestamp.getTime() - 
                        startTimestamp.getTime());
        }
        catch (EmptyStackException e) {
            instance.logTrace(ERROR, 
                        "### PROFILE STACK UNDERFLOW !! ###", null);
        }
        instance.logTrace(PM, "[PM STOP " + (stack.size() + 1) + "(" + timeDiff
                    + ")] " + message, null);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated informational message and the object to the
     * configured log device.  This is equivalent to calling the log function
     * with INFO as the event type.
     *
     * @param  message  to be logged to the log device.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    public static void info(String message, Object object) {
        instance.Info(message, object);
    }
    public void Info(String message, Object object) {
        instance.logTrace(INFO, message, object);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated error message text to the configured log device.
     * This is equivalent to calling the {@link #info(String, Object)} function
     * with a null object.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public static void info(String message) {
        instance.Info(message);
    }
    public void Info(String message) {
        instance.logTrace(INFO, message, null);
    }


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated informational message and the object to the
     * configured log device.  This is equivalent to calling the log function
     * with MON as the event type.
     *
     * @param  message  to be logged to the log device.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    public static void monitor(String message, Object object) {
        instance.logTrace(MON, message, object);
    }

    // ------------------------------------------------------------------------
    /**
     * This logs the indicated informational message and the object to the
     * configured log device.  This is equivalent to calling the log function
     * with MON as the event type.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public static void monitor(String message) {
        instance.logTrace(MON, message, null);
    }
    
    // ------------------------------------------------------------------------
    /**
     * This is the method called when the trace thread is started.  It waits
     * for a trace event to be queued and then removes it and dispatches it to
     * all registered devices.
     */
    // ------------------------------------------------------------------------
    public void run() {
        TraceEvent event = null;
        TraceDevice device = null;
        
        while(true) {
            do {
                event = null;
                // remove the new trace event from the event list
                synchronized(eventList) {
                    try {
                        event = (TraceEvent)eventList.removeFirst();
                    }
                    catch (Exception ignore) {}
                }
                if (event != null) {
                    for (int i = 0; i < deviceList.size(); i++) {
                        device = (TraceDevice)deviceList.get(i);
                        device.outputEvent(event);
                    }
                }
            } while (event != null); 

            // wait for a trace event to be added to the event list
            synchronized(this) {
                try {
                    wait(); 
                }
                catch (InterruptedException e) {
                    continue;
                }
            }
        }
    }
    

    // ------------------------------------------------------------------------
    /**
     *  Private constructor -- all public access is via static methods.
     */
    // ------------------------------------------------------------------------
    private Trace() {
        initialize();
        if (threadMode) {
            thread = new Thread(this, "Trace");
            thread.setDaemon(true);
            thread.setPriority(Thread.NORM_PRIORITY + 2);
            thread.start();
        }
    }


    // ------------------------------------------------------------------------
    /**
     * Logs the given message and object marked with the given traceLevel.
     * traceLevel is assumed to be a constant defined in this class that
     * defines the nature of the given message.  For example, this could be
     * FATAL, ERROR, WARNING, etc.
     *
     * @param  traceLevel  user defined classification of log message.
     * @param  message  free form user message to log.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    private void logTrace(int traceLevel, String message, Object object) {
        String stackTraceLine;
        Throwable throwableToTrace = null;

        // Get a Throwable for the stack traceback to get class, function,
        // line.
        Throwable throwable = new Throwable();
        if (object != null && throwable.getClass().isInstance(object)) {
            // The object being traced is a Throwable (probably an exception).
            throwableToTrace = (Throwable)object;
            
            // Check for whether there is a message - if not, just log
            // the exception and return.
            if (message == null || message.length() == 0) {
                logException(traceLevel, throwableToTrace);
                return;
            }

            // Since there is a message to go with this exception, continue to
            // trace just the message (by null'ing the object), saving
            // the throwableToTrace for after the message is already traced.
            object = null;
        }

        // Format the object into a string.
        String objString = "";
        if (object != null) {
            try {
                String tmpString = ":" + object.toString().trim();
                if (tmpString.length() > 1)
                    objString = tmpString;
            }
            catch (Exception e) {}
        }
        
        String msgString = "";
        // Check for empty message strings.
        if (message == null || message.length() == 0) {
            if (objString.length() == 0) {
                // Both message and the object are empty, replace with
                // something.
                msgString = "(empty trace)";
            }
            else {
                msgString = "";
            }
        }
        else {
            msgString = message;
        }
            
        
        // Collect the stack traceback (for the class, function & line) from
        // the Throwable into a StringWriter.
        StringWriter stackTrace = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stackTrace);
        throwable.printStackTrace(printWriter);
        
        // Since the Throwable outputs the stack trace in multiple lines, we
        // need to separate out the line we want, which is 3 calls into the
        // stack.
        stackTraceLine = getStackLine(stackTrace.toString(), 3);
        if (stackTraceLine.indexOf(".utility.Trace.") >= 0)
            if ((stackTraceLine.indexOf(".utility.Trace.fatal(Trace.java:") >= 0) ||
                        (stackTraceLine.indexOf(".utility.Trace.error(Trace.java:") >= 0) ||
                        (stackTraceLine.indexOf(".utility.Trace.warning(Trace.java:") >= 0) ||
                        (stackTraceLine.indexOf(".utility.Trace.notice(Trace.java:") >= 0) ||
                        (stackTraceLine.indexOf(".utility.Trace.info(Trace.java:") >= 0))
                // The user called Trace.info (which called Trace.Info), or similar
                // for the other methods.
                stackTraceLine = getStackLine(stackTrace.toString(), 4);
        instance.traceOutput(traceLevel, msgString + objString, stackTraceLine);
        
        // Now check to see if this trace call included an exception, and if
        // so, log the exception separately.
        if (throwableToTrace != null)
            logException(traceLevel, throwableToTrace);
    }


    // ------------------------------------------------------------------------
    /**
     * Logs the given exception marked with the given traceLevel.
     * traceLevel is assumed to be a constant defined in this class that
     * defines the nature of the given message.  For example, this could be
     * FATAL, ERROR, WARNING, etc.
     *
     * @param  traceLevel  user defined classification of log message.
     * @param  exception  object to log.
     **/
    // ------------------------------------------------------------------------
    private void logException(int traceLevel, Throwable exception) {
        // Collect the stack traceback (for the class, function & line) from
        // the Throwable into a StringWriter.
        StringWriter stackTraceWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stackTraceWriter);
        exception.printStackTrace(printWriter);
        
        StringBuffer stackTraceBuffer = stackTraceWriter.getBuffer();
        for (int i = 0; i < stackTraceBuffer.length(); i++) {
            while (stackTraceBuffer.charAt(i) == '\r' 
                        && i < stackTraceBuffer.length()) {
                stackTraceBuffer.deleteCharAt(i);
            }
        }

        String stackTrace = stackTraceBuffer.toString();
        // Since the Throwable outputs the stack trace in multiple lines, we
        // need to separate out the line we want in order to trace the function
        // where the exception occurred. This is always the first call into the
        // stack.
        String stackTraceLine = getStackLine(stackTrace, 1);
        instance.traceOutput(traceLevel, "EXCEPTION: " + stackTrace, stackTraceLine);
    }


    // ------------------------------------------------------------------------
    /**
     * Parses the given string into the line needed for extracting the caller's
     * info.  Since the Throwable outputs the stack trace in multiple lines, we
     * need to separate out the line we want and return it.
     *
     * @param  stackTrace  output from a Throwable's printStackTrace.
     * @param  depth  depth to dig back into the stackTrace.
     **/
    // ------------------------------------------------------------------------
    private String getStackLine(String stackTrace, int depth) {
        // Since the Throwable outputs the stack trace in multiple lines, we
        // need to separate out the line we want.
        String stackTraceLine = "";
        StringTokenizer st = new StringTokenizer(stackTrace, "\n\r");
        try {
            for (int i = 0; i <= depth; i++) {
                stackTraceLine = st.nextToken();

                // Get rid of the extra spaces and the "at " prefix.
                stackTraceLine = stackTraceLine.trim().substring(3);
            }
        }
        catch (Exception e) {
            // Ignore exceptions, line will be empty or last valid line.
        }
        return (stackTraceLine);
    }
        

    //-------------------------------------------------------------------------
    /**
     * Send the trace event to trace output devices.
     *
     * @param  traceLevel  defines the nature of the message. For example, this
     * could be FATAL, ERROR, WARNING, etc.
     *
     * @param  msgString  the message to output. Parts of this may have been
     * specified by the user.
     *
     * @param  stackTraceLine  stack line extracted from a Throwable.
     */
    //-------------------------------------------------------------------------
    private void traceOutput(int traceLevel, String msgString, String stackTraceLine) {
        TraceEvent event = null;
        TraceDevice device = null;
        int deviceListSize = deviceList.size();
            
        for (int i = 0; i < deviceListSize; i++) {
            device = (TraceDevice)deviceList.get(i);
            int deviceLevel = device.getLevel();
            if (traceLevel <= deviceLevel) {
                if (event == null) {
                    event = new TraceEvent(traceLevel, msgString, stackTraceLine);
                }
                device.outputEvent(event);
            }
        }
    }


    // ------------------------------------------------------------------------
    /**
     * Initialize the trace output device objects from the config file.
     **/
    // ------------------------------------------------------------------------
    private void initialize() {
        // Only initialize once.
        if (deviceList != null)
            return;

        TraceDevice device;
        TraceFilter filter;
        String deviceToken;
        String deviceType;

        deviceList = new LinkedList<TraceDevice>();
        eventList = new LinkedList();

        // Get trace device names.
        StringTokenizer st = null;
        try {
            st = new StringTokenizer(
                        Config.getString(CONFIG_PREFIX + CONFIG_DEVICES, "default"), 
                        " ");
        }
        catch (Throwable e) {
            System.err.println("Fatal error:");
            System.err.println("  Environment variable " + Config.APP_HOME +
                        " has an inappropriate value:");
            System.err.println("    Trying to get property " + CONFIG_PREFIX + CONFIG_DEVICES);
            System.err.println("    " + Config.APP_HOME + "=" +
                        System.getProperty(Config.APP_HOME, ""));
            e.printStackTrace();
            System.exit(1);
        }

        while (st.hasMoreTokens()) {
            deviceToken = st.nextToken();
            
            // Get trace device type -- default to standard out.
            deviceType = Config.getString(CONFIG_PREFIX + deviceToken + 
                        "." + CONFIG_DEVICE_TYPE, CONFIG_DEVICE_TYPE_STDOUT);

            filter = new TraceFilter(deviceToken);
            device = TraceDevice.create(deviceType, deviceToken, filter);
            
            deviceList.addLast(device);
        }
        
         // Get and set the thread mode.
        if (Config.getInt(CONFIG_PREFIX + CONFIG_THREAD_MODE, 0) == 1)
            threadMode = true;
        else
            threadMode = false;
    }


    // ------------------------------------------------------------------------
    /**
     * This main acts as a little test program.
     **/
    // ------------------------------------------------------------------------
    public static void main(String[] args) {
        String testStr = new String("TEST OBJECT");

        Trace.fatal("fatal(message)");
        Trace.fatal("fatal(message, object)", testStr);

        try {
            Thread.sleep(5000);
        }
        catch (Exception ignore) {}
        

        Trace.error("error(message)");
        Trace.error("error(message, object)", testStr);
        Trace.warning("warning(message)");
        Trace.warning("warning(message, object)", testStr);
        Trace.notice("notice(message)");
        Trace.notice("notice(message, object)", testStr);
        Trace.info("info(message)");
        Trace.info("info(message, object)", testStr);
        Trace.info("multi-line message - line 1\nmulti-line message - line 2\nmulti-line message - line 3");

        Trace.pm("pm(message)");
        Trace.pmStart("pmStart(message)");
        try {
            Thread.sleep(50);
        }
        catch (Exception ignore) {}
        Trace.pmStop("pmStop(message)");
        Trace.pmStart("pmStart(message)");
        try {
            Thread.sleep(50);
        }
        catch (Exception ignore) {}
        Trace.pmStart("pmStart(message)");
        try {
            Thread.sleep(50);
        }
        catch (Exception ignore) {}
        Trace.pmStop("pmStop(message)");
        Trace.pmStop("pmStop(message)");
        Trace.pmStop("pmStop(message) - underflow");

        try {
            String nullStr = null;
            nullStr = nullStr.toString();
        }
        catch (Exception e) {
            Trace.fatal("null ptr test", e);
        }
        try {
            nullPointer();
        }
        catch (Exception e) {
            Trace.fatal("subroutine null ptr test", e);
        }
        try {
            subroutine();
        }
        catch (Exception e) {
            Trace.fatal("nested subroutine null ptr test", e);
        }
        
        
        try {
            Thread.sleep(5000);
        }
        catch (Exception ignore) {}
        
        System.out.println("Exiting main loop");
    }


    // ------------------------------------------------------------------------
    /**
     * This is only used as a test case in the main function. Its only purpose
     * is to throw a null pointer exception.
     */
    // ------------------------------------------------------------------------
    private static void nullPointer() throws NullPointerException {
        Trace.info("inside nullPointer");
        String nullStr = null;

        nullStr = nullStr.toString();
        // Simulating the following:
        //     if (nullStr == null)
        //         throw (new NullPointerException());
    }
    

    // ------------------------------------------------------------------------
    /**
     * This is only used as a test case in the main function. Its only purpose
     * is to throw a null pointer exception nested down the stack a bit.
     */
    // ------------------------------------------------------------------------
    private static void subroutine() throws NullPointerException {
        Trace.info("inside subroutine");
        nullPointer();
    }

    // ------------------------------------------------------------------------
    /**
     * This is to set the log level for the device identified by name
     */
    // ------------------------------------------------------------------------
    public void setLogLevel(String name, int level) {
        TraceDevice device = null;
        
        for (int i = 0; i < deviceList.size(); i++) {
            device = (TraceDevice)deviceList.get(i);
            if (device.getName().equals(name)) {
                device.setLevel(level);
                return;
            }
        }
    }

    // ------------------------------------------------------------------------
    /**
     * This is to set the log level for the devices
     */
    // ------------------------------------------------------------------------
    public static void setLevel() {
        String deviceToken;
        int logLevel = 0;

        // Get trace device names.
        StringTokenizer st = new StringTokenizer(
            Config.getString(CONFIG_PREFIX + CONFIG_DEVICES, "default"), " ");

        while (st.hasMoreTokens()) {
            deviceToken = st.nextToken();
            logLevel = Config.getInt(CONFIG_PREFIX + deviceToken + "." + CONFIG_LEVEL, INFO);
            instance.setLogLevel(deviceToken, logLevel);
            Trace.notice("loglevel "+deviceToken+"="+logLevel);
        }
    }
}
