@REM Run the timesheet program.
@REM
@REM Usage:
@REM   timesheet
@REM
@REM ------------------------------------------------------------------------------

@set TIMER_HOME=/Home/TIMESHEET
@set MYSQL_JAR=mysql-connector-java-5.0.4-bin.jar

@cd %TIMER_HOME%
@set CLASSPATH=lib/%MYSQL_JAR%;Ekberg-TIMESHEET-1.0.jar;lib/xstream-1.1.3.jar;lib/xpp3-1.1.3.4d_b4_min.jar
@java -d TIMER_HOME=%TIMER_HOME% org.ekberg.timer.Timesheet
