name := "play-scala-anorm-example"

version := "2.6.0-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

unmanagedResourceDirectories in Compile += baseDirectory.value / "conf/resources"

resolvers ++= Seq( "maven.org" at "http://repo2.maven.org/maven2",
"conjars.org" at "http://conjars.org/repo",
"Hortonworks Releases" at "http://repo.hortonworks.com/content/groups/public")

dependencyOverrides += "com.google.guava" % "guava" % "16.0"

libraryDependencies += guice
libraryDependencies += jdbc

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "org.apache.phoenix" % "phoenix-core" % "4.7.0.2.6.1.0-129"  
libraryDependencies += "org.apache.zookeeper" % "zookeeper" % "3.4.6.2.6.1.0-129"
libraryDependencies += "org.apache.hbase" % "hbase-client" % "1.1.2.2.6.1.0-129"
libraryDependencies += "org.apache.hbase" % "hbase-common" % "1.1.2.2.6.1.0-129"
libraryDependencies += "org.apache.hbase" % "hbase-protocol" % "1.1.2.2.6.1.0-129"
libraryDependencies += "org.apache.hbase" % "hbase-server" % "1.1.2.2.6.1.0-129"
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.7.3.2.6.1.0-129"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.3.2.6.1.0-129"
libraryDependencies += "commons-collections" % "commons-collections" % "3.2.1"
libraryDependencies += "org.apache.hadoop" % "hadoop-auth" % "2.7.3.2.6.1.0-129"
libraryDependencies += "commons-configuration" % "commons-configuration" % "1.6"
libraryDependencies += "org.apache.htrace" % "htrace-core" % "3.1.0-incubating"
libraryDependencies += "org.apache.twill" % "twill-zookeeper" % "0.4.0-incubating"
libraryDependencies += "co.cask.tephra" % "tephra-hbase-compat-1.1" % "0.7.1"
