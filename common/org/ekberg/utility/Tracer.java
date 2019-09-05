//  @(#) $Id: Tracer.java,v 1.1.1.1 2004/11/05 23:05:07 tekberg Exp $  

//  *********************************************************************
// 
//    Copyright (c) 2004 MRG
//    All Rights Reserved
// 
//    The information contained herein is confidential to and the
//    property of MRG and is not to be disclosed
//    to any third party without prior express written permission
//    of MRG  MRG, as the
//    author and owner under 17 U.S.C. Sec. 201(b) of this work made
//    for hire, claims copyright in this material as an unpublished 
//    work under 17 U.S.C. Sec.s 102 and 104(a)   
// 
//  ******************************************************************* 


package org.ekberg.utility;


/** Used to allow common (shared) classes to use the Trace facility. */
public interface Tracer
{
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
    public void Fatal(String message, Object object);


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated fatal error message text to the configured log
     * device.  This is equivalent to calling the fatal function with a null
     * object.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public void Fatal(String message);


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated error message and the object to the configured
     * log device.
     *
     * @param  message  to be logged to the log.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    public void Error(String message, Object object);


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated error message text to the configured log device.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public void Error(String message);


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated warning message and the object to the configured
     * log device.
     *
     * @param  message  to be logged to the log device.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    public void Warning(String message, Object object);


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated warning message text to the configured log
     * device.
     *
     * @param  message  to be logged to the log device.
     **/
     // -----------------------------------------------------------------------
    public void Warning(String message);


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated notice message and the object to the configured
     * log device.
     *
     * @param  message  to be logged to the log device.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    public void Notice(String message, Object object);


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated error message text to the configured log device.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public void Notice(String message);


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated informational message and the object to the
     * configured log device.
     *
     * @param  message  to be logged to the log device.
     * @param  object  object to log.
     **/
    // ------------------------------------------------------------------------
    public void Info(String message, Object object);


    // ------------------------------------------------------------------------
    /**
     * This logs the indicated error message text to the configured log device.
     *
     * @param  message  to be logged to the log device.
     **/
    // ------------------------------------------------------------------------
    public void Info(String message);
}
