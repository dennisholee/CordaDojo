
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

```
cd ~/
git clone https://github.com/corda/samples.git 
cd samples/cordapp-example
./gradlew deployNodes
```

## Create image
```
docker commit {container_id} local/cordapp
```

## Run Docker
```
docker run -it --name cordapp -p 50005:50005 -p 50006:50006 -p 50007:50007 local/corda /bin/bash
```

Start example
```
cd ~/
cd samples/cordapp-example
workflows-kotlin/build/nodes/runnodes

# Run node A
./gradlew runPartyAServer
```
