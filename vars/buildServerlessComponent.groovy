/*
 * Toolform-compatible Jenkins 2 Pipeline build step for Serverless Framework apps using the node1016 builder
 */

def call(Map config) {

  def artifactDir = "${config.project}-${config.component}-artifacts"
  def testOutput = "${config.project}-${config.component}-tests.xml"

  final yarn = { cmd ->
    ansiColor('xterm') {
      dir(config.baseDir) {
        sh "NODE_OPTIONS=--max-old-space-size=4096 JEST_JUNIT_OUTPUT=${testOutput} yarn --verbose ${cmd}"
      }
    }
  }

  container("node1016-builder") {

    stage('Build Details') {
      echo "Project:   ${config.project}"
      echo "Component: ${config.component}"
      echo "BuildNumber: ${config.buildNumber}"
    }

    stage('Install dependencies') {
      yarn "install"
      yarn "build"
    }

    stage('Test') {
      yarn 'test --ci --testResultsProcessor="jest-junit"'
      junit allowEmptyResults: true, testResults: testOutput
    }

    stage('Quality Gate') {
      yarn "quality-check"
    }

    stage('Build') {
      yarn "sls package --stage \"${config.stage}\" --package \"${config.baseDir}/dist/\""
    }
  }

  if (config.stage == 'staging' || config.stage == 'live') {

    container('node1016-builder') {

      stage('Package') {
        sh "mkdir -p ${artifactDir}"

        yarn "install --production --ignore-scripts --prefer-offline"
        sh "mv ${config.baseDir}/node_modules ${config.baseDir}/dist ${config.baseDir}/package.json ${artifactDir}"
      }
    }

    stage('Archive to Jenkins') {
      def tarName = "${config.project}-${config.component}-${config.buildNumber}.tar.gz"
      sh "tar -czvf \"${tarName}\" -C \"${artifactDir}\" ."
      archiveArtifacts tarName
    }

  }

}
