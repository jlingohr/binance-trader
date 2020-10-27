name := "binance-trader"

version := "0.1"

scalaVersion := "2.13.3"

val zioVersion = "1.0.3"
val newtypeVersion = "0.4.3"
val refinedVersion = "0.9.12"


libraryDependencies ++= Seq(
  // Zio
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-streams" % zioVersion,

  // newtype
  "io.estatico" %% "newtype" % newtypeVersion,
  "eu.timepit" %% "refined" % refinedVersion,
)