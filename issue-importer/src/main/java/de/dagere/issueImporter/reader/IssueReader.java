package de.dagere.issueImporter.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class IssueReader {
   public static TreeMap<Integer, JSONObject> getIssues(final File issuesFile) throws IOException, FileNotFoundException {
      String issueJsonTxt = IOUtils.toString(new FileReader(issuesFile));
      JSONArray issueJsonArray = new JSONArray(issueJsonTxt);

      TreeMap<Integer, JSONObject> issues = new TreeMap<>();

      for (int i = 0; i < issueJsonArray.length(); i++) {
         JSONObject issue = issueJsonArray.getJSONObject(i);
         int oldIssueIndex = issue.getInt("number");
         issues.put(oldIssueIndex, issue);
      }
      return issues;
   }
}
