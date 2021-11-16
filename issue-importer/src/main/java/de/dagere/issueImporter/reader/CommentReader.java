package de.dagere.issueImporter.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import de.dagere.issueImporter.data.CommentData;

public class CommentReader {
   public static Map<Integer, List<CommentData>> getComments(final File commentFile) throws IOException, FileNotFoundException {
      String commentJsonTxt = IOUtils.toString(new FileReader(commentFile));
      JSONArray commentJsonArray = new JSONArray(commentJsonTxt);

      Map<Integer, List<CommentData>> comments = new HashMap<>();

      for (int i = 0; i < commentJsonArray.length(); i++) {
         JSONObject comment = commentJsonArray.getJSONObject(i);

         String issueUrl = comment.getString("issue_url");
         String indexString = issueUrl.substring(issueUrl.lastIndexOf("/") + 1);
         int issueIndex = Integer.parseInt(indexString);

         JSONObject user = comment.getJSONObject("user");
         String userName = user.getString("login");

         String bodyString = comment.getString("body");
         bodyString = bodyString.replaceAll("\\r\\n", "\\\\n");

         String createdAt = comment.getString("created_at");
         CommentData commentData = new CommentData(userName, bodyString, createdAt);

         List<CommentData> commentList = comments.get(issueIndex);
         if (commentList == null) {
            commentList = new LinkedList<>();
            comments.put(issueIndex, commentList);
         }
         commentList.add(commentData);
      }
      return comments;
   }
}
