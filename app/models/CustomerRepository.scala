package models

import java.util.Date
import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

import scala.concurrent.Future



@javax.inject.Singleton
class CustomerRepository @Inject()(dbapi: DBApi)(implicit ec: DatabaseExecutionContext) {

  private val db = dbapi.database("default")

  // -- Parsers

  /**
   * Parse a Computer from a ResultSet
   */

  private val simple = {
    get[Int]("accountnbr") ~
      get[Int]("siteId") ~
      get[Int]("video") ~
      get[Int]("voice") ~
      get[Int]("data") map {
      case accountnbr~siteId~video~voice~data =>
        Customer(accountnbr, siteId, video, voice, data)
    }
  }

  // -- Queries

  /**
   * Retrieve a customer from the accountNbr.
   */

  def findById(accountnbr: Int): Future[Option[Customer]] = Future {
    db.withConnection { implicit connection =>
      SQL("select * from customers where accountnbr = {accountnbr}").on('accountnbr -> accountnbr).as(simple.singleOpt)
    }
  }(ec)




}
