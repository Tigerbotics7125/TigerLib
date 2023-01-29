[![Build](https://github.com/Tigerbotics7125/TigerLib/actions/workflows/Build.yaml/badge.svg)](https://github.com/Tigerbotics7125/TigerLib/actions/workflows/Build.yaml)
[![Lint](https://github.com/Tigerbotics7125/TigerLib/actions/workflows/Lint.yaml/badge.svg)](https://github.com/Tigerbotics7125/TigerLib/actions/workflows/Lint.yaml)
[![Publish Documentation](https://github.com/Tigerbotics7125/TigerLib/actions/workflows/Documentation.yaml/badge.svg)](https://github.com/Tigerbotics7125/TigerLib/actions/workflows/Documentation.yaml)

[![](https://jitpack.io/v/Tigerbotics7125/tigerlib.svg)](https://jitpack.io/#Tigerbotics7125/tigerlib)

---

# What is TigerLib?
TigerLib is a FRC library which holds many different utilities for programming a FRC robot. These utilities include many tools such as OI tools, and Math functions.

# Installation

This is assuming you have already created a wpilib / gradle project.

1. Open the `build.gradle` file.

2. Add or edit the following lines to use Java 17.
```groovy
sourceCompatibility = JavaVersion.VERSION_17
targetCompatibility = JavaVersion.VERSION_17
```

3. Add the following to add the Jitpack repository to your project.
``` groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

4. Add the dependency within the `dependencies` block.
> [![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/tigerbotics7125/tigerlib?color=00aaff&label=Latest%3A)](https://github.com/Tigerbotics7125/TigerLib/releases)
> [![GitHub release (latest SemVer including pre-releases)](https://img.shields.io/github/v/release/tigerbotics7125/tigerlib?color=ffff00&include_prereleases&label=pre-release%3A)](https://github.com/Tigerbotics7125/TigerLib/releases)
```groovy
dependencies {
    // check icons above for most recent version
    // do not include the 'v' in the version.
    implementation 'com.github.Tigerbotics7125:tigerlib:VERSION'
    // ex: implementation 'com.github.Tigerbotics7125:tigerlib:2023.0.0'

    // ... other dependencies
}
```

5. Execute a the gradle build task, either in your terminal with `./gradlew build` or with the VSCode extension (Ctrl/Cmd + Shift + P, then search for: `> Gradle: Run a Gradle Build`).

# Support

Code documentation and usage examples are available below:

* [Javadocs](https://tigerbotics7125.github.io/TigerLib/)
* [Wiki](https://github.com/Tigerbotics7125/TigerLib/wiki)

If you still need help, create an issue [here](https://github.com/Tigerbotics7125/TigerLib/issues/new).

## Contribution

1. [Fork](https://github.com/Tigerbotics7125/TigerLib/fork) this repo.
1. Make your changes.
1. [Start](https://github.com/Tigerbotics7125/TigerLib/compare) a pull request.

All changes need to be commited [atomicly](https://www.freshconsulting.com/insights/blog/atomic-commits/) and documented fully.


## Acknowledgments
![Lines of code](https://img.shields.io/tokei/lines/github/tigerbotics7125/TigerLib?style=plastic)
* ***In numerical order:***
* StuyPulse 694 - Conversion class.
* Spectrum 3847 - Some OI concepts.

[![Contributors](https://contrib.rocks/image?repo=Tigerbotics7125/TigerLib)](https://github.com/Tigerbotics7125/TigerLib/graphs/contributors)
