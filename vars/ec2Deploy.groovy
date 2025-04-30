#!/user/bin/env groovy

import com.example.Docker
def call(String ec2Instance, String shellCmd) {
    return new Docker(this).ec2Deploy(ec2Instance, shellCmd)
}
