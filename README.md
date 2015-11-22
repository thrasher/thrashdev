ThrashDev - a collection of online dev tools
=============================

Created favicon.ico with various images
brew update
brew install imagemagick
convert thrashdev-16x16.png thrashdev-24x24.png thrashdev-32x32.png thrashdev-48x48.png thrashdev-64x64.png favicon.ico

To force JDK 1.7 for GAE, before uploading:
export JAVA_HOME=`/usr/libexec/java_home -v 1.7`

In Eclipse, you must install lombok to see the annotated auto-created class methods/members with:
java -jar ~/.m2/repository/org/projectlombok/lombok/1.16.4/lombok-1.16.4.jar

AppEngine credentials for Google are stored in:
~/.appcfg_oauth2_tokens_java 

To run local development test:
mvn appengine:devserver

To upload new code to GAE:
mvn appengine:update

To see other commands:
mvn help:describe -Dplugin=appengine

