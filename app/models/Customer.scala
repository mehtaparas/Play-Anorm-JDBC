package models

import play.api.libs.json._

case class Customer(accountnbr: Int, siteId: Int, video: Int, voice: Int, data: Int)

object Customer {
  implicit val customerFormat = Json.format[Customer]
  }

