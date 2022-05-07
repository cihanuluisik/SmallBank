#!/usr/bin/env bash
./gradlew clean build
docker build --build-arg JAR_FILE=build/libs/\*.jar -t baraka/cihan-test-docker .