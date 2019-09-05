//  @(#) $Id:  $


package org.ekberg.timer.utility;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;


// ----------------------------------------------------------------------------
/**
 * This class contains the data associated with one occurrence of a trace call
 * in a program.
 **/
// ----------------------------------------------------------------------------
public class TraceEvent implements Serializable {
    /** Needed by Serializable to identify the version number. */
    private static final long serialVersionUID = 1;

    /** The trace level for this event. */
    int traceLevel;

    /** Time when this event was created. */
    String timeStamp;
    
    /** Name of the current thread. */
    String threadName;

    /** This option may be specified by the user to associate an event with a
     * running counter. */
    long seqNumber;

    /** The message that the user wants to trace. */
    String message;

    /** A line number from a stack trace. */
    int lineNumber;

    /** A Java file name from a stack trace.*/
    String fileName;

    /** A Java function (method) name from a stack trace. */
    String function;

    /** A Java Class name from a stack trace. */
    String className;

    /** The full package name from a stack trace. */
    String packageName;

    /** Same as packageName, except one level up. */
    String basePackageName;
    
    /** Used to initialize SeqNumber to a unique value. */
    private static long nextSeqNumber = 1;

    private static final String dateFormatString = "MM/dd|HH:mm:ss.SSS";
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);


    // ------------------------------------------------------------------------
    /**
     * Create a TraceEvent.
     *
     * @param  traceLevel  trace level for this trace event.
     * @param  message  string to trace.
     * @param  stackLine  stack line extracted from a Throwable.
     **/
    // ------------------------------------------------------------------------
    public TraceEvent(int traceLevel, String message, String stackLine) {
        this.traceLevel = traceLevel;
        this.message = message;
        timeStamp = dateFormat.format(new Date());
        if (timeStamp.length() > dateFormatString.length())
            timeStamp = timeStamp.substring(0, dateFormatString.length());
        seqNumber = nextSeqNumber++;
        parseStackLine(stackLine);
        threadName = Thread.currentThread().getName();
    }


    // ------------------------------------------------------------------------
    /**
     * Breaks the given line into a class name, line number, function, etc.
     * Presumably, the line corresponds to a line from a stack trace.
     * 
     * @param  raw  stack line extracted from a Throwable.
     **/
    // ------------------------------------------------------------------------
    private void parseStackLine(String raw) {
        // Throughout this function, error conditions such as expected
        // delimiters that are not present is handled by catching the generated
        // indexing exceptions on the string class rather than handling every
        // error.

        lineNumber = 0;
        fileName   = "";
        function   = "";
        className  = "";
        
        try {
            // First, start at the end of the string and search
            // backwards to capture the line number.
            String lineNumberString = raw.substring(
                        raw.lastIndexOf(':') + 1,
                        raw.length() - 1);
            lineNumber = Integer.valueOf(lineNumberString).intValue();
        }
        catch (Exception e) {}

        int startFileName = 0;
        try {
            // Continue backward, for the filename.
            startFileName = raw.lastIndexOf('(');
            int startLineNumber = raw.indexOf(':', startFileName);
            if (startLineNumber <= 0) 
                startLineNumber = raw.length() - 1;
            fileName = raw.substring(startFileName + 1, startLineNumber);
        }
        catch (Exception ignore) {}

        int startFunction = 0;
        try {
            // Continue backward for the function name.
            startFileName = raw.lastIndexOf('(');
            startFunction = raw.lastIndexOf('.', startFileName);
            function = raw.substring(startFunction + 1,
                        startFileName);
        }
        catch (Exception ignore) {}

        int startClassName = 0;
        String fullClassName = null;
        try {
            // Continue backward for the class name.
            fullClassName = raw.substring(0, startFunction);
            startClassName = fullClassName.lastIndexOf('.');

            // Fix if we ran off the front of the string.
            if (startClassName == -1) 
                startClassName = 0;
            else 
                ++startClassName;
                    
            className = fullClassName.substring(startClassName, startFunction);
            // Continue backward for the package name.
            packageName = fullClassName.substring(0, startClassName - 1);
            basePackageName = packageName.substring(packageName.lastIndexOf('.') + 1);
        }
        catch (Exception ignore) {}
    }


    // ------------------------------------------------------------------------
    /**
     * This main acts as a little test program.
     **/
    // ------------------------------------------------------------------------
    public static void main(String[] args) 
    {
        TraceEvent event = new TraceEvent(1, "test event",
                    "org.ekberg.meth.utility.TraceEvent.main(Trace.java:173)");
        System.out.println("Event=" + event);
    }
}
