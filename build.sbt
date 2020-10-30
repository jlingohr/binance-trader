name := "binance-trader"

version := "0.1"

scalaVersion := "2.13.3"

val zioVersion = "1.0.3"
val newtypeVersion = "0.4.3"
val akkaVersion = "2.6.8"
val akkaHttpVersion = "10.2.1"
val http4sVersion = "0.21.8"
val http4sJdkVersion = "0.3.1"
val fs2Version = "2.1.0"
val catsVersion = "2.1.1"
val enumeratum = "1.5.15"
val enumeratumCirce = "1.5.22"
val circeVersion = "0.12.3"
val circeFs2Version = "0.12.0"
val mockitoVersion = "1.10.4"
val scalatestVersion = "3.1.0"
val refinedVersion = "0.9.17"

libraryDependencies ++= Seq(
  compilerPlugin(
    "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full
  ),

  // Zio
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-streams" % zioVersion,

  // newtype
  "io.estatico" %% "newtype" % newtypeVersion,
  "eu.timepit" %% "refined" % refinedVersion,

  // http4s client
  "org.http4s" %% "http4s-circe"           % http4sVersion,
  "org.http4s" %% "http4s-jdk-http-client" % http4sJdkVersion,

  // fs2
  "co.fs2" %% "fs2-core"             % fs2Version,
  "co.fs2" %% "fs2-io"               % fs2Version,
  "co.fs2" %% "fs2-reactive-streams" % fs2Version,
  "co.fs2" %% "fs2-experimental"     % fs2Version,

  // cats
  "org.typelevel"              %% "cats-core"      % catsVersion,
  "org.typelevel"              %% "cats-effect"    % catsVersion,

  // enums
  "com.beachape" %% "enumeratum"       % enumeratum,
  "com.beachape" %% "enumeratum-circe" % enumeratumCirce,

  // circe
  "io.circe" %% "circe-core"           % circeVersion,
  "io.circe" %% "circe-generic"        % circeVersion,
//  "io.circe" %% "circe-generic-extras" % circeVersion,
  "io.circe" %% "circe-parser"         % circeVersion,
  "io.circe" %% "circe-shapes"         % circeVersion,
  "io.circe" %% "circe-fs2"            % circeFs2Version,
  "eu.timepit" %% "refined"            % refinedVersion,
  "eu.timepit" %% "refined-cats"       % refinedVersion,

  // testing
  "org.scalactic" %% "scalactic"               % scalatestVersion,
  "org.scalatest" %% "scalatest"               % scalatestVersion % Test,
  "org.mockito"   %% "mockito-scala"           % mockitoVersion % Test,
  "org.mockito"   %% "mockito-scala-cats"      % mockitoVersion % Test,
  "org.mockito"   %% "mockito-scala-scalatest" % mockitoVersion % Test
)

scalacOptions += "-Ymacro-annotations"

