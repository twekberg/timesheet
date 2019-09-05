//  @(#) $Id:  $


package org.ekberg.timer.utility;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;


// ----------------------------------------------------------------------------
/**
 * This class contains the trace filter.
 *
 * @see org.ekberg.meth.utility.Trace
 **/
// ----------------------------------------------------------------------------
class TraceFilter implements TraceConstants {

    private static final String PACKAGE  = "Package";
    private static final String CLASS  = "Class";
    private static final String FIELD  = "Field";

    /** Collection of included names. */
    private HashMap<String,LinkedList<String>> includeMap = null;

    /** Collection of excluded names. */
    private HashMap<String,LinkedList<String>> excludeMap = null;

    /** Trace level. */
    private int level = 0;


    // ------------------------------------------------------------------------
    /**
     * This constructor accepts the config name of the TraceDest 
     * object it is attached to and uses it to look up 
     * filter-specific config data for that destination.  That
     * data defines the include and exclude lists for packages
     * and classes, and the fields to output in a trace log.
     *
     * @param  destToken  config name of the "owner" TraceDest object.
     **/
    // ------------------------------------------------------------------------
    TraceFilter(String destToken) {
        includeMap = new HashMap<String,LinkedList<String>>();
        excludeMap = new HashMap<String,LinkedList<String>>();
        
        String prefix = CONFIG_PREFIX + destToken + ".";
        String workStr;
        
        // Get and set the trace level.
        level = Config.getInt(prefix + CONFIG_LEVEL, RUNTIME_LEVEL);

        // Get and set the included packages.
        workStr = Config.getString(prefix + CONFIG_INCL_PACKAGE);
        setIncludeContain(workStr, PACKAGE);

        // Get and set the included classes.
        workStr = Config.getString(prefix + CONFIG_INCL_CLASS);
        setIncludeContain(workStr, CLASS);

        // Get and set the excluded packages.
        workStr = Config.getString(prefix + CONFIG_EXCL_PACKAGE);
        setExcludeContain(workStr, PACKAGE);

        // Get and set the excluded classes.
        workStr = Config.getString(prefix + CONFIG_EXCL_CLASS);
        setExcludeContain(workStr, CLASS);

        // Get and set the included fields.
        workStr = Config.getString(prefix + CONFIG_INCL_FIELD);
        setIncludeContain(workStr, FIELD);
    }


    //-------------------------------------------------------------------------
    /**
     * Return the trace level for this filter.
     *
     * @return
     *   int - the trace  level.
     */
    //-------------------------------------------------------------------------
    int getLevel() {
        return level;
    }


    // ------------------------------------------------------------------------
    /**
     * Go through the filter with the trace event.
     *
     * @param  event  trace event
     *
     * @return
     *   boolean - indicates if the event should be sent (true) or not sent
     * (false) to the log device.
     **/
    // ------------------------------------------------------------------------
    boolean isEventWanted(TraceEvent event) {
        // Check for level filtering first.
        if (event.traceLevel > level)
            return false;
        
        // Next, override filtering for error cases.
        if (event.traceLevel <= Trace.ERROR)
            return true;
        
        // Now check for include/exclude lists.
        boolean outputFlag      = true;
        boolean packageExcluded = false;
        boolean classExcluded   = false;
        boolean packageIncluded = true;

        if ((getExclude(PACKAGE) != null) && (isInExcludePackage(event))) {
            // The package is excluded.
            packageExcluded = true;
        }

        if ((getExclude(CLASS) != null) && (isInExcludeClass(event))) {
            // The class is excluded.
            classExcluded = true;
        }

        if ((getInclude(PACKAGE) != null) && (getInclude(PACKAGE).size() > 0)) {
            if (!isInIncludePackage(event)) {
                // The package is not included.
                packageIncluded = false;
            }
        }

        if ((getInclude(CLASS) == null) || 
                ((getInclude(CLASS) != null) && (getInclude(CLASS).size() == 0))) {
            // The include list is empty, all classes are included except
            // packages/classes specified in the excluding list.
            if (classExcluded || packageExcluded)
                outputFlag = false;
            else
                outputFlag = true;
        }
        else {
            if (isInIncludeClass(event) || packageIncluded) {
                // The class is included.
                outputFlag = true;
            }
            else {
                // The class is not included.
                outputFlag = false;
            }
        }

        return outputFlag;
    }


    // ------------------------------------------------------------------------
    /**
     * Go through the filter with the field name.
     *
     * @param  field  field name
     *
     * @return 
     *   boolean - true if the field name is in the list, false otherwise.
     **/
    // ------------------------------------------------------------------------
    boolean isFieldToPrint(String field) {
        LinkedList nameList;
        int i;
        String name;
        boolean flag;

        flag = false;

        // Check the FIELD include list.
        nameList = getInclude(FIELD);
        if (nameList != null) {
            for (i = 0; i < nameList.size(); i++) {
                name = (String)nameList.get(i);
                if (name.equals(field)) {
                    flag = true;
                    break;
                }
            }
        }
        else {
            // Null field list -- include them all.
            flag = true;
        }

        return flag;
    }


