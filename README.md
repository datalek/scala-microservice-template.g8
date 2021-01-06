# Scala Microservice Template

## ???

1. Read configurations from file or a URI
    * `"com.typesafe" % "config" % "1.4.0"`
2. Instantiate all services that are needed to provide features.
    * It's up to you, the example uses http4s (put here the version)
   
```
sbt new git@github.com:datalek/scala-microservice-template.g8.git --branch feature/templating
```

# How to test locally

```
sbt new file://scala-microservice-template.g8/ --name=microservice-test --force
```

Template license
----------------
Written in 2019 by Rakhovlad ferlosalem@gmail.com

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.

[g8]: http://www.foundweekends.org/giter8/
