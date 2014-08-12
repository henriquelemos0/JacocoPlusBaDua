#!/bin/bash

echo "Compiling Jacoco.Core"
cd /home/louiz91/workspace-jacoco/JacocoPlusBaDua/org.jacoco.core
mvn clean install -Dskip.tests #> /dev/null 2>&1 

echo "Compiling Jacoco.Agent.rt"
cd /home/louiz91/workspace-jacoco/JacocoPlusBaDua/org.jacoco.agent.rt
mvn clean install -Dskip.tests #> /dev/null 2>&1 

echo "Compiling Jacoco.Agent"
cd /home/louiz91/workspace-jacoco/JacocoPlusBaDua/org.jacoco.agent
mvn clean install -Dskip.tests #> /dev/null 2>&1 

echo "Extracting jacocoagent.jar"
cd /home/louiz91/workspace-jacoco/JacocoPlusBaDua/org.jacoco.agent/target
jar xf `find org.*.jar ! -iname "*javadoc*" ! -iname "*sources*"` jacocoagent.jar


