
```
docker run -it ubuntu /bin/bash

apt update

apt install -y wget openjdk-8-jdk vim unzip git
```

## Install Gradle
```
wget https://services.gradle.org/distributions/gradle-5.4.1-bin.zip

mkdir /opt/gradle
cp gradle-5.4.1-bin.zip /opt/gradle
cd /opt/gradle
unzip gradle-5.4.1-bin.zip

# vim ~/.profile
export GRADLE_HOME=/opt/gradle/gradle-5.4
export PATH=${GRADLE_HOME}/bin:${PATH}
```

## Download Cord Sample

git clone https://github.com/corda/samples
cd cordapp-example/
./gradlew deployNodes
