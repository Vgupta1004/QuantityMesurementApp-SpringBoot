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
                sh '''
                set -e

                JAR_FILE=$(ls target/*.jar | head -n 1)

                echo "Deploying $JAR_FILE"

                scp -o StrictHostKeyChecking=no \
                    -i ~/.ssh/jenkins_deploy_key \
                    "$JAR_FILE" \
                    ubuntu@172.31.39.168:/opt/quantityapp/app.jar

                ssh -o StrictHostKeyChecking=no \
                    -i ~/.ssh/jenkins_deploy_key \
                    ubuntu@172.31.39.168 << 'EOF'
                        sudo systemctl restart quantityapp
                        sudo systemctl status quantityapp --no-pager
EOF
                '''
            }
        }
    }
}
