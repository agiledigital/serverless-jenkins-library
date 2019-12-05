def call(Map config) {
  return [
    [
      path: '/home/jenkins/.cache',
      claimName: "${config.project}-home-jenkins-cache",
      sizeGiB: 1
    ]
  ]
}