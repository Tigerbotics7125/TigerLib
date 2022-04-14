# TigerLib

Tigerbotic's general library for FRC Robots.

---

When developing on this project, the default `build.gradle` cannot be used, as its designed to work when it's being used as a submodule.
You will have to use the `devBuild.gradle` by using the `--build-file` tag.

This also causes a fake error on `build.gradle` that there is no version on the GradleRIO plugin.

