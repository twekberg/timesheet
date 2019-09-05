// $Id:  $


package org.ekberg.timer.database;


import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import org.ekberg.timer.utility.Config;
import org.ekberg.timer.utility.Trace;


/**
 * Provides a simple database connection pooling mechanism.
 */
public abstract class DatabaseConnectionManager {

    /** Error message for ClassNotFoundException during initialization. */
    protected static final String INIT_ERROR_MESSAGE = "FATAL: Unable to load proper driver";

    /** Error message when all connections are busy and we need one.. */
    protected static final String BUSY_ERROR_MESSAGE = "FATAL: All connections are busy";

    /** FATAL error occured during one of the initializations. There is an
     * Boolean value for each user name.  */
    protected static Hashtable<String,Boolean> _initFailed = new Hashtable<String,Boolean>();

    /** Maximum number of "calling from here" messages saved for each connection. */
    protected static final int MAX_INFOS = 10;

    protected Connection[] pool;

    protected boolean[] busy;

    protected List<String>[] infos;

    private int ConnectionNumber = 0;

    /* Maximum number of attempts to get a good connection after one attempt
     * fails. */
    public static final int MAX_CONNECTION_RETRIES = 2;

    /** Property to use when loading the database driver. */
    protected static final String DRIVER_CONFIG = "database.driver";


