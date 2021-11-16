if [ -z $1 ]
then
	"Please call ./download.sh $owner $repository"
	exit 1
fi

curl https://api.github.com/repos/$owner/$repository/issues?state=all\&per_page=100 > issues.json
curl https://api.github.com/repos/$owner/$repository/issues/comments?per_page=100 > comments.json
