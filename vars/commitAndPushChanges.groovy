#!/user/bin/env groovy

import com.example.Docker
def call() {
    return new Docker(this).commitAndPushChanges(String pomVersion)
}
