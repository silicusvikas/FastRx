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
-Dsonar.sources=src/main/java/com/parkwoodrx/fastrx\\
-Dsonar.language=$PROJECT_LANGUAGE \\
-Dsonar.projectVersion=$PROJECT_VERSION \\
-Dsonar.projectBaseDir=src/main/java/com/parkwoodrx/fastrx \\
-Dsonar.java.binaries=target/classes \\
-Dsonar.tests=src/test/java/com/parkwoodrx/fastrx\\
-Dsonar.jacoco.reportPaths=target/jacoco.exec \\
-Dsonar.java.coveragePlugin=jacoco \\
-Dsonar.junit.reportPaths=target/surefire-reports \\
-Dsonar.surefire.reportsPath=target/surefire-reports \\
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
