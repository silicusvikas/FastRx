pipeline {
  agent any 
  stages {
    
    stage('Development') {
            steps {
					sh 'echo "Starting FastRx Build ...."'
					sh 'mvn --version'
					sh 'mvn clean install'
				}
			}
    stage('SonarQube Analysis') {
      environment {
        PROJECT_NAME = 'Silicus-FastRx-Java-Demo'
        PROJECT_KEY = 'silicus-java-demo'
        PROJECT_BRANCH = 'development'
        SONAR_HOST_URL = 'http://silicus.eastus.cloudapp.azure.com:9000'
        PROJECT_SOURCE_ENCODING = 'UTF-8'
        PROJECT_LANGUAGE = 'java'
      }
      steps {          
        sh '''PROJECT_VERSION=1.0.$(date +%y)$(date +%j).$BUILD_NUMBER
/opt/sonar/bin/sonar-runner -Dsonar.projectName=$PROJECT_NAME \\
-Dsonar.projectKey=$PROJECT_KEY \\
-Dsonar.host.url=$SONAR_HOST_URL \\
-Dsonar.sourceEncoding=$PROJECT_SOURCE_ENCODING \\
-Dsonar.sources=/var/lib/jenkins/workspace/FastRx_development-RSKHFJXL7NFTNYVMAT64PAOGI7A6K2EH7JQQVFAOTWKS766OWDJA/src/main/java \\
-Dsonar.language=$PROJECT_LANGUAGE \\
-Dsonar.projectVersion=$PROJECT_VERSION \\
-Dsonar.projectBaseDir=/var/lib/jenkins/workspace/FastRx_development-RSKHFJXL7NFTNYVMAT64PAOGI7A6K2EH7JQQVFAOTWKS766OWDJA/src \\
-Dsonar.java.binaries=/var/lib/jenkins/workspace/FastRx_development-RSKHFJXL7NFTNYVMAT64PAOGI7A6K2EH7JQQVFAOTWKS766OWDJA/target/classes \\
-Dsonar.tests=/var/lib/jenkins/workspace/FastRx_development-RSKHFJXL7NFTNYVMAT64PAOGI7A6K2EH7JQQVFAOTWKS766OWDJA/src/test/java/com/parkwoodrx/fastrx/webservice/test \\
-Dsonar.jacoco.reportPaths=/var/lib/jenkins/workspace/FastRx_development-RSKHFJXL7NFTNYVMAT64PAOGI7A6K2EH7JQQVFAOTWKS766OWDJA/target/jacoco.exec \\
-Dsonar.java.coveragePlugin=jacoco \\
-Dsonar.junit.reportPaths=/var/lib/jenkins/workspace/FastRx_development-RSKHFJXL7NFTNYVMAT64PAOGI7A6K2EH7JQQVFAOTWKS766OWDJA/target/surefire-reports \\
-Dsonar.surefire.reportsPath=/var/lib/jenkins/workspace/FastRx_development-RSKHFJXL7NFTNYVMAT64PAOGI7A6K2EH7JQQVFAOTWKS766OWDJA/target/surefire-reports \\
-Dsonar.verbose=false '''   
   }
    }		
     	    
    stage('Delete Workspace') {
      parallel {
        stage('Delete Workspace') {
          steps {
            cleanWs(cleanWhenAborted: true, cleanWhenFailure: true, cleanWhenNotBuilt: true, cleanWhenUnstable: true, cleanWhenSuccess: true, cleanupMatrixParent: true, deleteDirs: true)
          }
        }
      }
    }
  }
    post {
    success {
      mail(to: 'vikas.kishore@silicus.com', subject: "Success Pipeline: ${currentBuild.fullDisplayName}", body: "Congratulations pipeline build successfully ${env.BUILD_URL}")
    }
    failure {
      mail(to: 'vikas.kishore@silicus.com', subject: "Failed Pipeline: ${currentBuild.fullDisplayName}", body: "Something is wrong with ${env.BUILD_URL}")
    }
  }
}
