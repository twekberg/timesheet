//  @(#) $Id: DatabaseBase.java,v 1.1 2005/05/16 15:52:54 tekberg Exp $  


package org.ekberg.database;


import org.ekberg.utility.TString;


/**
 * This class implements a base class for all database table classes. Common
 * methods are defined here to reduce duplication.
 */
public abstract class DatabaseBase implements Cloneable {
    /** Maximum number of objects that can be created in 1 millisecond. */
    public static final long MAX_OBJECTS_PER_MILLISECOND = 100;

    /** Keywords for Interbase. Column names can not be in this list. If we
     * change to another database then another list will be needed. */
    protected static String[] INTERBASE_KEYWORDS = {
        "ACTION", "ACTIVE", "ADD", "ADMIN", "AFTER", "ALL", "ALTER", "AND",
        "ANY", "AS", "ASC", "ASCENDING", "AT", "AUTO", "AUTODDL", "AVG",
        "BASED", "BASENAME", "BASE_NAME", "BEFORE", "BEGIN", "BETWEEN", "BLOB",
        "BLOBEDIT", "BUFFER", "BY", "CACHE", "CASCADE", "CAST", "CHAR",
        "CHARACTER", "CHARACTER_LENGTH", "CHAR_LENGTH", "CHECK",
        "CHECK_POINT_LEN", "CHECK_POINT_LENGTH", "COLLATE", "COLLATION",
        "COLUMN", "COMMIT", "COMMITTED", "COMPILETIME", "COMPUTED", "CLOSE",
        "CONDITIONAL", "CONNECT", "CONSTRAINT", "CONTAINING", "CONTINUE",
        "COUNT", "CREATE", "CSTRING", "CURRENT", "CURRENT_DATE", "CURRENT_TIME",
        "CURRENT_TIMESTAMP", "CURSOR", "DATABASE", "DATE", "DAY", "DB_KEY",
        "DEBUG", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELETE", "DESC",
        "DESCENDING", "DESCRIBE", "DESCRIPTOR", "DISCONNECT", "DISPLAY",
        "DISTINCT", "DO", "DOMAIN", "DOUBLE", "DROP", "ECHO", "EDIT", "ELSE",
        "END", "ENTRY_POINT", "ESCAPE", "EVENT", "EXCEPTION", "EXECUTE",
        "EXISTS", "EXIT", "EXTERN", "EXTERNAL", "EXTRACT", "FETCH", "FILE",
        "FILTER", "FLOAT", "FOR", "FOREIGN", "FOUND", "FREE_IT", "FROM", "FULL",
        "FUNCTION", "GDSCODE", "GENERATOR", "GEN_ID", "GLOBAL", "GOTO", "GRANT",
        "GROUP", "GROUP_COMMIT_WAIT", "GROUP_COMMIT_", "WAIT_TIME", "HAVING",
        "HELP", "HOUR", "IF", "IMMEDIATE", "IN", "INACTIVE", "INDEX",
        "INDICATOR", "INIT", "INNER", "INPUT", "INPUT_TYPE", "INSERT", "INT",
        "INTEGER", "INTO", "IS", "ISOLATION", "ISQL", "JOIN", "KEY",
        "LC_MESSAGES", "LC_TYPE", "LEFT", "LENGTH", "LEV", "LEVEL", "LIKE",
        "LOGFILE", "LOG_BUFFER_SIZE", "LOG_BUF_SIZE", "LONG", "MANUAL", "MAX",
        "MAXIMUM", "MAXIMUM_SEGMENT", "MAX_SEGMENT", "MERGE", "MESSAGE", "MIN",
        "MINIMUM", "MINUTE", "MODULE_NAME", "MONTH", "NAMES", "NATIONAL",
        "NATURAL", "NCHAR", "NO", "NOAUTO", "NOT", "NULL", "NUMERIC",
        "NUM_LOG_BUFS", "NUM_LOG_BUFFERS", "OCTET_LENGTH", "OF", "ON", "ONLY",
        "OPEN", "OPTION", "OR", "ORDER", "OUTER", "OUTPUT", "OUTPUT_TYPE",
        "OVERFLOW", "PAGE", "PAGELENGTH", "PAGES", "PAGE_SIZE", "PARAMETER",
        "PASSWORD", "PLAN", "POSITION", "POST_EVENT", "PRECISION", "PREPARE",
        "PROCEDURE", "PROTECTED", "PRIMARY", "PRIVILEGES", "PUBLIC", "QUIT",
        "RAW_PARTITIONS", "RDB$DB_KEY", "READ", "REAL", "RECORD_VERSION",
        "REFERENCES", "RELEASE", "RESERV", "RESERVING", "RESTRICT", "RETAIN",
        "RETURN", "RETURNING_VALUES", "RETURNS", "REVOKE", "RIGHT", "ROLE",
        "ROLLBACK", "RUNTIME", "SCHEMA", "SECOND", "SEGMENT", "SELECT", "SET",
        "SHADOW", "SHARED", "SHELL", "SHOW", "SINGULAR", "SIZE", "SMALLINT",
        "SNAPSHOT", "SOME", "SORT", "SQLCODE", "SQLERROR", "SQLWARNING",
        "STABILITY", "STARTING", "STARTS", "STATEMENT", "STATIC", "STATISTICS",
        "SUB_TYPE", "SUM", "SUSPEND", "TABLE", "TERMINATOR", "THEN", "TIME",
        "TIMESTAMP", "TO", "TRANSACTION", "TRANSLATE", "TRANSLATION", "TRIGGER",
        "TRIM", "TYPE", "UNCOMMITTED", "UNION", "UNIQUE", "UPDATE", "UPPER",
        "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARIABLE", "VARYING",
        "VERSION", "VIEW", "WAIT", "WEEKDAY", "WHEN", "WHENEVER", "WHERE",
        "WHILE", "WITH", "WORK", "WRITE", "YEAR", "YEARDAY"};


