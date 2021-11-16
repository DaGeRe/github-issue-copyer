package de.dagere.issueImporter.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class IssueCreator {

   private final String authentication;
   private final String repoLocation;

   public IssueCreator(final String authentication, final String repoLocation) {
      this.authentication = authentication;
      this.repoLocation = repoLocation;
   }

   public int createIssue(final int i, final JSONObject issue) throws IOException {
      File issueFile = createIssueFile(i, issue);

      final int createdNumber = sendIssueREST(issueFile);
      return createdNumber;
   }

   private int sendIssueREST(final File issueFile) throws IOException {
      Process process = Runtime.getRuntime().exec("curl -X POST "
            + "-u " + authentication + " "
            + "-H \"Accept: application/vnd.github.v3+json\" "
            + "https://api.github.com/repos/" + repoLocation + "/issues "
            + "-d @" + issueFile.getName());

      String resultString = Util.getProcessResult(process);
      System.out.println(resultString);

      JSONObject result = new JSONObject(resultString);
      final int createdNumber = result.getInt("number");
      return createdNumber;
   }

   private File createIssueFile(final int i, final JSONObject issue) throws IOException {
      File issueFile = new File("issue_" + i + ".json");

      String title = Util.cleanBody(issue.getString("title"));
      System.out.println(title);

      JSONObject user = issue.getJSONObject("user");
      String userName = user.getString("login");

      String createdAt = issue.getString("created_at");

      System.out.println("User: " + userName);

      String assigneesString = AssigneeBuilder.getAssigneeString(issue);

      String bodyString = issue.getString("body");
      bodyString = Util.cleanBody(bodyString);

      int number = issue.getInt("number");

      String issueString = "{\"title\": \"Imported: " + title + "\"," +
            "\"body\": \"> This issue has been imported from an old repository. It was originally created by @" + userName + " at " + createdAt + " with number #" + number
            + "\\n\\n"
            + bodyString
            + "\""
            + assigneesString + "}";

      FileUtils.writeStringToFile(issueFile, issueString, StandardCharsets.UTF_8);
      return issueFile;
   }
}
