# $name$

## The "Module" concept
A module is a composable object that helps you to isolate and then, via the composition, use it to provide a service. 
The module is a base concept that helps our small head to be able to manage complex problems.

A module looks like:
```scala
// file example/users/UserModule.scala
package example.users

object UserModule {
  
  trait Service {
    def getUser(id: UserId): IO[Option[User]]
    def createUser(definition: UserDefintion): IO[User]  
  }

  def Live(): IO[UserModule.Service] =
    ??? 

}
```
```scala
// file example/users/Types.scala
package example.users

case class UserDefinition(
  name: String
)

case class UserId(
  value: String
) extends AnyVal

case class User(
  id: UserId,
  name: String
)
```