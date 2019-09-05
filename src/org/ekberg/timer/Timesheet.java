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


import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.*;
import java.util.*;
import javax.swing.*;
import org.ekberg.timer.database.TaskTimeElement;
import org.ekberg.timer.database.TaskTimeElementDB;
import org.ekberg.timer.utility.Config;
import org.ekberg.timer.utility.Trace;


/**
 * Main method. Creates the main timesheet window. The time starts either when
 * one clicks the "Start" button, or selects one of the timer buttons.
 *
 * Note that if one wants to add more timer buttons, edit the
 * timesheet.property file. This is done so infrequently that there is no GUI
 * for this.
 */
public class Timesheet {
    /** Number of milliseconds in one second. */
    public final static long ONE_SECOND = 1000L;

    protected final static String CONFIG_PREFIX = "timesheet.button.";

    /** Date formatter for the generated timesheet file. */
    protected final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    protected final static String HELP_MESSAGE = "Timesheet Program\n" +
                "\n" +
                "This program can be used to record the amount of time used at various tasks. A\n" +
                "list of time categories is listed on the left, with the amount of time spent\n" +
                "performing that task to the right. When a timer is started, its time is incremented\n" +
                "every second. The action buttons at the bottom and do the following:\n" +
                "\n" +
                "    Start - start updating the time for the currently selected timer.\n" +
                "\n" +
                "    Pause - stop updating the time for the currently selected timer.\n" +
                "\n" +
                "    Help - display this message.\n" +
                "\n" +
                "    Exit - save the times in the timesheets directory and stop this program.\n" +
                "\n" +
                "    Add Timer - asks for the name of a new timer and adds it.\n" +
                "\n" +
                "    Delete Timer - deletes the currently selected timer.\n" +
                "\n" +
                "When the day changes, the old data is automatically saved in the timesheets directory. If one\n" +
                "presses the Exit button, or kills the window, data is also saved automatically.\n" +
                "\n" +
                "When this program is started, it looks for timer data for today and loads this data into the\n" +
                "timers. In this way, one can exit this program and start it up again, without losing any timer\n" +
                "values.\n" +
                "\n" +
                "Also note that when a timer is selected, it is automatically started.  This means that the Start\n" +
                "button is not that useful. It is only needed when the currently selected timer is stopped, as when\n" +
                "this program is started, or a timer has been paused.\n" +
                "\n" +
                "The timesheet data is stored in a timesheet directory. This is specified in the timesheet.properties\n" +
                "file as timesheet.outputDirectory. Set this to a directory on your workstation, currently\n" +
                "    \"" + Config.getString("timesheet.outputDirectory") + "\n" +
                "Note that if the directory does not exist, it is created.  The timesheet data is stored in files of\n" +
                "the form timesheet-YYYY-MM-DD.csv. The data consists of the timers and their times, separated by\n" +
                "commas, with each timer being on its own line.  The time value is the number of hours, expressed as\n" +
                "a decimal number.  This allows for easy post-processing. Data that is that not a known timer in this\n" +
                "file is retained. There are many digits to the right of the decimal point so that the seconds\n" +
                "displayed in the timer are accurate.\n";

    /** Instance of a timesheet. Only one is needed. */
    protected static Timesheet _instance = null;

    /** Top level JFrame object. Needed to show alerts. */
    protected static JFrame topFrame;

    /** The names of the timer buttons. */
    protected static List<String> buttonNames;

    /** Collection of Button objects. Each button can show an hours value. */
    protected ArrayList<Button> buttons;

    /** This is the timer task that runs every second. */
    protected Updater updater;

    /** Instance to use to access the database. */
    protected static TaskTimeElementDB database = null;

    /** The name of the user retrieved via getenv. */
    protected static String userName;


    /** Internal class used to hold data needed for each timer button. */
    public class Button implements Comparable<Button>{
        /** The radio button that activates a timer. */
        public JRadioButton button;

        /** This shows the number of hours for this timer. */
        public JLabel time;

        /** Needed by outputTimes when rewriting times to the file. When this
         * is true, it means that this timer wasn't in the file and still needs
         * to be written. When this is false, it was found in the file, and
         * doesn't need to be written again. */
        boolean needsToBeWritten;


        //---------------------------------------------------------------------
        /**
         * Constructor for Button. Initializes everything.
         *
         * @param  name  The name for this timer. For example "Lunch".
         */
        //---------------------------------------------------------------------
        public Button(String name) {
            button = new JRadioButton(name);
            time = new JLabel("0");
            needsToBeWritten = true;
        }


