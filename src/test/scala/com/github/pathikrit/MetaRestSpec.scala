package com.github.pathikrit

import org.specs2.mutable.Specification

object MetaRestSpec extends Specification {
  import com.github.pathikrit.MetaRest._
  import play.api.libs.json.{Json, Reads, Writes}

  def jsonRoundTrip[A: Reads : Writes](model: A) = Json.parse(Json.toJson(model).toString()).as[A] mustEqual model

  "MetaRest" should {
    "Generate Get, Post, Patch, Put models with JSON capabilities" in {
      @MetaRest case class User(
        @get                id            : Int,
        @get @post @patch   name          : String,
        @get @post          email         : String,
                            registeredOn  : Long
      )

      jsonRoundTrip(User.Get(id = 0, name = "Rick", email = "awesome@msn.com"))
      jsonRoundTrip(User.Post(name = "Rick", email = "awesome@msn.com"))
      //TODO Check User.Put() does not exist
      jsonRoundTrip(User.Patch(name = Some("Pathikrit")))
    }

    "Work on complex models" in {
      /*
      sealed trait Document {
        val id: Int
        type Data
      }

      @MetaRest case class Email[A, B](
        @get   override val id             : Int,
        @get @post @patch   subject        : String,
        @put @put           body           : A,
        @get @post @patch   to             : List[String],
        @get @post @patch   cc             : List[String] = Nil,
        @get @post @patch   bcc            : Option[List[String]] = None,
        @get @post      var attachments    : List[B] = Nil
      ) extends Document {
        override type Data = A
      }

      Email.Get(id = 0, subject = "test", to = "me") must beAnInstanceOf[Email.Get]
      */
      todo //TODO: empty metarest, metarest.Get, other annotations, other annotations called Get?
    }
  }
}
