package de.dagere.issueImporter.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Util {
   public static String getProcessResult(final Process process) throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      StringBuilder builder = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
         builder.append(line);
         builder.append(System.getProperty("line.separator"));
      }
      String result = builder.toString();
      return result;
   }

   public static String cleanBody(final String original) {
      return original.replaceAll("\\r\\n", "\\\\n")
            .replaceAll("\"", "'")
            .replaceAll("\t", "   ");
   }
}
