#!/user/bin/env groovy

package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String imageName, String imageTag) {
        script.echo "building the docker image..."
        // script.sh "docker build -t ${imageName}:${imageTag} ."
        script.sh "sudo docker build -t ${imageName}:${imageTag} ."
    }

    def dockerLogin() {
        script.withCredentials([script.usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            // script.sh "echo '${script.PASS}' | docker login -u '${script.USER}' --password-stdin"
            script.sh "echo '${script.PASS}' | sudo docker login -u '${script.USER}' --password-stdin"
        }
    }

    def dockerPush(String imageName, String imageTag) {
        // script.sh "docker push ${imageName}:${imageTag}"
        script.sh "sudo docker push ${imageName}:${imageTag}"
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
