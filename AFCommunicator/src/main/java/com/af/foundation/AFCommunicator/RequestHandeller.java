/* Decompiler 16ms, total 649ms, lines 37 */
package com.af.foundation.AFCommunicator;

import com.af.foundation.AFCommon.converters.Translate;
import com.af.foundation.AFCommunicator.dbops.ExecuteSQL;
import com.af.foundation.AFCommunicator.dbops.QueryOperation;
import com.af.foundation.AFCommunicator.exceptions.AFCommunicatorExceptions;
import org.json.JSONObject;

public class RequestHandeller {
   private static final ExecuteSQL executeSQL = new ExecuteSQL();

   public JSONObject process(QueryOperation queryOperation, JSONObject jSonDataRequest) {
      switch(queryOperation) {
      case EXECUTE_QUERY:
         String queryString = Translate.jsonToQueryString(jSonDataRequest);
         return executeSQL.executeQueryStringRequest(queryString);
      default:
         throw new AFCommunicatorExceptions("Not a valid query operatin: " + String.valueOf(queryOperation), new IllegalArgumentException());
      }
   }

   public JSONObject process(QueryOperation queryOperation, String queryString) throws AFCommunicatorExceptions {
      switch(queryOperation) {
      case EXECUTE_QUERY:
         return executeSQL.executeQueryStringRequest(queryString);
      default:
         throw new AFCommunicatorExceptions("Not a valid query operatin: " + String.valueOf(queryOperation), new IllegalArgumentException());
      }
   }

   public static void main(String[] args) {
      String queString = "S//USER/user_id='john.doe1'";
      queString = "I/CETL_WORKER/WORKER_ID='SAMPLE2',WORK_NAME='Rendavathu WORKER ODA PER'";
      System.out.println((new RequestHandeller()).process(QueryOperation.EXECUTE_QUERY, queString));
   }
}