    //-------------------------------------------------------------------------
    /**
     * Creates a DatabaseConnectionManager and creates the connection.
     */
    //-------------------------------------------------------------------------
    protected DatabaseConnectionManager() {
        pool = new Connection[getMaxConnections()];
        busy = new boolean[getMaxConnections()];
        infos = (List<String>[])(new ArrayList[getMaxConnections()]);
        String driver = Config.getString(DRIVER_CONFIG);
        boolean ok = true;

        try {
            // Loading the class also registers the driver.
            Class.forName(driver);
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("TWE FATAL: DatabaseDriver exception in DatabaseConnectionManager");
            cnfe.printStackTrace();
            System.err.println("TWE        ClassNotFoundException in DatabaseConnectionManager constructor->" + cnfe.getMessage());
            System.err.println("       driver=\"" + driver + "\"");

            Trace.error("FATAL: DatabaseDriver exception in DatabaseConnectionManager", cnfe);
            Trace.error("       ClassNotFoundException in DatabaseConnectionManager constructor->" + cnfe.getMessage());
            Trace.error("       driver=\"" + driver + "\"");
            _initFailed.put(getUserName(), new Boolean(true));
            ok = false;
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Get the next available connection object.
     *
     * @param  info  information regarding the caller requesting the
     * connection. This is used to isolate database problems.
     *
     * @return
     *   Connection - an available connection object from the pool.
     *
     * @throws SQLException 
     */
    //-------------------------------------------------------------------------
    protected Connection getNext(String info) throws SQLException {
	int i = ConnectionNumber;
        int lastIndex = i;

        if (getInitFailed()) throw new SQLException(INIT_ERROR_MESSAGE);

        // Position to the next one that is not busy.
        do {
            if (!busy[i])
                break;
            i++;
            // Wrap around to first one.
            if (i == pool.length)
		i = 0;
        }
        while(i != lastIndex);

        // Start here the next time a connection is needed.
        ConnectionNumber = i + 1;
        if (ConnectionNumber == pool.length)
		ConnectionNumber = 0;

        if (!busy[i]) {
            busy[i] = true;
            infos[i].add((new Date()) + " " + info);
            if (infos[i].size() > MAX_INFOS) {
                infos[i].remove(0);
            }
            return pool[i];
        }
        // If we get to this point then all connection objects were busy.
        throw new SQLException(BUSY_ERROR_MESSAGE);
    }


    //-------------------------------------------------------------------------
    /**
     * Get a fresh new connection object.
     *
     * @return
     *   Connection - a connection object.
     *
     * @throws SQLException 
     */
    //-------------------------------------------------------------------------
    protected Connection newConnection() throws SQLException {
        Properties props = getConnectionProperties();
        String user      = props.getProperty("user");
        String password  = props.getProperty("password");

        Connection connection = DriverManager.getConnection(getUrl(),
                    user, password);

        // Set the transaction isolation level to the maximum.
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return connection;
    }


    //-------------------------------------------------------------------------
    /**
     * The user of the connection is indicating that it is no longer
     * needed. Put it back in the pool for another user.
     *
     * @param  connection  the connection to release.
     */
    //-------------------------------------------------------------------------
    public void release(Connection connection) {
	  for (int i=0; i<pool.length; i++) {
              if (pool[i] == connection) {
                  busy[i] = false;
                  break;
              }
          }
    }


    //-------------------------------------------------------------------------
    /**
     * Get the info strings combined in a reasonable manner.
     *
     * @return
     *   String - the infos array elements, combined into a string.
     */
    //-------------------------------------------------------------------------
    protected synchronized String getInfos() {
        StringBuffer sb = new StringBuffer();
        List info;

        sb.append("ConnectionNumber=" + ConnectionNumber);
        sb.append("\n");
        for(int i=0; i<infos.length; i++) {
            info = infos[i];
            sb.append("Connection " + i + " info.size()=" + info.size());
            sb.append("\n");
            sb.append("busy[" + i + "]=" + busy[i]);
            sb.append("\n");
            for(int j=0; j<info.size(); j++) {
                sb.append(info.get(j));
                sb.append("\n");
            }
        }
        return sb.toString();
    }


    //-------------------------------------------------------------------------
    /**
     * Create a pool of connection objects.
     */
    //-------------------------------------------------------------------------
    protected void createPool() {
	try {
	  for (int i=0; i<pool.length; i++) {
	  	pool[i] = newConnection();
                busy[i] = false;
                infos[i] = new ArrayList<String>();
	  }
	} catch (SQLException sqle) {
            Trace.error("Unable to start database connection");
        }
    }


    //-------------------------------------------------------------------------
    /**
     * Determine whether or not the initialization for this type of connection
     * was successful or not.
     *
     * @return
     *   boolean - true if the initialization failed, false if initialization
     * was successful.
     */
    //-------------------------------------------------------------------------
    protected boolean getInitFailed() {
        Boolean initFailed = (Boolean)_initFailed.get(getUserName());
        if (initFailed == null) {
            // Defaults to false - init went OK.
            initFailed = new Boolean(false);
            _initFailed.put(getUserName(), initFailed);

	    // Create connection pool. Note that the pool will only be created
	    // if the above get() call returned null.
	    createPool();
        }
        return initFailed.booleanValue();
    }


    //-------------------------------------------------------------------------
    /**
     * Output interesting information relating to this connection manager.
     *
     * @return
     *   String - nicely formatted connection data.
     */
    //-------------------------------------------------------------------------
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("DatabaseConnectionManager[");
        sb.append("ConnectionNumber=");
        sb.append(ConnectionNumber);
        sb.append(", pool[open,busy]=[");
        for(int i=0; i<pool.length; i++) {
            Connection connection = pool[i];
            if (i > 0)
                sb.append(", ");
            sb.append(i);
            sb.append(":[");
            if (connection == null)
                sb.append("null");
            else {
                try {
                    sb.append(!connection.isClosed());
                }
                catch (SQLException e) {
                    sb.append("false");
                }
            }
            sb.append(",");
            sb.append(busy[i]);
            sb.append("]");
        }
        sb.append("]");
        sb.append("]");
        return sb.toString();
    }


    //-------------------------------------------------------------------------
    /**
     * Shorthand for getting the user name.
     *
     * @return
     *   String - the user name.
     */
    //-------------------------------------------------------------------------
    protected String getUserName() {
        return getConnectionProperties().getProperty("user");
    }


    protected abstract Properties getConnectionProperties();
    protected abstract String getUrl();
    protected abstract int getMaxConnections();
}
