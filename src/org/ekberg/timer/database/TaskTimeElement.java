//  @(#) $Id:  $

//  *********************************************************************
// 
//    Copyright (c) 2008 Tom Ekberg.
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


package org.ekberg.timer.database;


/**
 * This class extends the generated TaskTimeElementBean class by adding some
 * specialization. No code other than this should refer directly to the
 * TaskTimeElementBean class.
 */
public class TaskTimeElement extends TaskTimeElementBean implements Cloneable
{
    /**
     * Value to make Eclipse happy.
     */
    private static final long serialVersionUID = 1L;


    //-------------------------------------------------------------------------
    /**
     * Simple constructor.
     */
    //-------------------------------------------------------------------------
    public TaskTimeElement() {
        super();
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
     *   TaskTimeElement - the copy. If an exception is raised then null is returned.
     */
    //-------------------------------------------------------------------------
    public TaskTimeElement copy() {
        TaskTimeElement ret = null;
        try {
            ret = (TaskTimeElement)clone();
        }
        catch (CloneNotSupportedException cns) {}
        return ret;
    }
}
