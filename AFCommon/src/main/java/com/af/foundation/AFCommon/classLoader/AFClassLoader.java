package com.af.foundation.AFCommon.classLoader;

import com.af.foundation.AFCommon.configuration.AFConfiguration;
import com.af.foundation.AFCommon.exceptions.AFCommonExceptions;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class AFClassLoader {
   private final String JARS_AT;
   File[] jarFiles;

   public AFClassLoader() {
      this.JARS_AT = AFConfiguration.CUSTOM_LIB;
      System.out.println("Loading classes" + this.JARS_AT);
      File folder = new File(this.JARS_AT);
      this.jarFiles = folder.listFiles((dir, name) -> {
         return name.endsWith(".jar");
      });
   }

   public URLClassLoader loadClass() throws AFCommonExceptions {
      if (this.jarFiles.length == 0) {
         throw new AFCommonExceptions("No jars to load");
      } else {
         System.out.println("loading all class ...");
         URL[] urls = new URL[this.jarFiles.length];

         for(int i = 0; i < urls.length; ++i) {
            try {
               System.out.println(this.jarFiles[i]);
               urls[i] = this.jarFiles[i].toURI().toURL();
            } catch (Exception var4) {
               throw new AFCommonExceptions("Error while loading the jars");
            }
         }

         return new URLClassLoader(urls);
      }
   }
}