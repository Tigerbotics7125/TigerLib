# TigerLib

[![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/Tigerbotics7125/TigerLib?color=success&label=Latest&style=plastic)](https://github.com/Tigerbotics7125/TigerLib/releases)
[![GitHub tag (latest SemVer pre-release)](https://img.shields.io/github/v/tag/Tigerbotics7125/TigerLib?color=important&include_prereleases&label=Pre-release&style=plastic)](https://github.com/Tigerbotics7125/TigerLib/releases)

[![Lines of code](https://img.shields.io/tokei/lines/github/tigerbotics7125/TigerLib?style=plastic)](#)

## Tigerbotic's general library for FRC Robots.


[![GitHub issues](https://img.shields.io/github/issues/Tigerbotics7125/TigerLib?style=plastic)](https://github.com/Tigerbotics7125/TigerLib/issues?q=is%3Aissue+is%3Aopen)

[![GitHub closed issues](https://img.shields.io/github/issues-closed-raw/Tigerbotics7125/TigerLib?style=plastic)](https://github.com/Tigerbotics7125/TigerLib/issues?q=is%3Aissue+is%3Aclosed)

[![GitHub pull requests](https://img.shields.io/github/issues-pr/Tigerbotics7125/TigerLib?style=plastic)](https://github.com/Tigerbotics7125/TigerLib/pulls?q=is%3Apr+is%3Aopen)

[![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed-raw/Tigerbotics7125/TigerLib?style=plastic)](https://github.com/Tigerbotics7125/TigerLib/pulls?q=is%3Apr+is%3Aclosed)

## Installation:

### Optionally: clone the pre-made repo [here](https://github.com/Tigerbotics7125/TigerLibTemplate/).



1. Initialize a git repo in your project by running `git init` in the root of your FRC project.
1. Add this project as a git submodule with `git submodule add https://github.com/tigerbotics7125/tigerlib`
1. Add the following snippet to `settings.gradle`:
    ```groovy
    include(":TigerLib")

    rootProject.children.each {
        setUpChildProject(it)
    }

    private void setUpChildProject(ProjectDescriptor project) {
        /*
        * Instead of every file being named build.gradle.kts we instead use the name ${project.name}.gradle.kts.
        * This is much nicer for searching for the file in your IDE.
        */
        final String groovyName = "${project.name}.gradle"
        final String kotlinName = "${project.name}.gradle.kts"
        project.buildFileName = groovyName
        if (!project.buildFile.isFile()) {
            project.buildFileName = kotlinName
        }
        assert project.buildFile.isFile(): "File named $groovyName or $kotlinName must exist."
        project.children.each { setUpChildProject(it) }
    }
    ```
1. Add the following snippet to the `dependencies {}` block in `build.gradle`:
    ```groovy
    dependencies {

        /* other dependencies */

        implementation project(":TigerLib")
    }
    ```
1. Run `./gradlew build` to build the project, and you should be up and running.

### Contributors & Acknowledgments
* Spectrum 3847 - base input classes

[![Contributors](https://contrib.rocks/image?repo=Tigerbotics7125/TigerLib)](https://github.com/Tigerbotics7125/TigerLib/graphs/contributors)
