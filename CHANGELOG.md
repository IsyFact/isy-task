# 5.0.0
### FEATURES
- `IFS-4713`: Dokumentation mit Stand aus `isyfact-standards` zusammengeführt und technische Schulden behoben.
- `IFS-4714`: Zentrale Versionierung eingeführt.
- `IFS-4576`: Portierung fehlender Tickets aus isy-standards
- `IFS-4526`: Logeintrag IsyTaskAspect korrigiert
- `IFS-4495`: Verwendung der Defaults, falls keine Task-Config definiert ist
- `IFS-4583`: Wiedereinführung der Quality-Gates

### BREAKING CHANGES
- `IFS-4922`: Aktualisierung von Java 17 auf 25

### DEPENDENCY UPGRADES
- Update com.github.spotbugs:spotbugs-maven-plugin von Version 4.9.8.1 auf 4.9.8.2
- Update spring.boot.version von Version 3.5.6 auf 3.5.9
- Update org.apache.maven.plugins:maven-enforcer-plugin von Version 3.6.0 auf 3.6.2
- Update org.codehaus.mojo:flatten-maven-plugin von Version 1.7.1 auf 1.7.3
- Update org.apache.maven.plugins:maven-source-plugin von Version 3.2.1 auf 3.4.0
- Update IsyFact/isy-github-actions-templates/.github/workflows/maven_build_template.yml von Version 1.7.0 auf 1.8.0
- Update IsyFact/isy-github-actions-templates/.github/workflows/docs_build_template.yml von Version 1.7.0 auf 1.8.0
- Update IsyFact/isy-github-actions-templates/.github/workflows/commit_message_checker_template.yml von Version 1.7.0 auf 1.8.0
- Update IsyFact/isy-github-actions-templates/.github/workflows/maven_create_release_template.yml von Version 1.7.0 auf 1.8.0
- `IFS-4531`: Update von Flatten Maven Plugin auf Version 1.7.1
    * Hinzufügen von Maven Enforcer Plugin auf Version 3.6.0
    * Setzen der Maven Version auf 3.6.3
- `IFS-4655`: Update von Maven Checkstyle Plugin auf Version 3.6.0
- `IFS-4580`: Spring-Boot Update auf Version 3.4.5
- `IFS-4864`: Spring-Boot Update auf Version 3.5.6
