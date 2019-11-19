FROM maven:3-jdk-8-alpine AS MAVEN_BUILD

ARG LOCAL_MAVEN_MIRROR=http://maven.aliyun.com/nexus/content/groups/public/

# used to edit maven settings.xml
RUN apk add --no-cache xmlstarlet

# change default local repository location. parent image set ~/.m2 as volume, so data won't be persisted for following build cmds
RUN xmlstarlet ed --inplace -N 's=http://maven.apache.org/SETTINGS/1.0.0' \
	--subnode '/s:settings' --type elem -n localRepository -v '${user.home}/m2/repository' \
	/usr/share/maven/conf/settings.xml

RUN if test -n "$LOCAL_MAVEN_MIRROR"; then \
	xmlstarlet ed --inplace -N 's=http://maven.apache.org/SETTINGS/1.0.0' \
		--subnode '/s:settings/s:mirrors' --type elem -n mirror -v '' \
		/usr/share/maven/conf/settings.xml \
	&& xmlstarlet ed --inplace -N 's=http://maven.apache.org/SETTINGS/1.0.0' \
		--subnode '/s:settings/s:mirrors/s:mirror' --type elem -n id -v 'custom-mirror' \
		/usr/share/maven/conf/settings.xml \
	&& xmlstarlet ed --inplace -N 's=http://maven.apache.org/SETTINGS/1.0.0' \
		--subnode '/s:settings/s:mirrors/s:mirror' --type elem -n name -v 'custom-mirror' \
		/usr/share/maven/conf/settings.xml \
	&& xmlstarlet ed --inplace -N 's=http://maven.apache.org/SETTINGS/1.0.0' \
		--subnode '/s:settings/s:mirrors/s:mirror' --type elem -n url -v "$LOCAL_MAVEN_MIRROR" \
		/usr/share/maven/conf/settings.xml \
	&& xmlstarlet ed --inplace -N 's=http://maven.apache.org/SETTINGS/1.0.0' \
		--subnode '/s:settings/s:mirrors/s:mirror' --type elem -n mirrorOf -v 'central' \
		/usr/share/maven/conf/settings.xml \
	;fi

COPY pom.xml /build/

COPY src /build/src/

WORKDIR /build/

RUN mvn package

# ------------------------- 8< -------------------------

FROM openjdk:8-jre-alpine

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/online_music-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "online_music-0.0.1-SNAPSHOT.jar"]

#暴露端口8000，到时候执行docker run 的时候才好把宿主机端口映射到8000
EXPOSE 8081