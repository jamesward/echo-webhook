import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json.Json

import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.xml.NodeSeq


@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  val URL = "/_test"

  val get = FakeRequest(GET, URL)
  val post = FakeRequest(POST, URL)

  "Application" should {

    "form url encoded" in new WithApplication {
      Await.result(route(post.withFormUrlEncodedBody("foo" -> "bar")).get, Duration.Inf)
      val Some(result) = route(get)
      status(result) must beEqualTo(OK)
      contentAsString(result) must beEqualTo("foo=bar")
    }

    "json" in new WithApplication {
      Await.result(route(post.withJsonBody(Json.obj("foo" -> "bar"))).get, Duration.Inf)
      val Some(result) = route(get)
      status(result) must beEqualTo(OK)
      contentAsString(result) must beEqualTo("""{"foo":"bar"}""")
    }

    "xml" in new WithApplication {
      Await.result(route(post.withXmlBody(<foo>bar</foo>)).get, Duration.Inf)
      val Some(result) = route(get)
      status(result) must beEqualTo(OK)
      contentAsString(result) must beEqualTo("<foo>bar</foo>")
    }

    "plain text" in new WithApplication {
      Await.result(route(post.withBody("test")).get, Duration.Inf)
      val Some(result) = route(get)
      status(result) must beEqualTo(OK)
      contentAsString(result) must beEqualTo("test")
    }

  }
}
