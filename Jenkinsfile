node {
  def image
   stage ('checkout') {
        checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '', url: 'https://github.com/sojess-work/mailService.git']]])
        }

   stage ('Build') {
         def mvnHome = tool name: 'Maven', type: 'maven'
         def mvnCMD = "${mvnHome}/bin/mvn "
         sh "${mvnCMD} clean package"
        }
    stage('Build image') {

       app = docker.build("verdant-tempest-376308/myauthapp")
    }

    stage('Push image to gcr') {
        docker.withRegistry('https://gcr.io', 'gcr:auth-app') {
            app.push("mailservice-0.0.1-SNAPSHOT")
        }
    }

}