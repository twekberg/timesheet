#!/bin/sh
#
# Run the checkbook program.
#
# Usage:
#   check
#
#------------------------------------------------------------------------------

set -x
export TIMER_HOME="TIMESHEET"
export MYSQL_JAR=mysql-connector-java-5.0.4-bin.jar

cd $TIMER_HOME
export CLASSPATH="lib/$MYSQL_JAR;Ekberg-TIMESHEET-1.0.jar;lib/xstream-1.1.3.jar;lib/xpp3-1.1.3.4d_b4_min.jar"
java -DTIMER_HOME="$TIMER_HOME" org.ekberg.timer.Timesheet
