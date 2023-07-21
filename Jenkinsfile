pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Run Gradle build with the Gradle Wrapper
                sh './gradlew build'
            }
        }

         stage('Copy JAR to Server') {
            steps {
                // Use the 'withCredentials' block to securely access the credential
                withCredentials([usernamePassword(credentialsId: 'server', passwordVariable: 'SERVER_PASSWORD', usernameVariable: 'SERVER_USERNAME')]) {
                    sh "sshpass -p \"$SERVER_PASSWORD\" scp -r build/libs server@192.168.128.132:~/test"
                }
            }
        }
    }

    post {
        success {
            echo 'JAR file successfully sent to Ubuntu server.'
            // Additional actions on successful build and file transfer
        }

        failure {
            echo 'Failed to send JAR file to Ubuntu server.'
            // Additional actions on build failure
        }
    }
}