    // ------------------------------------------------------------------------
    /**
     * Set the trace level.
     *
     * @param  traceLevel  the new trace level.
     **/
    // ------------------------------------------------------------------------
    void setTraceLevel(int traceLevel) {
        level = traceLevel;
    }


    // ------------------------------------------------------------------------
    /**
     * Get the trace level.
     *
     * @return  level  trace level.
     **/
    // ------------------------------------------------------------------------
    int getTraceLevel() {
        return (level);
    }


    // ------------------------------------------------------------------------
    /**
     * Put all included class/package names in the linked list.
     *
     * @param  nameStr  class/package names.
     * @param  typeStr  class/package.
     **/
    // ------------------------------------------------------------------------
    private void setIncludeContain(String nameStr, String typeStr) {
        String nameToken;

        if (nameStr != null) {
            StringTokenizer stName = new StringTokenizer(nameStr, " ");
            LinkedList<String> nameList = new LinkedList<String>();
            while (stName.hasMoreTokens()) {
                nameToken = stName.nextToken();
                nameList.addLast(nameToken);
            }
            includeMap.put(typeStr, nameList);
        }
    }


    // ------------------------------------------------------------------------
    /**
     * Put all excluded class/package names in the linked list.
     *
     * @param  nameStr  class/package names.
     * @param  typeStr  class/package.
     **/
    // ------------------------------------------------------------------------
    private void setExcludeContain(String nameStr, String typeStr) {
        String nameToken;

        if (nameStr != null) {
            StringTokenizer stName = new StringTokenizer(nameStr, " ");
            LinkedList<String> nameList = new LinkedList<String>();
            while (stName.hasMoreTokens()) {
                nameToken = stName.nextToken();
                nameList.addLast(nameToken);
            }
            excludeMap.put(typeStr, nameList);
        }
    }


    // ------------------------------------------------------------------------
    /**
     * Get included names from include map
     *
     * @param  type  type associated with this list.
     *
     * @return
     *   nameList - list of included specified type.
     **/
    // ------------------------------------------------------------------------
    private LinkedList getInclude(String type) {
        return ((LinkedList)includeMap.get(type));
    }


    // ------------------------------------------------------------------------
    /**
     * Get excluded names from exclude map.
     *
     * @param  type  type associated with this list.
     *
     * @return 
     *   nameList - list of excluded specified type.
     **/
    // ------------------------------------------------------------------------
    private LinkedList getExclude(String type) {
        return ((LinkedList)excludeMap.get(type));
    }


    // ------------------------------------------------------------------------
    /**
     * Go through the filter with the trace event.
     *
     * @param  event  trace event.
     *
     * @return
     *   boolean - true if the name is in the list, false otherwise.
     **/
    // ------------------------------------------------------------------------
    private boolean isInIncludePackage(TraceEvent event) {
        LinkedList nameList;
        int i;
        String name;
        boolean flag;

        flag = false;

        // Check the PACKAGE include list.
        nameList = getInclude(PACKAGE);
        if (nameList != null) {
            for (i = 0; i < nameList.size(); i++) {
                name = (String)nameList.get(i);
                if (name.equals(event.basePackageName)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }


    // ------------------------------------------------------------------------
    /**
     * Go through the filter with the trace event.
     *
     * @param  event  trace event.
     *
     * @return
     *   boolean - true if the name is in the list, false otherwise.
     **/
    // ------------------------------------------------------------------------
    private boolean isInIncludeClass(TraceEvent event) {
        LinkedList nameList;
        int i;
        String name;
        boolean flag;

        flag = false;

        // Check the CLASS include list.
        nameList = getInclude(CLASS);
        if (nameList != null) {
            for (i = 0; i < nameList.size(); i++) {
                name = (String)nameList.get(i);
                if (name.equals(event.className)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }


    // ------------------------------------------------------------------------
    /**
     * Go through the filter with the trace event.
     *
     * @param  event  trace event.
     *
     * @return
     *   boolean - true if the name is in the list, false otherwise.
     **/
    // ------------------------------------------------------------------------
    private boolean isInExcludePackage(TraceEvent event) {
        LinkedList nameList;
        int i;
        String name;
        boolean flag;

        flag = false;

        // Check the PACKAGE exclude list.
        nameList = getExclude(PACKAGE);
        if (nameList != null) {
            for (i = 0; i < nameList.size(); i++) {
                name = (String)nameList.get(i);
                if (name.equals(event.basePackageName)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }


    // ------------------------------------------------------------------------
    /**
     * Go through the filter with the trace event.
     *
     * @param  event  trace event.
     *
     * @return
     *   boolean - true if the name is in the list, false otherwise.
     **/
    // ------------------------------------------------------------------------
    private boolean isInExcludeClass(TraceEvent event) {
        LinkedList nameList;
        int i;
        String name;
        boolean flag;

        flag = false;

        // Check the CLASS include list.
        nameList = getExclude(CLASS);
        if (nameList != null) {
            for (i = 0; i < nameList.size(); i++) {
                name = (String)nameList.get(i);
                if (name.equals(event.className)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
