#!/user/bin/env groovy

def call(String shellCmd, String ec2Instance) {
    sh """
        scp -o StrictHostKeyChecking=no server-cmds.sh ${ec2Instance}:/home/ec2-user
        scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2Instance}:/home/ec2-user
        ssh -o StrictHostKeyChecking=no ${ec2Instance} "${shellCmd}"
    """
}
