# Written for a containerized version of PaperSpigot.
# OpenJDK JRE 8
FROM openjdk:8

# Specify the Java arguments -Xmx and -Xms
ENV JAVA_MIN_HEAP=512M
ENV JAVA_MAX_HEAP=512M

# Copy jar files
COPY target/SMPBot-jar-with-dependencies.jar /runtime/Bot.jar

# Change working directory so that bots's files are created there
WORKDIR /runtime/

# Run the downloaded jarfile with the previously specified arguments
CMD java -jar -Xmx${JAVA_MIN_HEAP} -Xms${JAVA_MAX_HEAP} SMPbot.jar