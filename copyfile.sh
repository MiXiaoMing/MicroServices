#!/usr/bin/expect
spawn scp [lindex $argv 0] root@1.119.166.179:[lindex $argv 1]
set timeout -1
expect "root@1.119.166.179's password: "
send "LqIkKKTIcKeTIXSF4FF6Ftp\r"
expect eof