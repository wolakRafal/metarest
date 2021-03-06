[![Build Status](https://travis-ci.org/pathikrit/metarest.png?branch=master)](http://travis-ci.org/pathikrit/metarest)

MetaRest
--------
Use Scala macros to generate your RESTy models

Let's say you have the following `User` model in your business layer:

```scala
case class User(id: Int, name: String, email: String, registeredOn: DateTime)
```

But, now you want to create well-formed models to describe the requests/responses of your HTTP REST APIs:
```scala
// Response to GET /users/$id (Retrieve an existing user)
case class UserGet(id: Int, name: String, email: String)

// Request body of POST /users (Create a new user)
case class UserPost(name: String, email: String)

//Request body of PATCH /users/$id (Edit name of an existing user)
case class UserPatch(name: Option[String])
```

That is a lot of boilerplate! Keeping all these request models in sync with your business model and/or adding/removing fields quickly becomes difficult and cumbersome for more complicated models.

With MetaRest, all you need to do:
```scala
import com.github.pathikrit.MetaRest._

@MetaRest case class User(
  @get               id            : Int,
  @get @post @patch  name          : String,
  @get @post         email         : String,
                     registeredOn  : DateTime
)
```

The above block would generate code essentially looking like this:
```scala
object User {
  case class Get(id: Int, name: String, email: String)
  case class Post(name: String, email: String)
  case class Patch(name: Option[String])
}
```

Now, you can have a well defined CRUD interface:
```scala
trait UserRepo {
  def get(id: Int): User.Get
  def create(request: User.Post): User.Get
  def update(id: Int, request: User.Patch): User.Get
}
```

MetaRest also automatically generates Play's JSON formatters for all the models using
the [json-annotation](https://github.com/kifi/json-annotation) macro:

```scala
import play.api.libs.json.Json

val jsonStr = """{
  "id": 0,
  "name": "Rick",
  "email": "awesome@msn.com"
}"""
val request: User.Get = Json.parse(jsonStr).as[User.Get]
val json: JsValue = Json.toJson(request)

println(request)
println(json)
assert(json.toString == jsonStr)
```

SBT: In your `build.sbt`, add the following entries:

```scala
resolvers += Resolver.bintrayRepo("pathikrit", "maven")

libraryDependencies += "com.github.pathikrit" %% "metarest" % 0.2.0

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full)
```

The latest published version can be found here:
http://dl.bintray.com/pathikrit/maven/com/github/pathikrit
