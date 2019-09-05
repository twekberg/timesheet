//  @(#) $Id:  $  

//  *********************************************************************
// 
//    Copyright (c) 2003 Thomas W. Ekberg
//    All Rights Reserved
// 
//    The information contained herein is confidential to and the
//    property of Thomas W. Ekberg and is not to be disclosed
//    to any third party without prior express written permission
//    of Thomas W. Ekberg  Thomas W. Ekberg, as the
//    author and owner under 17 U.S.C. Sec. 201(b) of this work made
//    for hire, claims copyright in this material as an unpublished 
//    work under 17 U.S.C. Sec.s 102 and 104(a)   
// 
//  ******************************************************************* 


package org.ekberg.utility;


/**
 * This interface is used by the apply methods in TextUtil. It defines the
 * apply method. Refer to that method for more details.
 */
public interface Applier {
    // ------------------------------------------------------------------------
    /**
     * Apply this method to an element to perform some calculation and return
     * the result. The element is guaranteed to not be null. Throwing an
     * exception in this method is the same as returning null - both cause the
     * element to be ignored.
     *
     * @param  element  the element to be applied.
     *
     * @return
     *   Object - some result.
     * elements.
     */
    // ------------------------------------------------------------------------
    public Object apply(Object element);
}
