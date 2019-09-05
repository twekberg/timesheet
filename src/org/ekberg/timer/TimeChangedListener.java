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


package org.ekberg.timer;


import java.util.Date;


/** Interface used when one wants to know that the time has changed. */
public interface TimeChangedListener
{
    //-------------------------------------------------------------------------
    /**
     * This method is called when the time changes.
     *
     * @param  date  some kind of date object. For NewDayListener this will for
     * the previous day.
     */
    //-------------------------------------------------------------------------
    public void action(Date date);
}
