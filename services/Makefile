.PHONY: common db iam professors faculties students all

common:
	cd ./common; \
	mvn clean install

db: common
	cd ./wc.database; \
	mvn clean package -Dquarkus.package.type=fast-jar

iam: common
	cd ./wc.iam; \
	mvn clean package -Dquarkus.package.type=fast-jar

professors: common
	cd ./wc.professors; \
	mvn clean package -Dquarkus.package.type=fast-jar -U

faculties: common
	cd ./wc.faculties; \
	mvn clean package -Dquarkus.package.type=fast-jar

students: common
	cd ./wc.students; \
	mvn clean package -DskipTests -Dquarkus.package.type=fast-jar

all: common db iam professors faculties students

