plugins { id("io.vacco.oss.gitflow") version "0.9.8" }

group = "io.vacco.savitzky-golay"
version = "1.0.1"

configure<io.vacco.oss.gitflow.GsPluginProfileExtension> {
  addJ8Spec()
  addClasspathHell()
  sharedLibrary(true, false)
}

val api by configurations

dependencies {
  testImplementation("io.vacco.sabnock:sabnock:0.1.0")
  testImplementation("com.google.code.gson:gson:2.10.1")
}

