/** Name of project */
name := "Swatch"

/** Organization */
organization := "com.github.sguzman"

/** Project Version */
version := "1.0"

/** Scala version */
scalaVersion := "2.12.4"

/** Scalac parameters */
scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

/** Javac parameters */
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

/** Resolver */
resolvers ++= Seq(
  DefaultMavenRepository,
  Resolver.sonatypeRepo("public"),
  Resolver.typesafeRepo("releases"),
  Resolver.sbtPluginRepo("releases"),
  Resolver.jcenterRepo,
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
)

/** Source Dependencies */
libraryDependencies ++= Seq(
  "org.scalaj" % "scalaj-http_2.12" % "2.3.0",
  "net.ruippeixotog" % "scala-scraper_2.12" % "2.0.0",
  "org.feijoas" % "mango_2.12" % "0.14"
)

/** Make sure to fork on run */
fork in run := true


herokuFatJar in Compile := Some((assemblyOutputPath in assembly).value)