    //-------------------------------------------------------------------------
    /**
     * If the value is "null", return null instead.
     *
     * @param  value  the value to check
     *
     * @return
     *   String - null, or value.
     */
    //-------------------------------------------------------------------------
    public static String convertNull(String value) {
        return (value == null)
                    ? null
                    : (value.equalsIgnoreCase("null")
                                ? null
                                : value);
    }


    //-------------------------------------------------------------------------
    /**
     * Convert a null value to an empty string. Note that null is considered to
     * be either the object null or the string "null".
     *
     * @param  value  the value to convert.
     *
     * @return
     *   String - a non-null value.
     */
    //-------------------------------------------------------------------------
    public static String noNull(String value) {
        return convertNull(value) == null
                    ? ""
                    : value;
    }


    //-------------------------------------------------------------------------
    /**
     * Put quotes around a value if it is a string.
     *
     * @param  value  the value to possibly quote.
     *
     * @return
     *   String - If value is a string, put quotes around it, otherwise return
     * value.
     */
    //-------------------------------------------------------------------------
    public static String quoteString(String value) {
        return (value == null)
                    ? value
                    : "\"" + value + "\"";
    }


    //-------------------------------------------------------------------------
    /**
     * Convert a string literal to something that can be present in an SQL
     * string. This converts special characters to their alternate
     * representation. For Interbase, single quotes are encoded as two single
     * quotes. For Oracle, that also the case. For Oracle, the ampersand
     * character must be written as substr(dump(hextoraw('26'),17),-1) and then
     * concatenated with string literals with the || concatenation operator.
     *
     * @param  value  
     *
     * @return
     *   String - the encoded string literal.
     */
    //-------------------------------------------------------------------------
    public static String encodeToSql(String value) {
        String ret = value;

        if (value.indexOf("'") >= 0)
            // Only create a new object if we need to.
            ret = value.replace("'", "\'");
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Convert string literals to something that can be present in an SQL
     * string. Use the single-value encodeToSql method to do most of the work.
     *
     * @param  values  the values to convert.
     *
     * @return
     *   String[] - the encoded string literal.
     */
    //-------------------------------------------------------------------------
    public static String[] encodeToSql(String[] values) {
        String[] ret = new String[values.length];

        for(int i=0; i<values.length; i++) {
            ret[i] = encodeToSql(values[i]);
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Determine if a name is a database SQL keyword. Case is ignored.
     *
     * @param  name  the name being checked.
     *
     * @return
     *   boolean - true if name is a keyword, false if not.
     */
    //-------------------------------------------------------------------------
    public static boolean isKeyword(String name) {
        String uName = name.toUpperCase();
        return TString.create(uName).indexOf(INTERBASE_KEYWORDS) >= 0;
    }


    //-------------------------------------------------------------------------
    /**
     * Create a clone of this object. This is needed by those database classes
     * that implement the Cloneable interface.
     *
     * @return
     *   Object - the clone.
     */
    //-------------------------------------------------------------------------
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    //-------------------------------------------------------------------------
    /**
     * Get a new ID for an object being created. This uses the running
     * objectCount to attempt to make sure that there are no key clashes.
     * If two objects are created in the same millisecond, the objectCount
     * will indicate the ordering, unless MAX_OBJECTS_PER_MILLISECOND is
     * exceeded.
     *
     * @return
     *   long - a brand new ID for this object.
     */
    //-------------------------------------------------------------------------
    public long getNextID() {
        long thisMsTime = System.currentTimeMillis();
        if (thisMsTime != getLastMsTime()) {
            // Starting a new millisecond. Reset objectCount back to 0 so that
            // this object will have an ID that is < all other objects created
            // in this millisecond.
            setObjectCount(0);
            setLastMsTime(thisMsTime);
        }
        long ID = thisMsTime * MAX_OBJECTS_PER_MILLISECOND + getObjectCount();
        setObjectCount((getObjectCount() + 1) % MAX_OBJECTS_PER_MILLISECOND);
        return ID;
    }


    //-------------------------------------------------------------------------
    /**
     * Get the millisecond time of the last object that was created. There is a
     * separate time for each database object.
     *
     * @return
     *   long - the last object millisecond time.
     */
    //-------------------------------------------------------------------------
    protected abstract long getLastMsTime();


    //-------------------------------------------------------------------------
    /**
     * Update the millisecond time for a particular concete database class.
     *
     * @param  lastMsTime  the new millsecond time.
     */
    //-------------------------------------------------------------------------
    protected abstract void setLastMsTime(long lastMsTime);


    //-------------------------------------------------------------------------
    /**
     * Get the object count for a particular object type. An object ID is
     * constructed from the millisecond time and this count. Each millisecond
     * this count is set to zero so we can see how many objects are created
     * each millsecond.
     *
     * @return
     *   long - the current object count for an object type.
     */
    //-------------------------------------------------------------------------
    protected abstract long getObjectCount();


    //-------------------------------------------------------------------------
    /**
     * Update the object count for a particular concrete database class.
     *
     * @param  objectCount  the new object count.
     */
    //-------------------------------------------------------------------------
    protected abstract void setObjectCount(long objectCount);
}