        //---------------------------------------------------------------------
        /**
         * This method defines the natural ordering of Button objects.
         *
         * @param  button  the button to compare with this button.
         *
         * @return
         *   int - -1 if this is less than button, 0 if equal and 1 if this is
         *  greater than button.
         */
        //---------------------------------------------------------------------
        public int compareTo(Button b) {
            return (button.getText()).compareTo(b.button.getText());
        }


        //---------------------------------------------------------------------
        /**
         * Pretty printer for Button objects. Mostly used for debugging.
         *
         * @return
         *   String - nicely formatted Button object.
         */
        //---------------------------------------------------------------------
        public String toString() {
            return "Button[name=" + button.getText() +
                        ", time=" + time.getText() +
                        ", needsToBeWritten=" + needsToBeWritten +
                        "]";
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Timesheet constructor. Creates the GUI and loads prexisting data if it
     * is present.
     *
     * @param  container  content pane of the top level frame.
     */
    //-------------------------------------------------------------------------
    public Timesheet(Container container) {
        _instance = this;

        GridBagLayout gbl = new GridBagLayout();
        Button b;

        // Show buttons on the left column, and their times in the right column.
        container.setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        buttons = new ArrayList<Button>(); // Button objects.
        ButtonGroup buttonGroup = new ButtonGroup();
        dumpButtonNames(buttonNames);

        for(String buttonName: buttonNames) {
            b = new Button(buttonName);
            buttons.add(b);
            c.gridx = 0;
            c.anchor = GridBagConstraints.WEST;
            gbl.setConstraints(b.button, c);
            container.add(b.button);
            buttonGroup.add(b.button);
            b.button.setSelected(c.gridy == 0);
            // Start a timer when its button is clicked.
            b.button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        updater.setTimerGUI(findButton().time);
                    }
                });

            c.gridx = 1;
            c.weightx = 1.0;    // Give the time any extra space.
            gbl.setConstraints(b.time, c);
            container.add(b.time);
            c.weightx = 0.0;
            c.gridy++;
        }

        JPanel p = new JPanel();
        c.gridx = 0;
        c.gridwidth = 2;
        gbl.setConstraints(p, c);
        container.add(p);

        p.setLayout(new GridLayout(1, 4, 2, 2));
        // Put the action buttons at the bottom.
        JButton start = new JButton("Start");
        p.add(start);

        JButton pause = new JButton("Pause");
        p.add(pause);

        JButton help = new JButton("Help");
        p.add(help);

        JButton exit = new JButton("Exit");
        p.add(exit);

        // Now the timer buttons
        p = new JPanel();
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        gbl.setConstraints(p, c);
        container.add(p);

        p.setLayout(new GridLayout(1, 3, 2, 2));
        // Put the action buttons at the bottom.
        JButton newTimer = new JButton("Add Timer");
        p.add(newTimer);

        JButton deleteTimer = new JButton("Delete Timer");
        p.add(deleteTimer);


        updater = new Updater();
        java.util.Timer t = new java.util.Timer();
        t.scheduleAtFixedRate(updater, 0L, ONE_SECOND);

        updater.addNewDayListener(new NewDayListener() {
                public void action(Date previousDay) {
                    outputTimes(previousDay);
                    // Starting a new day. Clear out all of the buttons.
                    resetButtons();
                }
            });

        updater.addMinutesChangedListener(new MinutesChangedListener() {
                public void action(Date now) {
                    outputTimes(now);
                }
            });

