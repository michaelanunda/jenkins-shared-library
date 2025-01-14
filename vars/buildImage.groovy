#!/user/bin/env groovy

def call(String imageName, String imageTag) {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t ${imageName}:${imageTag} ."
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh "docker push ${imageName}:${imageTag}"
    }
}


//import com.example.Docker
//def call(String imageName) {
    //return new Docker(this).buildDockerImage(imageName)
//}
