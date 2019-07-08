def call() {
  return [
    containerTemplate(
      name: 'node1016-builder',
      image: 'agiledigital/node1016-builder',
      alwaysPullImage: true,
      command: 'cat',
      ttyEnabled: true
    )
  ]
}