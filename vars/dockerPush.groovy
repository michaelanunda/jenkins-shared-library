#!/user/bin/env groovy

import com.example.Docker
def call(String imageName, String imageTag) {
    return new Docker(this).dockerPush(imageName, imageTag)
}
