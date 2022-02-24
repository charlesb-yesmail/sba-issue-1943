#! /bin/bash
PRG="$0"
PRGDIR=`dirname "$PRG"`
JAVA_OPTS="-Xms1024m -Xmx2048m -Dcom.sun.management.jmxremote.port=12345 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
# Start up in Debug mode
JAVA_OPTS+=" -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5009"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
. ./set_env.sh
env | sort | grep "SPRING"

$JAVA_HOME/bin/java $JAVA_OPTS \
  -Dspring.boot.admin.client.username="$SPRING_SECURITY_USER_NAME" \
  -Dspring.boot.admin.client.password="$SPRING_SECURITY_USER_PASSWORD" \
  -Dspring.boot.admin.client.instance.metadata.user.name="$SPRING_SECURITY_USER_NAME" \
  -Dspring.boot.admin.client.instance.metadata.user.password="$SPRING_SECURITY_USER_PASSWORD" \
  -Dspring.boot.admin.username="$SB_ADMIN_USER" \
  -Dspring.boot.admin.password="$SB_ADMIN_PASSWORD" \
  -Dmanagement.endpoint.health.roles="ACTUATOR" \
  -jar "$PRGDIR"/target/*.jar
