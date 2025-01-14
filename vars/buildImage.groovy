#!/user/bin/env groovy

def call() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t nanatwn/demo-app:jma-2.0 .'
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push uba31/demo-app:jma-2.0'
    }
}


//import com.example.Docker
//def call(String imageName) {
    //return new Docker(this).buildDockerImage(imageName)
//}
