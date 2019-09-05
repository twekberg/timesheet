//  *********************************************************************
// 
//    Copyright (c) 2004 Tom Ekberg
//    All Rights Reserved
// 
//    The information contained herein is confidential to and the
//    property of Tom Ekberg and is not to be disclosed
//    to any third party without prior express written permission
//    of Tom Ekberg  Tom Ekberg, as the
//    author and owner under 17 U.S.C. Sec. 201(b) of this work made
//    for hire, claims copyright in this material as an unpublished 
//    work under 17 U.S.C. Sec.s 102 and 104(a)   
// 
//  ******************************************************************* 


package org.ekberg.timer.utility;


import java.io.FileInputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;

/**************************************************************************
 * System configuration class - front end to get data from a common
 * properties file.
 *************************************************************************/
public class Config 
{
    public static final String APP_HOME = "TIMER_HOME";

    private static final String APP_PROPERTY_FILENAME = "timesheet.properties";

    /**
     * This variable refers to the properties list currently being operated
     * on.
     */
    private static Properties properties;

    private static String slash;

    //=====================================================================
    // Public Methods
    //=====================================================================

    // --------------------------------------------------------------------
    /**
     * Adds or sets the value of the property referred to by name.
     *
     * @param  name  name of property to set.
     * @param  value  value of property to set.
     *
     * @return void
     *
     * @see java.util.Properties#setProperty(String, String)
     * @see java.lang.System#setProperty(String,String)
     */
    // --------------------------------------------------------------------
    public static void setProperty(String name, String value) 
    {
        properties.setProperty(name, value);
    }


    // --------------------------------------------------------------------
    /**
     * Returns a string value from the configuration data.  The caller
     * supplies a full qualified name of a property, and a defaultValue if
     * the property does not exist.
     *
     * @param  name  of property to get.
     * @param  defaultValue  to return if named property doesn't exist.
     *
     * @return
     *   String - value of requested property or defaultValue if property
     * doesn't exist.
     *
     * @see java.util.Properties#getProperty(String)
     */
    // --------------------------------------------------------------------
    public static String getString(String name, String defaultValue) 
    {
        String result = properties.getProperty(name, defaultValue);
        result = System.getProperty(name, result);
        return result;
    }


    // --------------------------------------------------------------------
    /**
     * Returns the value of the property referred to by name or null if the
     * named property does not exist.
     *
     * @param  name  of property to get.
     *
     * @return
     *   String - value of requested property or null if property doesn't
     * exist.
     *
     * @see java.util.Properties#getProperty(String)
     */
    // --------------------------------------------------------------------
    public static String getString(String name) 
    {
        String result = properties.getProperty(name);
        if (result == null)
            result = System.getProperty(name);
        else
            result = System.getProperty(name, result);
        return result;
    }
    

    // --------------------------------------------------------------------
    /**
     * Returns an Integer value from the configuration data.  The caller
     * supplies a full qualified name of a property, and a defaultValue if
     * the property does not exist.
     *     
     * @param  name  of property to get.
     * @param  defaultValue  to return if named property doesn't exist.
     *
     * @return
     *   Integer -  object with value of requested property or defaultValue
     * if property doesn't exist.
     *
     * @see java.util.Properties#getProperty(String)
     */
    // --------------------------------------------------------------------
    public static Integer getInteger(String name, int defaultValue) 
    {
        Integer result = getInteger(name);
        if (result == null)
            result = new Integer(defaultValue);
        return result;
    }
    

    // --------------------------------------------------------------------
    /**
     * Returns an integer value from the configuration data.  The caller
     * supplies a full qualified name of a property.
     *     
     * @param  name  of property to get.
     *
     * @return
     *   Integer - object with value of requested property or null if
     * property doesn't exist.
     *
     * @see java.util.Properties#getProperty(String)
     * @see java.lang.Integer#getInteger(String)
     */
    // --------------------------------------------------------------------
    public static Integer getInteger(String name)
    {
        Integer result;
        String stringValue = getString(name);
        if (stringValue == null)
            result = null;
        else {
            try {
                result = Integer.decode(stringValue);
            }
            catch (NumberFormatException e) {
                result = null;
            }
        }
        return result;
    }
    

    // --------------------------------------------------------------------
    /**
     * Returns an int value from the configuration data.  The caller
     * supplies a full qualified name of a property, and a defaultValue if
     * the property does not exist.
     *     
     * @param  name  of property to get.
     * @param  defaultValue  to return if named property doesn't exist.
     *
     * @return
     *   int - value of requested property or defaultValue if property
     * doesn't exist.
     *
     * @see java.util.Properties#getProperty(String)
     * @see java.lang.Integer#getInteger(String)
     */
    // --------------------------------------------------------------------
    public static int getInt(String name, int defaultValue) 
    {
        return getInteger(name, defaultValue).intValue();
    }
    

