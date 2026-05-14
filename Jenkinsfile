pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven'
    }

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'dev',
                url: 'https://github.com/Vgupta1004/QuantityMesurementApp-SpringBoot.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy to Backend') {
            steps {
                sshagent(['backend-ssh']) {
                    sh '''
                    scp -o StrictHostKeyChecking=no target/*.jar ubuntu@172.31.39.168:/opt/quantityapp/app.jar

                    ssh -o StrictHostKeyChecking=no ubuntu@172.31.39.168 << EOF
                        sudo systemctl restart quantityapp
EOF
                    '''
                }
            }
        }
    }
}
