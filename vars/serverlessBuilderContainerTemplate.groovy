def call() {
  return [
    containerTemplate(
      name: 'node810-builder',
      image: 'agiledigital/node810-builder',
      alwaysPullImage: true,
      command: 'cat',
      ttyEnabled: true
    )
  ]
}