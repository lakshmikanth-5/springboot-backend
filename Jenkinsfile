
pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    tools {
        maven 'maven'
    }

    stages {

        stage('Maven Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Archiving') {
            steps {
                archiveArtifacts(
                    artifacts: 'target/*.jar',
                    fingerprint: true,
                    followSymlinks: false,
                    onlyIfSuccessful: true
                )
            }
        }
    }

    post {

        always {
            slackSend(
                channel: '#all-project25',
                message: """
                Job: ${env.JOB_NAME}
                Build: #${env.BUILD_NUMBER}
                Status: ${currentBuild.currentResult}
                URL: ${env.BUILD_URL}
                """,
                teamDomain: 'project25-world',
                tokenCredentialId: 'slack-token'
            )
        }

        cleanup {
            cleanWs()
        }
    }
}

