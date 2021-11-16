# github-issue-copyer

This project copies the issues and comments from one github repository to another. This is required as long as Github itself does not support this. Since copied issues may only be created as your user, all issues and comments are created by the user that is authenticated and a note in the beginning of the issue with the original creator will be added.

To do this, execute the following:
- Call `./download.sh $OWNER $REPOSITORY` to download the issues (e.g. if the issue of this repo shall be downloaded, call `./download.sh DaGeRe github-issue-copyer`
- Switch to issue-importer and build it by `cd issue-importer && mvn clean package` (requires maven installed)
- Call `java -jar target/issue-importer-1.0-SNAPSHOT.jar --issueFile ../issues.json --commentFile ../comments.json --repoLocation $REPO --authentication $AUTH`, with
  - $REPO being your repository, e.g. DaGeRe/github-issue-copyer and
  - $AUTH beeing your authentication by token, e.g. DaGeRe:**** (the token needs to be created by https://github.com/settings/tokens)
  
This currently does not copy labels.
