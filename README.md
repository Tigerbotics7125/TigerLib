<!-- Jitpack, Build workflow, Test workflow -->
[![](https://jitpack.io/v/Tigerbotics7125/tigerlib.svg)](https://jitpack.io/#Tigerbotics7125/tigerlib) [![Build TigerLib](https://github.com/Tigerbotics7125/TigerLib/actions/workflows/Build.yaml/badge.svg?branch=main)](https://github.com/Tigerbotics7125/TigerLib/actions/workflows/Build.yaml) [![CI/CD](https://github.com/Tigerbotics7125/TigerLib/actions/workflows/CI-CD.yaml/badge.svg?branch=main)](https://github.com/Tigerbotics7125/TigerLib/actions/workflows/CI-CD.yaml)

---

# What is TigerLib?
TigerLib is a FRC library which holds many different utilities for programming a FRC robot. These utilities include many tools such as OI tools, and Math functions.

# Installation

1. Add the JitPack repository to your `build.gradle` file.
``` groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

1. Add the dependency to your `build.gradle` file.
> [![](https://jitpack.io/v/Tigerbotics7125/tigerlib.svg)](https://jitpack.io/#Tigerbotics7125/tigerlib)
```groovy
dependencies {
    // check tag above for most recent version
    implementation 'com.github.Tigerbotics7125:tigerlib:VERSION'

    // ... other dependencies
}
```

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
