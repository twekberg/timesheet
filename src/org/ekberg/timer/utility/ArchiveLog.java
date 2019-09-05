//  @(#) $Id:  $


package org.ekberg.timer.utility;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;


// ----------------------------------------------------------------------------
/**
 * This class implements a time stamped log file. Current log data is
 * written to the file name specified via the constructor. Old log data is
 * located in a file with the same base name, but the base name is prepended
 * with a YYYY-MM-DD timestamp.
 * To prevent rename problems, when a log message is to be written, the log
 * file is opened, the message is written and the log file is closed.
 * <P>
 * To get an ArchiveLog object call the getInstance method. When a message is
 * to be written to the log file, construct the string and call the
 * outputString method. Nothing is added to the string except a newline, so be
 * sure and include timestamps where appropriate. The java.sql.Timestamp class
 * has a handy toString method to make this easy.
 * <P>
 * Design note: If an error is detected in this class a message is written to
 * System.err. The Trace.error method cannot be used because the
 * TraceDeviceFile class uses ArchiveLog, and could result in an infinite
 * recursion loop.
 **/
// ----------------------------------------------------------------------------
public class ArchiveLog {
    /** File name where log output gets written. The currentFileName consists
     * of dirName and baseName. */
    private String currentFileName;

    /** Set to false when outputString triggers an exception. This prevents
     * multiple error messages from being generated. */
    private boolean firstTimeOutputError = true;

    /** Base part of currentFileName with no directory component. */
    private String baseName;

    /** Directory part of currentFileName with no trailing slash. */
    private String dirName;

    /** Holds other ArchiveLog instances that are or were in use. This prevents
     * more than one instance being created for the same log file. */
    private static Hashtable<String,ArchiveLog> archiveLogs = new Hashtable<String,ArchiveLog>();

    /** Current year, all digits. */
    private int year;

    /** Current month. January is numbered as 0. */
    private int month;

    /** Current day of month. */
    private int dayOfMonth;


    // ---------------------------------------------------------------------
    /**
     * Protected constructor.  Call the getInstance method to get an
     * instance.
     *
     * @param  dirName  directory name (no trailing directory delimiter) for
     * where the log file is to be placed.
     * 
     * @param  baseName  base name (no timestamp) of the log file name.
     */
    // ---------------------------------------------------------------------
    protected ArchiveLog(String dirName, String baseName) {
        init(dirName, baseName);
    }


    // ---------------------------------------------------------------------
    /**
     * This method is used to perform common initialization for the
     * constructors.
     *
     * @param  dirName  directory name (no trailing directory delimiter) for
     * where the log file is to be placed.
     * 
     * @param  baseName  base name (no timestamp) of the log file name.
     */
    // ---------------------------------------------------------------------
    protected void init(@SuppressWarnings("hiding") String dirName, @SuppressWarnings("hiding") String baseName) {
        this.dirName  = dirName;
        this.baseName = baseName;

        setTimestamp();         // Note when "today" is.

        currentFileName    = System.getProperty(Config.APP_HOME, "") + File.separator + this.dirName + File.separator + this.baseName;
        File dirNameFile = new File(System.getProperty(Config.APP_HOME, "") + File.separator + this.dirName);
        File currentFile = new File(currentFileName);
        if (currentFile.exists()) {
            // The file already exists. If the date on this file is the same
            // as today's date then we just use it. If the date on this file
            // is before today's date then we rename it with that date's
            // timestamp. This can happen if the server were shut down, and
            // then restarted at a later date, or if no log messages have
            // been written for a while.
            String existingTimestamp = 
                        (new SimpleDateFormat("yyyy-MM-dd")).format(
                                    new Date(currentFile.lastModified()));
            if (!existingTimestamp.equals(buildTimestamp())) {
                String timestampFileName =
                            System.getProperty(Config.APP_HOME, "") + File.separator +
                            (dirName.length() > 0 ? dirName + File.separator : "") +
                            existingTimestamp + "-" + baseName;
                if (!((new File(currentFileName)).renameTo(new File(timestampFileName)))) {
                    System.err.println("Unable to rename archive log file from " +
                            currentFileName + " to " + timestampFileName);
                }
            }
        }
        
        // Make sure the directories exist.
        if (!dirNameFile.isDirectory()) {
            // The archive log directory doesn't exist. Create it.
            dirNameFile.mkdirs();
        }

        archiveLogs.put(dirName + File.separator + baseName, this);
    }


