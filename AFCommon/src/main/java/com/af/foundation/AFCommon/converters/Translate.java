/* Decompiler 68ms, total 815ms, lines 156 */
package com.af.foundation.AFCommon.converters;

import com.af.foundation.AFCommon.configuration.AFConfiguration;
import com.af.foundation.AFCommon.exceptions.AFCommonExceptions;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;

public class Translate {
   private final List<String> QUERY_TYPES = Arrays.asList("S", "I");
   static final String SEPERATOR = "/";
   private String tableName = "";

   public String getTableName() {
      return this.tableName;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }

   public String getDisplayOrderQuerry() {
      String var1 = AFConfiguration.DB_TYPE;
      byte var2 = -1;
      switch(var1.hashCode()) {
      case 104203880:
         if (var1.equals("mssql")) {
            var2 = 0;
         }
      default:
         switch(var2) {
         case 0:
            return "SELECT ORDINAL_POSITION,COLUMN_NAME,IS_NULLABLE,DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + this.getTableName() + "';";
         default:
            throw new AFCommonExceptions("Un supporting DB type: " + AFConfiguration.DB_TYPE);
         }
      }
   }

   public static String jsonToQueryString(JSONObject inJsonObject) {
      String queryString = inJsonObject.getString("OPERATION").toUpperCase() + "/";
      String fields = inJsonObject.getString("FIELDS");
      if (!fields.isBlank() || !fields.isEmpty()) {
         queryString = queryString + fields;
      }

      queryString = queryString + "/";
      queryString = queryString + inJsonObject.getString("APPLICATION_NAME") + "/";
      queryString = queryString + inJsonObject.getString("DATA");
      return queryString;
   }

   private String selectProcessing(String[] parts) {
      int partsLength = parts.length;
      String sqlString = "";
      sqlString = sqlString + "SELECT ";
      if (!parts[1].isBlank() && !parts[1].isEmpty()) {
         sqlString = sqlString + parts[1] + " FROM ";
      } else {
         sqlString = sqlString + "* FROM ";
      }

      this.setTableName(parts[2]);
      sqlString = sqlString + "[" + parts[2] + "] ";
      if (partsLength > 3) {
         String clause = parts[3];
         sqlString = sqlString + "WHERE " + clause;
      }

      return sqlString;
   }

   private String insertProcessing(String[] parts) {
      String sqlString = String.format("INSERT INTO [%s] (", parts[1]);
      this.setTableName(parts[1]);
      String[] dataPart = parts[2].split(",");
      String colunmNameString = "";

      for(int i = 0; i < dataPart.length; ++i) {
         if (i == dataPart.length - 1) {
            colunmNameString = colunmNameString + String.format("[%s])", dataPart[i].split("=")[0]);
         } else {
            colunmNameString = colunmNameString + String.format("[%s],", dataPart[i].split("=")[0]);
         }
      }

      sqlString = sqlString + colunmNameString + " VALUES (";
      String columnDataString = "";

      for(int i = 0; i < dataPart.length; ++i) {
         if (i == dataPart.length - 1) {
            columnDataString = columnDataString + String.format("%s);", dataPart[i].split("=")[1]);
         } else {
            columnDataString = columnDataString + String.format("%s,", dataPart[i].split("=")[1]);
         }
      }

      sqlString = sqlString + columnDataString;
      return sqlString;
   }

   public String queryStringToSQL(String request) {
      if (!request.isBlank() && !request.isEmpty()) {
         String sqlString = "";

         try {
            String[] parts = request.split("/");
            if (parts.length < 3) {
               throw new AFCommonExceptions("Invalid query string : " + request);
            }

            String var4 = parts[0].toUpperCase();
            byte var5 = -1;
            switch(var4.hashCode()) {
            case 73:
               if (var4.equals("I")) {
                  var5 = 1;
               }
               break;
            case 83:
               if (var4.equals("S")) {
                  var5 = 0;
               }
            }

            switch(var5) {
            case 0:
               sqlString = this.selectProcessing(parts);
               break;
            case 1:
               sqlString = this.insertProcessing(parts);
               break;
            default:
               throw new AFCommonExceptions("Invalid operation " + parts[0] + ", Must be one of " + String.valueOf(this.QUERY_TYPES));
            }
         } catch (ArrayIndexOutOfBoundsException var6) {
            throw new AFCommonExceptions("Invalid query string : " + request);
         }

         PrintStream var10000 = System.out;
         String var10001 = String.valueOf(this.getClass());
         var10000.println(var10001 + " : " + sqlString);
         return sqlString;
      } else {
         throw new AFCommonExceptions("Empty query string");
      }
   }

   public static void main(String[] args) {
      Translate translate = new Translate();
      System.out.println(translate.queryStringToSQL("S//USER/USER_ID='john.doe1'"));
      System.out.println(translate.queryStringToSQL("I/CETL_WORKER/WORKER_ID='SAMPLE',WORK_NAME='WORKER ODA PER,SESSION_ID='CSLDJN'"));
   }
}