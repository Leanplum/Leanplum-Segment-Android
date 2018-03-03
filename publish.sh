#!/bin/sh
set -e

if [ ! -f ~/.bintray-login ]
then
    echo "save your bintray.com <userid>:<api-key> in ~/.bintray-login"
    exit 1
fi

groupid=${1}
package=${2}
version=${3}

bintray_login=$(cat ~/.bintray-login)
group_subdir=$(echo "$groupid" | tr . /)
reporoot=https://api.bintray.com/content/leanplum/maven/${package}/${version}/${group_subdir}/${version}

shift 3

if [ -z "$1" ]
then
    echo "no files given"
    exit 1
fi

for f in "$@"
do
  curl -T "$f" -u"${bintray_login}" "${reporoot}/$(basename "$f")"
  echo 
done
