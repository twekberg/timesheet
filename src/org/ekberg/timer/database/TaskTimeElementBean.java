//  @(#) $Id:  $


//-----------------------------------------------------------------------------
/**
 * WARNING: DO NOT EDIT THIS FILE.
 *
 * This file is automatically generated by the GenBean program and should not be
 * modified directly. Only the .properties file should be changed.
 *
 */
//-----------------------------------------------------------------------------


package org.ekberg.timer.database;


import java.io.Serializable;


/**
 * This class maps to the TaskTimeElementBeans table in the database. It is used to allow
 * one to manage TaskTimeElementBeans interactively, rather than doing so by hand
 * coding.
 *<P>
 * The operations that are public are:
 * <UL>
 *   <LI> Simple constructor.
 *   <LI> get/set methods on all data items.
 *   <LI> toString.
 * </UL>
 */
public class TaskTimeElementBean extends BasicBean implements Serializable,Cloneable {
    /** Define a version for Serializable. */
    private static final long serialVersionUID = 1L;

    /** This is for the duration column. The count for this task, expressed in hours. */
    double duration;

    /** This is for the taskDate column. The date that this task element is for. */
    String taskDate;

    /** This is for the taskName column. The user-specified task name whose time is to be counted. */
    String taskName;

    /** This is for the userName column. The user login name. */
    String userName;

    /** Special flag used to 'delete' task name without removing the timing
     * data. The findAll methods will only return those beans that are
     * enabled. */
    boolean enabled;


    // ------------------------------------------------------------------------
    /**
     * Simple constructor.
     */
    // ------------------------------------------------------------------------
    public TaskTimeElementBean() {
        duration = 0;
    }


    // ------------------------------------------------------------------------
    /**
     * Get accessor for the duration attribute.
     *
     * @return
     *   double - the duration attribute.
     */
    // ------------------------------------------------------------------------
    public double getDuration() {
        return duration;
    }


    // ------------------------------------------------------------------------
    /**
     * Set accessor for the duration attribute.
     *
     * @param  duration  The new value for the duration attribute.
     */
    // ------------------------------------------------------------------------
    public void setDuration(double duration) {
        this.duration = duration;
        setDirty();
    }


    // ------------------------------------------------------------------------
    /**
     * Get accessor for the taskDate attribute.
     *
     * @return
     *   String - the taskDate attribute.
     */
    // ------------------------------------------------------------------------
    public String getTaskDate() {
        return taskDate;
    }


    // ------------------------------------------------------------------------
    /**
     * Set accessor for the taskDate attribute.
     *
     * @param  taskDate  The new value for the taskDate attribute.
     */
    // ------------------------------------------------------------------------
    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
        setDirty();
    }


    // ------------------------------------------------------------------------
    /**
     * Get accessor for the taskName attribute.
     *
     * @return
     *   String - the taskName attribute.
     */
    // ------------------------------------------------------------------------
    public String getTaskName() {
        return taskName;
    }


    // ------------------------------------------------------------------------
    /**
     * Set accessor for the taskName attribute.
     *
     * @param  taskName  The new value for the taskName attribute.
     */
    // ------------------------------------------------------------------------
    public void setTaskName(String taskName) {
        this.taskName = taskName;
        setDirty();
    }


    // ------------------------------------------------------------------------
    /**
     * Get accessor for the userName attribute.
     *
     * @return
     *   String - the userName attribute.
     */
    // ------------------------------------------------------------------------
    public String getUserName() {
        return userName;
    }


    // ------------------------------------------------------------------------
    /**
     * Set accessor for the userName attribute.
     *
     * @param  userName  The new value for the userName attribute.
     */
    // ------------------------------------------------------------------------
    public void setUserName(String userName) {
        this.userName = userName;
        setDirty();
    }


    //-------------------------------------------------------------------------
    /**
     * Get accessor for the enabled attribute.
     *
     * @return
     *   boolean - the enabled attribute.
     */
    //-------------------------------------------------------------------------
    public boolean isEnabled() {
        return enabled;
    }


    //-------------------------------------------------------------------------
    /**
     * Set accessor for the enabled attribute.
     *
     * @param  enabled  the new value for the enabled attribute.
     */
    //-------------------------------------------------------------------------
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    // ------------------------------------------------------------------------
    /**
     * Returns a String representation of a TaskTimeElementBean object. This is mostly only
     * useful during debugging.
     *
     * @return
     *   String - the "pretty-printed" TaskTimeElementBean object.
     */
    // ------------------------------------------------------------------------
    public String toString() {
        return "TaskTimeElementBean" +
                    '@' + Integer.toHexString(hashCode()) +
                    "[" +
                    "id=" + getId() +
                    ", duration=" + getDuration() +
                    ", taskDate=" + (getTaskDate() == null ? null : "\"" + getTaskDate() + "\"") +
                    ", taskName=" + (getTaskName() == null ? null : "\"" + getTaskName() + "\"") +
                    ", userName=" + (getUserName() == null ? null : "\"" + getUserName() + "\"") +
                    ", enabled=" + isEnabled() +
                    "]";
    }


    // ------------------------------------------------------------------------
    /**
     * Create a clone of this object.
     *
     * @return
     *   Object - the clone.
     */
    //-------------------------------------------------------------------------
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    // ------------------------------------------------------------------------
    /**
     * Create a copy of this object.
     *
     * @return
     *   TaskTimeElementBean - the copy. If an exception is raised then null is returned.
     */
    //-------------------------------------------------------------------------
    public TaskTimeElementBean copy() {
        TaskTimeElementBean ret = null;
        try {
            ret = (TaskTimeElementBean)clone();
        }
        catch (CloneNotSupportedException cns) {}
        return ret;
    }
}