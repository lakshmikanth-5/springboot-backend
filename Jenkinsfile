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
       
        stage('maven build ') {
            steps {
               sh 'mvn clean package'
            }
        }

        stage ('archieving') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, followSymlinks: false, onlyIfSuccessful: true
            }
        }
      
    }
    post{
        always{
            emailext body: '''Hi,

     The jenkins has been failed . please check it.

     Thanks
     Devops Team''', subject: 'testing jenkins pipeline: $JOB_URL', to: 'laxmikanthreddy99@outlook.com'
    }
    cleanup{
     cleanWs()
    }
    }

}
