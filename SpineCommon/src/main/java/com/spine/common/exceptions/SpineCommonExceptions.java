/* Decompiler 4ms, total 486ms, lines 14 */
package com.spine.common.exceptions;

public class SpineCommonExceptions extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public SpineCommonExceptions(String message) {
      super(message);
   }

   public SpineCommonExceptions(String message, Throwable cause) {
      super(message, cause);
   }
}