        start.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    updater.setTimerGUI(findButton().time);
                }
            });

        pause.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    outputTimes(new Date());
                    updater.setTimerGUI(null);
                }
            });

        help.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    alert(HELP_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
                }
            });

        exit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    allDone();
                }
            });

        newTimer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    String newTimer = JOptionPane.showInputDialog(topFrame, "Enter the name of a new Timer:");
                    if (newTimer != null) {
                        boolean found = false;
                        for(String buttonName: buttonNames) {
                            if (newTimer.equals(buttonName)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            alert("That timer is already being used.", JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            outputTimes(new Date());
                            buttonNames.add(newTimer);
                            Timesheet.dumpButtonNames(buttonNames);
                            topFrame.getContentPane().removeAll();
                            new Timesheet(topFrame.getContentPane());
                            topFrame.pack();
                        }
                    }
                }
            });

        deleteTimer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    Button foundButton = null;
                    for(Button b: buttons) {
                        if (b.button.isSelected()) {
                            foundButton = b;
                            break;
                        }
                    }
                    if (foundButton != null) {
                        buttonNames.remove(foundButton.button.getText());
                        Timesheet.dumpButtonNames(buttonNames);
                        topFrame.getContentPane().removeAll();
                        new Timesheet(topFrame.getContentPane());
                        topFrame.pack();
                    }
                }
            });

        loadData();
    }


    //-------------------------------------------------------------------------
    /**
     * Dump the timer button names to a file. The user has changed the names
     * and they need to be recorded.
     *
     * @param  buttonNames  the new button names.
     */
    //-------------------------------------------------------------------------
    public static void dumpButtonNames(List<String> buttonNames) {
        String today = null;
        List<TaskTimeElement> beans = null;
        Calendar cal = Calendar.getInstance();
        if (database.findAll().size() > 0) {
            // Loop backward through days until we find something.
            do {
                today = sdf.format(cal.getTime());
                beans = database.findAllByTaskDate(today);
                Trace.info("TWE dumpButtonNames: today=" + today + ", buttonNames=" + buttonNames);
                Trace.info("TWE dumpButtonNames: beans=" + beans);
                cal.add(Calendar.DAY_OF_WEEK, -1);
            } while (beans.size() == 0);
        } else {
            beans = new ArrayList<TaskTimeElement>();
        }
        int loc;
        for(TaskTimeElement bean: beans) {
            if ((loc = buttonNames.indexOf(bean.getTaskName())) < 0) {
                // The database says yes, but buttonNames says no.
                // A buttonName was deleted.
                bean.setEnabled(false); // Preserve duration value.
                database.update(bean);
                Trace.info("TWE dumpButtonNames: disabled " + bean);
                break;
            }
        }
        for(String buttonName: buttonNames) {
            boolean found = false;
            for(TaskTimeElement bean: beans) {
                if (buttonName.equals(bean.getTaskName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                // The buttonName says yes, but the database says no.
                // A new buttonName was added.
                TaskTimeElement oldBean = database.findByTaskNameAndTaskDate(buttonName, today);
                if (oldBean != null) {
                    // Found an existing (diabled) bean. Reenable it.
                    Trace.info("TWE dumpButtonNames: oldBean " + oldBean);
                    oldBean.setEnabled(true);
                    database.update(oldBean);
                } else {
                    TaskTimeElement newBean = new TaskTimeElement();
                    newBean.setDuration(0);
                    newBean.setTaskDate(today);
                    newBean.setTaskName(buttonName);
                    newBean.setUserName(userName);
                    newBean.setEnabled(true);
                    if (database.create(newBean)) {
                        Trace.info("TWE dumpButtonNames: created " + newBean);
                    } else {
                        Trace.error("Unable to create bean " + newBean);
                    }
                }
                break;
            }
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Load the button names from their persistent store.
     *
     * @return
     *   List<String> - the button names. If an error was detected then null
     * will be returned.
     */
    //-------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public static List<String> loadButtonNames() {
        List<String> names = null;
        List<TaskTimeElement> beans = database.findAll();
        // Use the date of the last one to retrieve all for this date.
        if (beans.size() == 0) {
            // No names, and no error - return empty list.
            names = new ArrayList<String>();
        } else {
            String lastDate = beans.get(beans.size() - 1).getTaskDate();
            beans = database.findAllByTaskDate(lastDate);
            names = new ArrayList<String>(beans.size());
            for(TaskTimeElement bean: beans) {
                names.add(bean.getTaskName());
            }
        }
        return names;
    }


    //-------------------------------------------------------------------------
    /**
     * Reset all of the buttons back to zero.
     */
    //-------------------------------------------------------------------------
    protected void resetButtons() {
        for(Button b: buttons) {
            b.time.setText("0");
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Load old data into the Buttons.
     */
    //-------------------------------------------------------------------------
    protected void loadData() {
        List<TaskTimeElement> beans = database.findAllByTaskDate(sdf.format(new Date()));
        for(TaskTimeElement bean: beans) {
            Button b = findButton(bean.getTaskName());
            try {
                b.time.setText(updater.formatTime(bean.getDuration()));
            } catch (NumberFormatException e) {
            }
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Form a timesheet file name from a directory and a date. Note that the
     * actual file may or may not exist.
     *
     * @param  directory  the timesheet directory.
     *
     * @param  date  date this timesheet data is for.
     *
     * @return
     *   File - a File object.
     */
    //-------------------------------------------------------------------------
    protected File getTimesheetFile(File directory, Date date) {
        return new File(directory, "timesheet-" + sdf.format(date) + ".csv");
    }


    //-------------------------------------------------------------------------
    /**
     * Locate a Button object associated with the currently selected
     * JRadioButton object.
     *
     * @return
     *   Button - if found, the selected Button object, otherwise null.
     */
    //-------------------------------------------------------------------------
    protected Button findButton() {
        Button ret = null;
        for(Button b: buttons) {
            if (b.button.isSelected()) {
                ret = b;
                break;
            }
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Find a button using its name.
     *
     * @param  name  the named button to look for.
     *
     * @return
     *   Button - if found, return the Button object, otherwise null.
     */
    //-------------------------------------------------------------------------
    protected Button findButton(String name) {
        Button ret = null;
        for(Button b: buttons) {
            if (b.button.getText().equals(name)) {
                ret = b;
                break;
            }
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Output the current times for this day.
     *
     * @param  date  used to construct the timesheet file name.
     */
    //-------------------------------------------------------------------------
    public void outputTimes(Date date) {
        boolean haveData = false;

        for(Button b: buttons) {
            if (!b.time.getText().equals("0")) {
                haveData = true;
                break;
            }
        }
        Trace.info("TWE haveData=" + haveData);

        if (haveData) {
            String today = sdf.format(date);
            List<TaskTimeElement> beans = database.findAllByTaskDate(today);
            Trace.info("TWE buttons=" + buttons);
            Trace.info("TWE beans=" + beans);
            for(Button b: buttons) {
                boolean foundBean = false;
                double hours = updater.parseTime(b.time.getText());
                for(TaskTimeElement bean: beans) {
                    if (b.button.getText().equals(bean.getTaskName())) {
                        Trace.info("TWE outputTimes: saved time=" + (updater.parseTime(b.time.getText())));
                        bean.setDuration(hours);
                        if (!database.update(bean)) {
                            Trace.error("Got an error updating bean: " + bean);
                        }
                        foundBean = true;
                        break;
                    }
                }
                if (!foundBean) {
                    // Didn't find an existing bean. Create one.
                    TaskTimeElement newBean = new TaskTimeElement();
                    newBean.setDuration(hours);
                    newBean.setTaskDate(today);
                    newBean.setTaskName(b.button.getText());
                    newBean.setUserName(userName);
                    newBean.setEnabled(true);
                    if (database.create(newBean)) {
                        Trace.info("TWE dumpButtonNames: created " + newBean);
                    } else {
                        Trace.error("Unable to create bean " + newBean);
                    }
                    
                }
            }
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Display a fatal error message, then exit.
     *
     * @param  message  the message to display.
     */
    //-------------------------------------------------------------------------
    public static void fatal(String message) {
        alert(message, JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }


    //-------------------------------------------------------------------------
    /**
     * Display an alert message.
     * Note: this method is synchronized so only one will be active at a
     * time. With Java, another thread could try and invoke this method.
     *
     * @param  message  the message to display.
     */
    //-------------------------------------------------------------------------
    public synchronized static void alert(String message, int category) {
        String[] lines = message.split("\\n");
        if (lines.length > 10) {
            // This may be too long to display all at once. Put the message in
            // a scrollable JTextArea.
            JTextArea ta = new JTextArea(message, 20, 50);
            ta.setEditable(false);
            JScrollPane p = new JScrollPane(ta);
            JOptionPane.showMessageDialog(topFrame, p, "Alert",
                        category);
        }
        else {
            JOptionPane.showMessageDialog(topFrame, message, "Alert",
                        category);
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Exiting. Save the data we have so far.
     */
    //-------------------------------------------------------------------------
    public static void allDone() {
        _instance.outputTimes(new Date());
        System.exit(0);
    }


    //-------------------------------------------------------------------------
    /**
     * Main program. Run this to show the timesheet window.
     *
     * @param  args  unused.
     */
    //-------------------------------------------------------------------------
    public static void main(String[] args) {
        userName = System.getenv("USERNAME"); // Windows
        if (userName == null) {
            userName = System.getenv("USER");        // Linux
        }
        if (userName == null) {
            System.out.println("Unable to determine user login name.");
            database = new TaskTimeElementDB();
        } else {
            database = new TaskTimeElementDB(userName);
        }

        topFrame = new JFrame("Timesheet");
        buttonNames = Timesheet.loadButtonNames();

        new Timesheet(topFrame.getContentPane());
        topFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    allDone();
                }
            });
        topFrame.pack();
        Dimension frameSize = topFrame.getSize();
        // Bump the height up to show a reasonable number of times.
        frameSize.width = 300;
        topFrame.setSize(frameSize);
        topFrame.setVisible(true);
    }
}
