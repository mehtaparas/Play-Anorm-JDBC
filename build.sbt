name := "play-scala-anorm-example"

version := "2.6.0-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += evolutions

libraryDependencies += "com.h2database" % "h2" % "1.4.196"

libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.3"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "org.apache.phoenix" % "phoenix" % "4.11.0-HBase-1.3" pomOnly()
libraryDependencies += "org.apache.phoenix" % "phoenix-core" % "4.11.0-HBase-1.3"
libraryDependencies += "org.apache.zookeeper" % "zookeeper" % "3.4.10" pomOnly()
libraryDependencies += "org.apache.hbase" % "hbase" % "1.3.1" pomOnly()
libraryDependencies += "org.apache.hbase" % "hbase-client" % "1.3.1"
libraryDependencies += "org.apache.hbase" % "hbase-common" % "1.3.1"
libraryDependencies += "org.apache.hbase" % "hbase-protocol" % "1.3.1"
libraryDependencies += "org.apache.hbase" % "hbase-server" % "1.3.1"
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.7.3"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.3" % "provided"

