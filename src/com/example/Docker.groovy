#!/user/bin/env groovy

package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def incrementVersion() {
        script.echo 'Incrementing app version inside pom.xml...'
        script.sh 'mvn build-helper:parse-version versions:set \
            -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
            versions:commit'
        def matcher = script.readFile('pom.xml') =~ '<version>(.+)</version>'
        def version = matcher[0][1]
        script.env.pomVersion = version
        script.env.IMAGE_NAME = "uba31/demo-app"
        script.env.IMAGE_TAG = "${version}-${script.env.BUILD_NUMBER}"
        script.env.IMAGE_NAME_TAG = "${script.env.IMAGE_NAME}:${script.env.IMAGE_TAG}"

        /*
        script.IMAGE_TAG = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
        script.env.IMAGE_TAG = IMAGE_TAG
        script.env.IMAGE_NAME_TAG = "${env.IMAGE_NAME}:${IMAGE_TAG}"
        */
    }

    def buildJar() {
        script.echo "building the application for branch ${script.env.BRANCH_NAME}"
        script.sh 'mvn clean install'
    }

    def buildDockerImage(String imageNameTag) {
        script.echo "building the docker image..."
        // script.sh "docker build -t ${imageNameTag} ."
        script.sh "sudo docker build -t ${imageNameTag} ."
    }

    def dockerLogin() {
        script.withCredentials([script.usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            script.echo "Logging into DockerHub..."
            // script.sh "echo '${script.PASS}' | docker login -u '${script.USER}' --password-stdin"
            script.sh "echo '${script.PASS}' | sudo docker login -u '${script.USER}' --password-stdin"
        }
    }

    def dockerPush(String imageNameTag) {
        script.echo "Pushing Image into DockerHub..."
        // script.sh "docker push ${imageNameTag}"
        script.sh "sudo docker push ${imageNameTag}"
    }

    def ec2Deploy(String ec2Instance, String shellCmd, String ec2Path) {
        script.echo "Deploying and Running Image inside of EC2 Instance..."
        script.sh """
        scp -o StrictHostKeyChecking=no server-cmds.sh ${ec2Instance}:${ec2Path}
        scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2Instance}:${ec2Path}
        ssh -o StrictHostKeyChecking=no ${ec2Instance} "${shellCmd}"
        """
    }

    def commitAndPushChanges(String pomVersion) {
        script.echo "Commiting and pushing changes to remote Git repo..."
        script.withCredentials([usernamePassword(credentialsId: 'github-credentials', usernameVariable: 'GITHUB_USER', passwordVariable: 'GITHUB_TOKEN')]) {
        script.sh """
        git config --global user.name 'Jenkins'
        git config --global user.email 'jenkins@example.com'
        git status
        git branch
        git config --list
        git add .
        git commit -m "Incremented version of pom.xml to ${pomVersion}"
        git push https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/michaelanunda/mavenapp2.1.git HEAD:starting-code
        """
     }
    }
}
