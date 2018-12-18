# DMBiometric
Easiest way to implement fingerprint authorization into project
Add it in your root build.gradle at the end of repositories:


  allprojects {
      repositories {
        ...
        maven { url 'https://jitpack.io' }
      }
    }
    
Add the dependency

  dependencies {
            implementation 'com.github.pmbfish40:DMBiometric:1.0.0'
    }
