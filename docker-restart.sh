#!/usr/bin/expect
spawn ssh -l root 1.119.166.179
set timeout -1
expect "root@1.119.166.179's password: "
send "LqIkKKTIcKeTIXSF4FF6Ftp\r"
expect "*]#"
send "docker-compose -f /jbh/servers/docker-compose-microservices.yml restart [lindex $argv 0] && exit\r"
expect eof