#!/bin/bash

ROOT_DIR="/Users/qiumi/Code/MicroServices"
MIDDLE_PATH="$1"
MODULE="$2"
PROPERTIES_FILE="$3"

echo
echo "###############################################################"
echo "############### $MODULE start ##################"
echo "###############################################################"

    ## 编译
    echo
    echo "---- package $MODULE ----"
    cd $MIDDLE_PATH

    /Library/Java/JavaVirtualMachines/jdk1.8.0_191.jdk/Contents/Home/bin/java -Dmaven.multiModuleProjectDirectory=$ROOT_DIR/$MIDDLE_PATH "-Dmaven.home=/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3" "-Dclassworlds.conf=/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/m2.conf" "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=63108:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/boot/plexus-classworlds-2.5.2.jar" org.codehaus.classworlds.Launcher -Didea.version=2018.1.7 package -f pom.xml

    cd $ROOT_DIR

    ## 拷贝文件
    echo 
    echo "---- copy file $MODULE ----"
    ./copyfile.sh $MIDDLE_PATH/target/$MODULE-0.0.1-SNAPSHOT.jar /jbh/servers/data/$MODULE.jar
    ./copyfile.sh $MIDDLE_PATH/src/main/resources/$PROPERTIES_FILE.properties /jbh/servers/data/$MODULE.properties

    ## 启动服务
    echo 
    echo "---- docker restart $MODULE ----"
    ./docker-restart.sh $MODULE

echo
echo "###############################################################"
echo "############### $MODULE end ##################"
echo "###############################################################"

exit 1

