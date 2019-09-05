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


import java.util.Date;


/** Interface used when one wants to know when the day changes. */
public interface NewDayListener extends TimeChangedListener {

    //-------------------------------------------------------------------------
    /**
     * This method is called when the day changes.
     *
     * @param  previousDay  the Date before the day changed.
     */
    //-------------------------------------------------------------------------
    public void action(Date previousDay);
}
