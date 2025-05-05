#!/user/bin/env groovy

import com.example.Docker
def call(String pomVersion) {
    return new Docker(this).commitAndPushChanges(pomVersion)
}
