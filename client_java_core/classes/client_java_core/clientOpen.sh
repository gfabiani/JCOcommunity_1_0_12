#!/bin/bash
#set -x
VAR=`pwd`
sudo java -cp "$VAR/core/external_jar/gestPassword.jar:$VAR/../" client_java_core.core.ClientTCPThread

