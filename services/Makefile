.PHONY: db iam professors faculties all

db:
	cd ./wc.database; \
	mvn clean package -Dquarkus.package.type=fast-jar

iam:
	cd ./wc.iam; \
	mvn clean package -Dquarkus.package.type=fast-jar

professors:
	cd ./wc.professors; \
	mvn clean package -DskipTests -Dquarkus.package.type=fast-jar

faculties:
	cd ./wc.faculties; \
	mvn clean package -Dquarkus.package.type=fast-jar

all: db iam professors faculties

