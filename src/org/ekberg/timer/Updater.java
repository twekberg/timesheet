//  @(#) $Id:  $  

//  *********************************************************************
// 
//    Copyright (c) 2004 Tom Ekberg.
//    All Rights Reserved
// 
//    The information contained herein is confidential to and the
//    property of Tom Ekberg. and is not to be disclosed
//    to any third party without prior express written permission
//    of Tom Ekberg.  Tom Ekberg., as the
//    author and owner under 17 U.S.C. Sec. 201(b) of this work made
//    for hire, claims copyright in this material as an unpublished 
//    work under 17 U.S.C. Sec.s 102 and 104(a)   
// 
//  ******************************************************************* 


package org.ekberg.timer;


import java.text.*;
import java.util.*;
import javax.swing.*;


/**
 * This class updates a specific timer, once every second.
 * New day listeners can express interest in a new day by calling
 * addNewDayListener() with a NewDayListener object.
 */
public class Updater extends TimerTask {
    /** The time formatted to show hours. */
    protected static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("H:mm:ss");

    /** The time formatted when there are no hours, but there are minutes. */
    protected static final SimpleDateFormat MINUTE_FORMAT = new SimpleDateFormat("m:ss");

    /** The time formatted when there are no hours or minutes. */
    protected static final SimpleDateFormat SECOND_FORMAT = new SimpleDateFormat("ss");

    /** The time can be formatted in one of the following ways. */
    protected static final SimpleDateFormat[] sdfs = {
        HOUR_FORMAT, MINUTE_FORMAT, SECOND_FORMAT};

    /** The GUI to update with the new time. This is the time to be updated. */
    JLabel timerGUI;

    /** Time at which this task last ran. */
    long scheduledTime = 0L;

    /** NewDayListener objects. */
    List<NewDayListener> dayListeners = new ArrayList<NewDayListener>();

    /** MinutesChangedListener objects. */
    List<MinutesChangedListener> minuteListeners = new ArrayList<MinutesChangedListener>();

    /** The day of year the previous time run() was invoked. */
    int previousDay;

    /** The year the previous time time run() was invoked. */
    int previousYear;


    //-------------------------------------------------------------------------
    /**
     * Specify the JLLabel to update.
     *
     * @param  timerGUI  the new timer to update.
     */
    //-------------------------------------------------------------------------
    public void setTimerGUI(JLabel timerGUI) {
        this.timerGUI = timerGUI;
    }


    //-------------------------------------------------------------------------
    /**
     * Simple constructor. Initializes all values.
     */
    //-------------------------------------------------------------------------
    protected Updater() {
        super();
        timerGUI = null;
        previousDay = -1;
        previousYear = -1;
    }


