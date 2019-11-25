package $package$

object Boot extends App {

  for {
    // parse configurations
    configA <- ConfigProvider.parseConfigServiceA
    configB <- ConfigProvider.parseConfigServiceB
    // create services
    serviceA <- Right(ServiceProvider.serviceA(configA))
    serviceB <- Right(ServiceProvider.serviceB(configA))

    // start server
  } yield c

}