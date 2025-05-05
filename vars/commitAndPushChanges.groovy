#!/user/bin/env groovy

import com.example.Docker
def call(String pomVersion) {
    withCredentials([usernamePassword(credentialsId: 'github-credentials', usernameVariable: 'GITHUB_USER', passwordVariable: 'GITHUB_TOKEN')]) {
    return new Docker(this).commitAndPushChanges(pomVersion)
}
 }
