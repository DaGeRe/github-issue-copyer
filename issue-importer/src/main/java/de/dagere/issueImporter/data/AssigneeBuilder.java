package de.dagere.issueImporter.data;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class AssigneeBuilder {
   private static final String ASSIGNEE_START_STRING = ", \"assignees\":[";
   private static final List<String> authorizedAssignees = Arrays.asList(new String[] { "DaGeRe" });

   public static String getAssigneeString(final JSONObject issue) {
      JSONArray assignees = issue.getJSONArray("assignees");
      String assigneesString;
      if (assignees.length() > 0) {
         assigneesString = buildString(assignees);
         System.out.println("Assignees: " + assigneesString + " " + assigneesString.equals(ASSIGNEE_START_STRING));
         if (assigneesString.equals(ASSIGNEE_START_STRING)) {
            assigneesString = "";
         } else {
            assigneesString = assigneesString.substring(0, assigneesString.length() - 1) + "]";
         }
      } else {
         assigneesString = "";
      }
      return assigneesString;
   }

   private static String buildString(final JSONArray assignees) {
      String assigneesString;
      assigneesString = ASSIGNEE_START_STRING;
      for (int assignee = 0; assignee < assignees.length(); assignee++) {
         String assigneeName = assignees.getJSONObject(assignee).getString("login");
         boolean allowedContained = checkAllowsContained(assigneeName);
         if (allowedContained) {
            assigneesString += "\"" + assigneeName + "\",";
         }
      }
      return assigneesString;
   }

   private static boolean checkAllowsContained(final String assigneeName) {
      boolean allowedContained = false;
      for (String authorized : authorizedAssignees) {
         if (assigneeName.equals(authorized)) {
            allowedContained = true;
         }
      }
      return allowedContained;
   }
}
