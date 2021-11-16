package de.dagere.issueImporter.data;

public class CommentData {
   private final String userName;
   private final String body;
   private final String createdAt;

   public CommentData(final String user, final String body, final String createdAt) {
      this.userName = user;
      this.body = body;
      this.createdAt = createdAt;
   }

   public String getBody() {
      return "> This comment has been imported from an old repository. It was originally created by @" + userName + " at " + createdAt + "\\n\\n" + body;
   }

}