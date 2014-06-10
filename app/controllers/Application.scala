package controllers

import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current

object Application extends Controller {

  def get(id: String) = Action { request =>
    Cache.getAs[AnyContent](id).fold(NotFound("No data for that URL")) {
      case c: AnyContentAsFormUrlEncoded =>
        Ok(c.data)
      case c: AnyContentAsJson =>
        Ok(c.json)
      case c: AnyContentAsXml =>
        Ok(c.xml)
      case c: AnyContentAsText =>
        Ok(c.txt)
      case _ =>
        InternalServerError("Content Type Not Recognized")
    }
  }

  def post(id: String) = Action { request =>
    Cache.set(id, request.body)
    Ok
  }

}