    //-------------------------------------------------------------------------
    /**
     * The main loop is here. This is invoked every second.
     */
    //-------------------------------------------------------------------------
    public void run() {
        scheduledTime = System.currentTimeMillis();

        Calendar today = new GregorianCalendar();
        int currentDay = today.get(Calendar.DAY_OF_YEAR);
        if ((previousDay != currentDay) && (timerGUI == null)) {
            if (previousDay != -1) {
                // Have a new day and there it no active GUI.  Inform those who
                // wanted to be notified.
                GregorianCalendar yesterday = new GregorianCalendar();
                yesterday.set(Calendar.DAY_OF_YEAR, previousDay);
                yesterday.set(Calendar.YEAR, previousYear);
                invokeDayListeners(yesterday.getTime());
            }
            previousDay = currentDay;
            previousYear = today.get(Calendar.YEAR);
        }
        if (timerGUI != null) {
            Calendar oldTime = parseCalendarTime(timerGUI.getText());
            int minutes;
            if (oldTime != null) {
                Calendar time = (Calendar)oldTime.clone();
                time.add(Calendar.SECOND, 1);
                if (time.get(Calendar.DAY_OF_YEAR) != oldTime.get(Calendar.DAY_OF_YEAR)) {
                    invokeDayListeners(oldTime.getTime());
                }
                // Pick the right output formatter.
                SimpleDateFormat sdf = SECOND_FORMAT;
                if ((minutes = time.get(Calendar.MINUTE)) > 0) {
                    sdf = MINUTE_FORMAT;
                    if ((minutes / 3 * 3 == minutes) &&
                                (time.get(Calendar.SECOND) == 0)) {
                        invokeMinuteListeners(new Date());
                    }
                }
                if (time.get(Calendar.HOUR_OF_DAY) > 0)
                    sdf = HOUR_FORMAT;
                timerGUI.setText(sdf.format(time.getTime()));
            }
            else {

                System.err.println("Unable to parse " + timerGUI.getText());
            }
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Parse a time value from the GUI. This can in one of the following
     * formats: SS, MM:SS or HH:MM:SS.
     *
     * @param  timeText  the time value.
     *
     * @return
     *   double - if the time could be parsed, the time in hours is returned,
     * otherwise -1.
     */
    //-------------------------------------------------------------------------
    public double parseTime(String timeText) {
        double ret = -1;
        Calendar time = parseCalendarTime(timeText);

        if (time != null) {
            ret = (double)time.get(Calendar.HOUR_OF_DAY) +
                        time.get(Calendar.MINUTE) / 60.0D +
                        time.get(Calendar.SECOND) / 3600.0D +
                        time.get(Calendar.MILLISECOND ) / 3600000.0D;
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Parse a time value from the GUI.This can in one of the following
     * formats: SS, MM:SS or HH:MM:SS. This differs from parseTime only in the
     * return value.
     *
     * @param  timeText  the time value.
     *
     * @return
     *   Calendar - if the time could be parsed, the Calendar time is returned,
     * otherwise null.
     */
    //-------------------------------------------------------------------------
    public Calendar parseCalendarTime(String timeText) {
        Calendar ret = null;
        SimpleDateFormat sdf;
        Calendar time = new GregorianCalendar();

        for(int i=0; i<sdfs.length; i++) {
            sdf = sdfs[i];
            try {
                time.setTime(sdf.parse(timeText));
                ret = time;
                break;
            }
            catch (ParseException e) {
            }
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Format hours as a floating point number into a string.
     *
     * @param  hourTime  the hour time.
     *
     * @return
     *   String - the equivalent time, suitable for date parsing.
     */
    //-------------------------------------------------------------------------
    public String formatTime(double hourTime) {
        String ret = "";
        long secondTime = Math.round(hourTime * 3600.0D);

        if (secondTime >= 3600) {
            // Have hours. Format as H:MM:SS.
            ret = (secondTime / 3600) + ":";
            secondTime = secondTime - (secondTime / 3600) * 3600;
            ret = ret + pad2("" + (secondTime / 60)) + ":" +
                        pad2("" + (secondTime % 60));
        }
        else if (secondTime >= 60) {
            // Have minutes. Format as M:SS.
            ret = (secondTime / 60) + ":" + pad2("" + (secondTime % 60));
        }
        else {
            // Only have seconds. Format as S.
            ret = "" + (secondTime % 60);
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Pad a string with leading zeros until there are 2 digits.
     *
     * @param  s  the string.
     *
     * @return
     *   String - the string padded with leading zeros.
     */
    //-------------------------------------------------------------------------
    public String pad2(String s) {
        while (s.length() < 2) {
            s = "0" + s;
        }
        return s;
    }


    //-------------------------------------------------------------------------
    /**
     * Call those objects that are interested in knowing when the day changes.
     *
     * @param  date  the old date.
     */
    //-------------------------------------------------------------------------
    protected void invokeDayListeners(Date date) {
        for(NewDayListener ndl: dayListeners) {
            ndl.action(date);
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Call those objects that are interested in knowing when the minutes change.
     *
     * @param  date  now.
     */
    //-------------------------------------------------------------------------
    protected void invokeMinuteListeners(Date date) {
        for(MinutesChangedListener mcl: minuteListeners) {
            mcl.action(date);
        }
    }


    public void addMinutesChangedListener(MinutesChangedListener mcl) {
        minuteListeners.add(mcl);
    }


    //-------------------------------------------------------------------------
    /**
     * This object is indicating that it is interested in knowing when the day
     * changes.
     *
     * @param  ndl  a NewDayListener object.
     */
    //-------------------------------------------------------------------------
    public void addNewDayListener(NewDayListener ndl) {
        dayListeners.add(ndl);
    }


    //-------------------------------------------------------------------------
    /**
     * Required method for a TimerTask.
     *
     * @return
     *   boolean - see JavaDocs for TimerTask.
     */
    //-------------------------------------------------------------------------
    public boolean cancel() {
        return true;
    }


    //-------------------------------------------------------------------------
    /**
     * Required method for a TimerTask.
     *
     * @return
     *   long - see JavaDocs for TimerTask.
     */
    //-------------------------------------------------------------------------
    public long scheduledExecutionTime() {
        return scheduledTime;
    }
}
