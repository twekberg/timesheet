// $Id:  $

package org.ekberg.timer.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.ekberg.timer.utility.Config;
import org.ekberg.timer.utility.Trace;


/**
 * Provides a simple database connection pooling mechanism.
 */
public class AdminConnectionManager extends DatabaseConnectionManager {

    private static AdminConnectionManager _instance = new AdminConnectionManager();

    /** Use Config.getString with this property name to get the
     * username for this connection. */ 
    private final static String USERNAME_CONFIG = "database.admin.username";

    /** Use Config.getString with this property name to get the
     * password for this connection. */
    private final static String PASSWORD_CONFIG = "database.admin.password";

    /** Property that defines where the database is located. */
    private static final String URL_CONFIG = "database.admin.url";

    /** Maximum number of connections to create for this database. */
    protected static final int MAX_CONNECTIONS = 3;

    /** Cache for the properties. */
    private static Properties properties;

    /** Cache for the url. */
    private static String url;


    protected AdminConnectionManager() {
    }


    //-------------------------------------------------------------------------
    /**
     * Get a connection to the database.
     *
     * @param  info  information regarding the caller requesting the
     * connection. This is used to isolate database problems.
     *
     * @return
     *   Connection - the connection.
     *
     * @throws SQLException 
     */
    //-------------------------------------------------------------------------
    public static synchronized Connection getConnection(String info) throws SQLException {
        return _instance.getNext(info);
    }


    public static synchronized void releaseConnection(Connection connection) {
        _instance.release(connection);
    }


    public static String getStatus() {
        return _instance.toString();
    }


    public static String asString() {
        return _instance.getInfos();
    }


    //-------------------------------------------------------------------------
    /**
     * Get the properties that are related to this connection. At a minimum
     * this includes the user and password properties. Using this approach
     * allows for other connection-related parameters to be passed.
     *
     * @return
     *   Properties - a property list for this type of database.
     */
    //-------------------------------------------------------------------------
    protected Properties getConnectionProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user",     Config.getString(USERNAME_CONFIG));
            properties.setProperty("password", Config.getString(PASSWORD_CONFIG));
        }
        return properties;
    }


    //-------------------------------------------------------------------------
    /**
     * Get the URL for the database.
     *
     * @return
     *   String - the URL.
     */
    //-------------------------------------------------------------------------
    protected String getUrl() {
        if (url == null) {
            url = Config.getString(URL_CONFIG);
        }
        return url;
    }


    //-------------------------------------------------------------------------
    /**
     * Return the maximum number of connections to cache for this database.
     *
     * @return
     *   int - maximum connection count.
     */
    //-------------------------------------------------------------------------
    protected int getMaxConnections() {
        return MAX_CONNECTIONS;
    }
}
