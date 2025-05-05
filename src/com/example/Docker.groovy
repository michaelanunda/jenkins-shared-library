#!/user/bin/env groovy

package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def incrementVersion() {
        script.echo 'Incrementing app version...'
        script.sh 'mvn build-helper:parse-version versions:set \
            -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
            versions:commit'
        def matcher = script.readFile('pom.xml') =~ '<version>(.+)</version>'
        def version = matcher[0][1]
        script.env.pomVersion = version
        script.env.IMAGE_NAME = "uba31/demo-app"
        script.env.IMAGE_TAG = "${version}-${script.env.BUILD_NUMBER}"
        script.env.IMAGE_NAME_TAG = "${script.env.IMAGE_NAME}:${script.env.IMAGE_TAG}"
    }

    def buildDockerImage(String imageNameTag) {
        script.echo "building the docker image..."
        // script.sh "docker build -t ${imageNameTag} ."
        script.sh "sudo docker build -t ${imageNameTag} ."
    }

    def dockerLogin() {
        script.withCredentials([script.usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            // script.sh "echo '${script.PASS}' | docker login -u '${script.USER}' --password-stdin"
            script.sh "echo '${script.PASS}' | sudo docker login -u '${script.USER}' --password-stdin"
        }
    }

    def dockerPush(String imageNameTag) {
        // script.sh "docker push ${imageNameTag}"
        script.sh "sudo docker push ${imageNameTag}"
    }

    def ec2Deploy(String ec2Instance, String shellCmd, String ec2Path) {
        script.sh """
        scp -o StrictHostKeyChecking=no server-cmds.sh ${ec2Instance}:${ec2Path}
        scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2Instance}:${ec2Path}
        ssh -o StrictHostKeyChecking=no ${ec2Instance} "${shellCmd}"
        """
    }
}




/*
       package com.example

       class Docker implements Serializable {

          def script

          Docker(script) {
               this.script = script
          }

          def buildDockerImage(String imageName, String imageTag) {
              script.echo "building the docker image..."
              script.withCredentials([script.usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                    script.sh "docker build -t ${imageName}:${imageTag} ."
                    script.sh "echo '${script.PASS}' | docker login -u '${script.USER}' --password-stdin"
                    script.sh "docker push ${imageName}:${imageTag}"
               }
          }
       }
*/
