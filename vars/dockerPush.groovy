#!/user/bin/env groovy

import com.example.Docker
def call(String imageNameTag) {
    return new Docker(this).dockerPush(imageNameTag)
}