    // --------------------------------------------------------------------
    /**
     * Returns a boolean value from the configuration data.  The caller
     * supplies a full qualified name of a property, and a defaultValue if
     * the property does not exist.
     *     
     * @param  name  of property to get.
     * @param  defaultValue  to return if named property doesn't exist.
     *
     * @return
     *   boolean - value of requested property or defaultValue if property
     * doesn't exist.
     */
    // --------------------------------------------------------------------
    public static boolean getBoolean(String name, boolean defaultValue)
    {
        boolean result;
        String stringValue = getString(name);
        if (stringValue == null)
            result = defaultValue;
        else {
            result = Boolean.valueOf(stringValue).booleanValue();
        }
        return result;
    }

    
    // --------------------------------------------------------------------
    /**
     * Returns a short value from the configuration data.  The caller
     * supplies a full qualified name of a property, and a defaultValue if
     * the property does not exist.
     *     
     * @param  name  of property to get.
     * @param  defaultValue  to return if named property doesn't exist.
     *
     * @return
     *   short - value of requested property or defaultValue if property
     * doesn't exist.
     */
    // --------------------------------------------------------------------
  public static short getShort(String name, short defaultValue)
    {
        Short result = null;
        String stringValue = getString(name);
        if (stringValue != null)
        {
            try {
                result = Short.decode(stringValue);
            }
            catch (NumberFormatException e) {}
        }
        return result == null ? defaultValue : result.shortValue();
    }


    // --------------------------------------------------------------------
    /**
     * Returns a long value from the configuration data.  The caller
     * supplies a full qualified name of a property, and a defaultValue if
     * the property does not exist.
     *     
     * @param  name  of property to get.
     * @param  defaultValue  to return if named property doesn't exist.
     *
     * @return
     *   long - value of requested property or defaultValue if property
     * doesn't exist.
     */
    // --------------------------------------------------------------------
  public static long getLong(String name, long defaultValue)
    {
        Long result = null;
        String stringValue = getString(name);
        if (stringValue != null)
        {
            try {
                result = Long.decode(stringValue);
            }
            catch (NumberFormatException e) {}
        }
        return result == null ? defaultValue : result.longValue();
    }


    // --------------------------------------------------------------------
    /**
     * Returns a float value from the configuration data.  The caller
     * supplies a full qualified name of a property, and a defaultValue if
     * the property does not exist.
     *     
     * @param  name  of property to get.
     * @param  defaultValue  to return if named property doesn't exist.
     *
     * @return
     *   float - value of requested property or defaultValue if property
     * doesn't exist.
     */
    // --------------------------------------------------------------------
  public static float getFloat(String name, float defaultValue)
    {
        Float result = null;
        String stringValue = getString(name);
        if (stringValue != null)
        {
            try {
                result = Float.valueOf(stringValue);
            }
            catch (NumberFormatException e) {}
        }
        return result == null ? defaultValue : result.floatValue();
    }


    // --------------------------------------------------------------------
    /**
     * Returns a double value from the configuration data.  The caller
     * supplies a full qualified name of a property, and a defaultValue if
     * the property does not exist.
     *     
     * @param  name  of property to get.
     * @param  defaultValue  to return if named property doesn't exist.
     *
     * @return
     *   double - value of requested property or defaultValue if property
     * doesn't exist.
     */
    // --------------------------------------------------------------------
  public static double getDouble(String name, double defaultValue)
    {
        Double result = null;
        String stringValue = getString(name);
        if (stringValue != null)
        {
            try {
                result = Double.valueOf(stringValue);
            }
            catch (NumberFormatException e) {}
        }
        return result == null ? defaultValue : result.doubleValue();
    }


    // --------------------------------------------------------------------
    /**
     * Returns a Collection of the property names that start with the
     * specified prefix.
     *
     * @param  prefix  the prefix to match names against.
     *
     * @return
     *   Collection - matching names that can be iterated.
     */
    // --------------------------------------------------------------------
    public static List<String> getMatchingNames(String prefix)
    {
        LinkedList<String> list = new LinkedList<String>();
        try {
            Enumeration names = properties.propertyNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                if (name.startsWith(prefix)) {
                    list.add(name);
                }
            }
        }
        catch (Exception ignore) { }
        return list;
    }


    //=====================================================================
    // Private Methods
    //=====================================================================

    // --------------------------------------------------------------------
    /**
     *  Required for preventing instantiation.
     */
    // --------------------------------------------------------------------
    private Config()
    {
    }

    // --------------------------------------------------------------------
    /**
     *  Static class initializer - loads the properties.
     */
    // --------------------------------------------------------------------
    static
    {
        // Don't use System.getProperty("file.separator", "/") since it causes
        // an inconsistency with the slashes.
        slash = "/";
        properties = new Properties();

        
        // First load the application wide properties.
        String configBase = System.getProperty(APP_HOME, "") + slash + "config" +
                    slash;
        // Do this just to make sure all of the /'s are the same.
        configBase = configBase.replace("\\", "/");

        String propFile = configBase + APP_PROPERTY_FILENAME;
        loadFile(propFile);
    }


    // --------------------------------------------------------------------
    /**
     *  Reads properties from the given file into this list.
     *
     *  @param propertyFilename
     */
    // --------------------------------------------------------------------
    private static void loadFile(String propertyFilename) 
    {
        FileInputStream file;
        try {
            file = new FileInputStream(propertyFilename);
            properties.load(file);
            file.close();
        }
        catch (SecurityException ignore) {}
        catch (java.io.FileNotFoundException ignore) {}
        catch (Exception e) {
            System.err.println("cannot load properties file " +
                        propertyFilename + ": " + e);
        }
    }


    //=====================================================================
    // Main
    //=====================================================================


    // --------------------------------------------------------------------
    /**
     * This main acts as a little test program.
     **/
    // --------------------------------------------------------------------
    public static void main(String[] args) 
    {
        System.out.println(Config.getString(args[0]));
    }
}
