#!/user/bin/env groovy
 
 import com.example.Docker
 def call(String ec2Instance, String shellCmd, String ec2Path) {
     return new Docker(this).ec2Deploy(ec2Instance, shellCmd, ec2Path)
 }
