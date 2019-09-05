package org.ekberg.timer.database;


public class BasicBean
{
    /** Flag that indicates whether or not data has changed and may need to be
     * written the database. */
    private boolean isDirty = false;

    /** The primary key */
    private long id;
	
   //-------------------------------------------------------------------------
   /**
    * Check to see if an object has changed.
    *
    * @return
    *   boolean - true for dirty, false when nothing has changed.
    */
   //-------------------------------------------------------------------------
   public boolean isDirty() {
       return isDirty;
   }


   //-------------------------------------------------------------------------
   /**
    * Indicate that no objects need to be written.
    */
   //-------------------------------------------------------------------------
   protected void setClean() {
       isDirty = false;
   }


   //-------------------------------------------------------------------------
   /**
    * Indicate that an object has been modified.
    */
   //-------------------------------------------------------------------------
   protected void setDirty() {
       isDirty = true;
   }


    //-------------------------------------------------------------------------
    /**
     * Get the value of the primary key.
     *
     * @return
     *   long - the primary key value.
     */
    //-------------------------------------------------------------------------
    public long getId() {
        return id;
    }


    //-------------------------------------------------------------------------
    /**
     * Set the primary key to a new value.
     *
     * @param  id  the new primary key value.
     */
    //-------------------------------------------------------------------------
    public void setId(long id) {
        this.id = id;
        setDirty();
    }
}