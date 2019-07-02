def call(Map config) {
  return [
    [
      path: '/home/jenkins/.config/yarn/global',
      claimName: "${config.project}-home-jenkins-yarn",
      sizeGiB: 1
    ]
  ]
}