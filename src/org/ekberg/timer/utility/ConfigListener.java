//  @(#) $Id:  $


package org.ekberg.timer.utility;

/**
 * A value related to the meth.properties has changed. Use this listener to determine
 * what was changed to perform some action.
 */
public interface ConfigListener {
    // ------------------------------------------------------------------------
    /**
     * Invoked when a dynamically configurable value in Config.properties has changed.
     *
     */
    // ------------------------------------------------------------------------
    public void ConfigChanged();
}
