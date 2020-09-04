pipeline {
    agent {
        docker {
            image 'maven:java11' 
            args '-v /root/.m2:/root/.m2 -e "JAVA_HOME=/usr/lib/jvm/java-11-openjdk/"' 
        }
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
    }
}
