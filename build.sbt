name := "MHW-Future-Manager"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "11-R16",
  "net.sf.opencsv" % "opencsv" % "2.3",
  "org.scalafx" %% "scalafxml-core-sfx8" % "0.4"
)
mainClass in Compile := Some("rose.orphnoch.mhw.futuremanager.FutureManager")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
