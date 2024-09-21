/* Decompiler 16ms, total 649ms, lines 37 */
package com.spine.communicator;


import com.spine.communicator.dbops.ExecuteSQL;
import com.spine.communicator.dbops.QueryOperation;
import com.spine.communicator.exceptions.SpineCommunicatorExceptions;
import com.spine.common.converters.Translate;


import org.json.JSONObject;

public class RequestHandeller {
   private static final ExecuteSQL executeSQL = new ExecuteSQL();

   public JSONObject process(QueryOperation queryOperation, JSONObject jSonDataRequest) {
      switch(queryOperation) {
      case EXECUTE_QUERY:
         String queryString = Translate.jsonToQueryString(jSonDataRequest);
         return executeSQL.executeQueryStringRequest(queryString);
      default:
         throw new SpineCommunicatorExceptions("Not a valid query operatin: " + String.valueOf(queryOperation), new IllegalArgumentException());
      }
   }

   public JSONObject process(QueryOperation queryOperation, String queryString) throws SpineCommunicatorExceptions {
      switch(queryOperation) {
      case EXECUTE_QUERY:
         return executeSQL.executeQueryStringRequest(queryString);
      default:
         throw new SpineCommunicatorExceptions("Not a valid query operatin: " + String.valueOf(queryOperation), new IllegalArgumentException());
      }
   }

   public static void main(String[] args) {
      String queString = "S//USER/user_id='john.doe1'";
      queString = "I/CETL_WORKER/WORKER_ID='SAMPLE2',WORK_NAME='Rendavathu WORKER ODA PER'";
      System.out.println((new RequestHandeller()).process(QueryOperation.EXECUTE_QUERY, queString));
   }
}