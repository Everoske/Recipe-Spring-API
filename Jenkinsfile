pipeline {

	agent any
	
	tools {
		maven '3.9.9'
	}

    environment {
        DOCKERHUB_CREDENTIALS = credentials('')
    }

	stages {
		stage('Clean') {
			steps {
				bat 'mvn clean compile'
			}
		}

		stage('Test') {
			steps {
				bat 'mvn test'
			}
		}

		stage('Build') {
        	steps {
        		bat 'mvn package'
        	}
        }

		stage('Build Docker Image') {
		    steps {
		        script {
		            bat 'docker build -t recipe-demo .'
		        }
		    }
		}

		stage ('Run Performance Tests') {
		    parallel {
		        stage('docker compose up') {
		            steps {
		                bat 'docker compose up'
		            }
		        }
		        stage('test') {
		            steps {
		                sleep time: 30, unit: 'SECONDS'
		                bat 'mvn clean verify'
		                bat 'docker compose down'
		            }
		        }
		    }
		}

		stage ('Deploy Image to Docker Hub') {
        		    steps {
                        bat 'docker login -u -p
                        bat 'docker push recipe-demo:latest'
        		    }
        		}
	}

	post {
    	always {
    	    bat 'docker compose down'
    	    bat 'docker logout'
    	}
    }
}