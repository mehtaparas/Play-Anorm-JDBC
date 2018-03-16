package models

import play.api.libs.json._

case class Customer(siteId: Int, accountnbr: Int, node: String, host: String, headend: String)

object Customer {
  implicit val customerFormat = Json.format[Customer]
  }

