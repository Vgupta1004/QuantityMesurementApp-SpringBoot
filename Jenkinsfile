pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven'
    }

    environment {
        AWS_REGION     = 'ap-south-1' 
        ECR_REGISTRY   = '281817609181.dkr.ecr.ap-south-1.amazonaws.com'
        ECR_REPO       = 'quantitymeasurement'
        IMAGE_TAG      = "${BUILD_NUMBER}" 
        BACKEND_IP     = '13.201.49.16'  
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'dev', url: 'https://github.com/Vgupta1004/QuantityMesurementApp-SpringBoot.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Login to AWS ECR') {
            steps {
                withAWS(credentials: 'aws-ecr-creds', region: "${AWS_REGION}") {
                    echo "Logging into Amazon ECR on Jenkins Server..."
                    sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building image tag: ${IMAGE_TAG}"
                sh "docker build -t ${ECR_REGISTRY}/${ECR_REPO}:${IMAGE_TAG} ."
            }
        }

        stage('Push Image to ECR') {
            steps {
                echo "Pushing fresh image to AWS ECR..."
                sh "docker push ${ECR_REGISTRY}/${ECR_REPO}:${IMAGE_TAG}"
            }
        }

        stage('SSH & Deploy to Backend') {
            steps {
                echo "Connecting to docker instance at ${BACKEND_IP}..."
                sh """
                set -e
                ssh -o StrictHostKeyChecking=no -i ~/.ssh/jenkins_deploy_key ubuntu@${BACKEND_IP} << 'EOF'
                    echo "1. Logging backend into AWS ECR..."
                    aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}
                    
                    cd /opt/quantityapp/
                    
                    echo "2. Setting deployment image tag to ${IMAGE_TAG}..."
                    export IMAGE_TAG=${IMAGE_TAG}
                    
                    echo "3. Pulling updated image..."
                    docker compose pull app
                    
                    echo "4. Re-creating application container..."
                    docker compose up -d --no-deps app
                    
                    echo "5. Cleaning up stale Docker cache images..."
                    docker image prune -f
                    
                    echo "Deployment Complete!"
EOF
                """
            }
        }
    }
}