    // ---------------------------------------------------------------------
    /**
     * Get an instance of an ArchiveLog object for the specified directory
     * and base filenames.
     *
     * @param  dirName  directory name (no trailing directory delimiter) for
     * where the log file is to be placed.
     * 
     * @param  baseName  base name (no timestamp) of the log file name.
     **/
    // ---------------------------------------------------------------------
    public synchronized static ArchiveLog getInstance(String dirName, String baseName) {
        ArchiveLog archiveLog = null;

        archiveLog = (ArchiveLog)archiveLogs.get(dirName + File.separator + baseName);
        if (archiveLog == null) {
            archiveLog = new ArchiveLog(dirName, baseName);
        }
        return archiveLog;
    }


    // ---------------------------------------------------------------------
    /**
     * Write a message to the archive log file. Nothing is added to the
     * string except a newline, so be sure and include timestamps where
     * appropriate.
     *
     * @param  message  the log message to be written.
     **/
    // ---------------------------------------------------------------------
    public synchronized void outputString(String message) {
        try {
            updateTimestamp();
            BufferedWriter bufferedWriter = new BufferedWriter(
                        new FileWriter(currentFileName, true)); // append
            bufferedWriter.write(message + "\n");
            bufferedWriter.close();
            bufferedWriter = null;
        }
        catch (Exception e) {
            System.err.println(message);
            if (firstTimeOutputError) {
                System.err.println("ArchiveLog.outputString: detected an " +
                            "Exception for archive log file " +
                            currentFileName);
                e.printStackTrace();
                firstTimeOutputError = false;
            }
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Generate a pretty print string for an ArchiveLog object.
     *
     * @return
     *   String - pretty print string.
     */
    //-------------------------------------------------------------------------
    public String toString() {
        return "ArchiveLog[" +
                    "currentFileName=" + currentFileName +
                    ", firstTimeOutputError=" + firstTimeOutputError +
                    ", baseName=" + baseName +
                    ", dirName=" + dirName +
                    ", year=" + year +
                    ", month=" + month +
                    ", dayOfMonth=" + dayOfMonth +
                    "]";
    }


    // ---------------------------------------------------------------------
    /**
     * Update the timestamp if needed. Handle the transition to a new day.
     **/
    // ---------------------------------------------------------------------
    private void updateTimestamp() {
        Calendar now = Calendar.getInstance();
        int aYear       = now.get(Calendar.YEAR);
        int aMonth      = now.get(Calendar.MONTH);
        int aDayOfMonth = now.get(Calendar.DAY_OF_MONTH);

        if (
                    (aDayOfMonth != this.dayOfMonth) ||
                    (aMonth      != this.month)      ||
                    (aYear       != this.year)) {
            renameCurrentToTimestamp();
            setTimestamp(year, month, dayOfMonth);
        }
    }


    // ---------------------------------------------------------------------
    /**
     * Change the timestamp to today's value.
     **/
    // ---------------------------------------------------------------------
    private void setTimestamp() {
        Calendar now = Calendar.getInstance();

        setTimestamp(
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));
    }


    // ---------------------------------------------------------------------
    /**
     * Change the timestamp to its new value.
     *
     * @param  year  the current year.
     * @param  month  the current month. 0 = January.
     * @param  dayOfMonth  the current day of the month.
     **/
    // ---------------------------------------------------------------------
    private void setTimestamp(int year, int month, int dayOfMonth) {
        this.year       = year;
        this.month      = month;
        this.dayOfMonth = dayOfMonth;
    }


    // ---------------------------------------------------------------------
    /**
     * Build the timestamp in the form YYYY-MM-DD.
     *
     * @return
     *   String - the timestamp.
     **/
    // ---------------------------------------------------------------------
    private String buildTimestamp() {
        String YYYY;
        String MM;
        String DD;

        YYYY = "" + this.year;
        MM   = "" + (this.month + 1); // Months are 0 based.
        DD   = "" + this.dayOfMonth;
        if (MM.length() == 1)  MM = "0" + MM;
        if (DD.length() == 1)  DD = "0" + DD;
        return YYYY + "-" + MM + "-" + DD;
    }


    // ---------------------------------------------------------------------
    /**
     * Rename the current log file to a file with the same name and a
     * timestamp.
     **/
    // ---------------------------------------------------------------------
    private void renameCurrentToTimestamp() {
        String timestampFileName =
                    System.getProperty(Config.APP_HOME, "") + File.separator +
                    (dirName.length() > 0 ? dirName + File.separator : "") +
                    buildTimestamp() + "-" + baseName;

        if (!((new File(currentFileName)).renameTo(new File(timestampFileName)))) {
            // If we had an error trying to rename the file, check to see if it
            // already exists. If it does, then we can wait another day and
            // rename it then.
            if (!((new File(timestampFileName)).exists())) {
                System.err.println("Unable to rename archive log file from " +
                            currentFileName + " to " + timestampFileName);
            }
        }
    }


    // ---------------------------------------------------------------------
    /**
     * Basis for test cases. Use this method to try the various test cases.
     * <P>
     * The author couldn't figure out how to change the time programically,
     * so this approach will have to suffice.
     * <P>
     * There are 4 test cases to verify the proper operation of this class.
     * <OL>
     *   <LI>
     *     Initial condition: no c:/tmp/*testlog.txt files<BR>
     *     Test steps:<BR>
     *     <OL>
     *       <LI> Let both messages be written.<BR>
     *     </OL>
     *     Final condition: c:/tmp/*testlog.txt only matches for
     *                      c:/tmp/testlog.txt. Both messages are written to
     *                      that file.<BR>
     *   <LI>
     *     Initial condition: no c:/tmp/*testlog.txt files<BR>
     *     Test steps:<BR>
     *     <OL>
     *       <LI> Let message 1 be written.<BR>
     *       <LI> Before 30 seconds pass, advance the date to tomorrow.<BR>
     *       <LI> Let message 2 be written.<BR>
     *       <LI> Set the date back to today's date.<BR>
     *     </OL>
     *     Final condition: c:/tmp/*testlog.txt matches for
     *                      c:/tmp/testlog.txt and
     *                      c:/tmp/YYYY-MM-DD-testlog.txt
     *                      where YYYY-MM-DD is today's date. Message 1
     *                      should be in c:/tmp/YYYY-MM-DD-testlog.txt and
     *                      message 2 should be in c:/tmp/testlog.txt.<BR>
     *   <LI> 
     *     Initial condition: test case 1 has been executed.<BR>
     *     Test steps:<BR>
     *     <OL>
     *       <LI> Let both messages be written.<BR>
     *     </OL>
     *     Final condition: c:/tmp/*testlog.txt only matches for
     *                      c:/tmp/testlog.txt. Messages 1 and 2 from test
     *                      case 1, and messages 1 and 2 from this test case
     *                      should in that file.<BR> 
     *   <LI> 
     *     Initial condition: test case 3 has been executed.<BR>
     *     Test steps:<BR>
     *     <OL>
     *       <LI> Advance the date to tomorrow.<BR>
     *       <LI> Let both messages be written.<BR>
     *       <LI> Set the date back to today's date.<BR>
     *     </OL>
     *     Final condition: c:/tmp/*testlog.txt matches for
     *                      c:/tmp/testlog.txt and 
     *                      c:/tmp/YYYY-MM-DD-testlog.txt
     *                      where YYYY-MM-DD is today's date.
     *                      c:/tmp/YYYY-MM-DD-testlog.txt should be contain
     *                      Messages 1 and 2 from test case 1, and messages
     *                      1 and 2 from test case 3. c:/tmp/testlog.txt
     *                      should contain messages 1 and 2 from this test
     *                      case.<BR>
     * </OL>
     **/
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        // To allow the tester enough time to change the date, we need to
        // create an anonymous thread class so we can call Thread.sleep.
        Thread thread = new Thread() {
                public void run() {
                    ArchiveLog archiveLog = getInstance("c:/tmp", "testlog.txt");
                    String message;
                    message = " message 1";
                    archiveLog.outputString((new Timestamp(new Date().getTime())) + message);
                    try {
                        Thread.sleep(30 * 1000); // 30 seconds.
                    }
                    catch (InterruptedException e) {}
                    message = " message 2";
                    archiveLog.outputString((new Timestamp(new Date().getTime())) + message);
                }
            };
        thread.start();
    }
}
