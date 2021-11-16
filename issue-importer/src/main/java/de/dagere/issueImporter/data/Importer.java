package de.dagere.issueImporter.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class Importer {
   private final String authentication;
   private final String repoLocation;

   public Importer(final String authentication, final String repoLocation) {
      this.authentication = authentication;
      this.repoLocation = repoLocation;
   }

   public void handleIssue(final Map<Integer, List<CommentData>> comments, final JSONObject issue, final int i) throws IOException {
      
      IssueCreator creator = new IssueCreator(authentication, repoLocation);
      final int createdNumber = creator.createIssue(i, issue);

      setState(issue, createdNumber);

      addComments(comments, createdNumber);
   }

   private void addComments(final Map<Integer, List<CommentData>> comments, final int createdIssueNumber) throws IOException {
      int commentIndex = 0;
      if (comments.containsKey(createdIssueNumber)) {
         for (CommentData issueComments : comments.get(createdIssueNumber)) {
            System.out.println("Adding comment: " + commentIndex);
            String commentPostString = "{\"body\": \"" + issueComments.getBody() + "\"}";
            File localCommentFile = new File("issue_" + createdIssueNumber + "_comment_" + commentIndex++ + ".json");
            FileUtils.writeStringToFile(localCommentFile, commentPostString, StandardCharsets.UTF_8);

            Process commentProcess = Runtime.getRuntime().exec("curl -X POST "
                  + "-u " + authentication + " "
                  + "-H \"Accept: application/vnd.github.v3+json\" "
                  + "https://api.github.com/repos/" + repoLocation + "/issues/" + createdIssueNumber + "/comments "
                  + "-d @" + localCommentFile.getName());
            String commentResultString = Util.getProcessResult(commentProcess);
            System.out.println(commentResultString);
         }
      }
   }

   private void setState(final JSONObject issue, final int createdNumber) throws IOException {
      String state = issue.getString("state");
      System.out.println(state);
      if (state.equals("closed")) {
         String closeString = "{\"state\": \"closed\"}";
         String url = "https://api.github.com/repos/" + repoLocation + "/issues/" + createdNumber;
         ProcessBuilder builder = new ProcessBuilder("curl",
               "-X", "POST",
               "-u", authentication,
               "-H", "Accept: application/vnd.github.v3+json",
               url,
               "-d", closeString);
         Process stateProcess = builder.start();
         System.out.println("Sending " + closeString + " to " + url);
         String stateResultString = Util.getProcessResult(stateProcess);
         System.out.println(stateResultString);
      }
   }
}
