/* Decompiler 26ms, total 587ms, lines 78 */
package com.spine.common.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LogFileZipper {
   public static synchronized void zipFile(File file) {
      String zipFileName = file.getAbsolutePath().replace(".log", ".zip");

      try {
         FileInputStream fis = new FileInputStream(file);

         try {
            FileOutputStream fos = new FileOutputStream(zipFileName);

            try {
               ZipOutputStream zos = new ZipOutputStream(fos);

               try {
                  ZipEntry zipEntry = new ZipEntry(file.getName());
                  zos.putNextEntry(zipEntry);
                  byte[] buffer = new byte[1024];

                  while(true) {
                     int length;
                     if ((length = fis.read(buffer)) < 0) {
                        zos.closeEntry();
                        file.delete();
                        System.out.println("file " + file.getName() + " zipped");
                        break;
                     }

                     zos.write(buffer, 0, length);
                  }
               } catch (Throwable var11) {
                  try {
                     zos.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }

                  throw var11;
               }

               zos.close();
            } catch (Throwable var12) {
               try {
                  fos.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }

               throw var12;
            }

            fos.close();
         } catch (Throwable var13) {
            try {
               fis.close();
            } catch (Throwable var8) {
               var13.addSuppressed(var8);
            }

            throw var13;
         }

         fis.close();
      } catch (IOException var14) {
         var14.printStackTrace();
      }

   }
}