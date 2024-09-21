/* Decompiler 82ms, total 691ms, lines 167 */
package com.af.foundation.AFCommon.logging;

import com.af.foundation.AFCommon.configuration.AFConfiguration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public class AFLogger {
   private static final long MAX_FILE_SIZE;
   private Logger logger;
   private File logFile;

   private AFLogger(String logFileName) {
      this.logger = Logger.getLogger(logFileName);

      try {
         String logFolder = AFConfiguration.LOG_DIR;
         (new File(logFolder)).mkdirs();
         this.logFile = new File(logFolder + "/" + logFileName + ".log");
         AFLogger.AFlogFileHandler fileHandler = new AFLogger.AFlogFileHandler(this.logFile.getAbsolutePath());
         fileHandler.setFormatter(new AFLogger.CustomFormatter());
         Handler[] var4 = this.logger.getHandlers();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Handler handler = var4[var6];
            this.logger.removeHandler(handler);
         }

         this.logger.addHandler(fileHandler);
         Level logLevel = getLogLevel(AFConfiguration.LOG_LEVEL);
         this.logger.setLevel(logLevel);
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   public static AFLogger getLogger(String logFileName) {
      return new AFLogger(logFileName);
   }

   public void debug(String message) {
      synchronized(this.logFile) {
         this.checkForLogRollover();
         this.logger.finest(message);
      }
   }

   public void info(String message) {
      synchronized(this.logFile) {
         this.checkForLogRollover();
         this.logger.info(message);
      }
   }

   public void error(String message) {
      synchronized(this.logFile) {
         this.checkForLogRollover();
         this.logger.severe(message);
      }
   }

   private void checkForLogRollover() {
      if (this.logFile.length() >= MAX_FILE_SIZE) {
         LogFileZipper.zipFile(this.logFile);
         this.logFile.delete();

         try {
            this.logFile.createNewFile();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

   }

   private static Level getLogLevel(String logLevel) {
      String var1 = logLevel.toUpperCase();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case 78159:
         if (var1.equals("OFF")) {
            var2 = 3;
         }
         break;
      case 2251950:
         if (var1.equals("INFO")) {
            var2 = 1;
         }
         break;
      case 64921139:
         if (var1.equals("DEBUG")) {
            var2 = 2;
         }
         break;
      case 66247144:
         if (var1.equals("ERROR")) {
            var2 = 0;
         }
      }

      switch(var2) {
      case 0:
         return Level.SEVERE;
      case 1:
         return Level.INFO;
      case 2:
         return Level.FINE;
      case 3:
         return Level.OFF;
      default:
         return Level.ALL;
      }
   }

   static {
      MAX_FILE_SIZE = (long)(AFConfiguration.LOG_MAX_SIZE * 1024 * 1024);
   }

   static class AFlogFileHandler extends StreamHandler {
      private File logFile;

      public AFlogFileHandler(String fileName) throws IOException {
         this.logFile = new File(fileName);
         this.setOutputStream(new FileOutputStream(this.logFile, true));
      }

      public synchronized void publish(LogRecord record) {
         super.publish(record);
         this.flush();
      }

      public synchronized void close() throws SecurityException {
         this.flush();
      }
   }

   static class CustomFormatter extends Formatter {
      private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss z");

      public String format(LogRecord record) {
         StringBuilder sb = new StringBuilder();
         sb.append("[ ").append(dateFormat.format(new Date(record.getMillis())).toUpperCase()).append(" ] - ").append(this.getCustomLogLevelName(record.getLevel())).append(" >>> ").append(this.formatMessage(record)).append(System.lineSeparator());
         return sb.toString();
      }

      private String getCustomLogLevelName(Level level) {
         if (level == Level.SEVERE) {
            return "ERROR";
         } else if (level == Level.INFO) {
            return "INFO";
         } else {
            return level == Level.FINE ? "DEBUG" : level.getName();
         }
      }
   }
}