## safe_app_java

**Maintainer:** Krishna Kumar (krishna.kuamr@maidsafe.net)

safe_app_java library for generating JAR and AAR packages.

### Prerequisites

safe_app_java is compatible with **Java 8**.

### Composite Build Setup

Repository is set up based on [Composite Build Setup](https://blog.gradle.org/introducing-composite-builds) of Gradle.

[api](/api) is a shared project for AAR and JAR builds. Both [safe-app-java](safe-app-java) and [safe-app-android](safe-app-android) include api project in their build dependency.

Refer the README of [safe-app-java](safe-app-java) and [safe-app-android](safe-app-android) for corresponding build instructions.

# License

Licensed under either of

* the MaidSafe.net Commercial License, version 1.0 or later ([LICENSE](LICENSE))
* the General Public License (GPL), version 3 ([COPYING](COPYING) or http://www.gnu.org/licenses/gpl-3.0.en.html)

at your option.

# Contribution

Unless you explicitly state otherwise, any contribution intentionally submitted for inclusion in the
work by you, as defined in the MaidSafe Contributor Agreement, version 1.1 ([CONTRIBUTOR]
(CONTRIBUTOR)), shall be dual licensed as above, and you agree to be bound by the terms of the
MaidSafe Contributor Agreement, version 1.1.
