/* Decompiler 4ms, total 486ms, lines 14 */
package com.af.foundation.AFCommon.exceptions;

public class AFCommonExceptions extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public AFCommonExceptions(String message) {
      super(message);
   }

   public AFCommonExceptions(String message, Throwable cause) {
      super(message, cause);
   }
}