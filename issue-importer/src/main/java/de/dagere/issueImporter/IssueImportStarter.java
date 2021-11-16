package de.dagere.issueImporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import org.json.JSONObject;

import de.dagere.issueImporter.data.CommentData;
import de.dagere.issueImporter.data.Importer;
import de.dagere.issueImporter.reader.CommentReader;
import de.dagere.issueImporter.reader.IssueReader;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class IssueImportStarter implements Callable<Void>{
   
   @Option(names = { "-issueFile", "--issueFile" }, description = "File that contains the issues for import", required = true)
   private File issueFile;
   
   @Option(names = { "-commentFile", "--commentFile" }, description = "File that contains the comments for the import", required = true)
   private File commentFile;
   
   @Option(names = { "-authentication", "--authentication" }, description = "Authentication in the form user:token (obtained from https://github.com/settings/tokens)", required = true)
   private String authentication;
   
   @Option(names = { "-repoLocation", "--repoLocation" }, description = "Location of the repository (in the form username/reponame or organizationname/reponame)", required = true)
   private String repoLocation;
   
   private int timeout = 15000;
   
   public static void main(final String[] args) throws FileNotFoundException, IOException, InterruptedException {
      final CommandLine commandLine = new CommandLine(new IssueImportStarter());
      commandLine.execute(args);
   }

   @Override
   public Void call() throws Exception {
      Map<Integer, List<CommentData>> comments = CommentReader.getComments(commentFile);
      TreeMap<Integer, JSONObject> issues = IssueReader.getIssues(issueFile);

      for (Map.Entry<Integer, JSONObject> entry : issues.entrySet()) {
         new Importer(authentication, repoLocation).handleIssue(comments, entry.getValue(), entry.getKey());
         Thread.sleep(timeout);
      }
      return null;
   }
}
