#!/bin/sh
APP_HOME=$(cd "$(dirname "$0")" && pwd -P)
if [ -n "$JAVA_HOME" ]; then JAVA="$JAVA_HOME/bin/java"; else JAVA="java"; fi
exec "$JAVA" -Xmx512m -Dfile.encoding=UTF-8 \
    -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
    org.gradle.wrapper.GradleWrapperMain "$@"
