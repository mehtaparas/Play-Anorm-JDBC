package controllers

import javax.inject.Inject

import models._
import play.api.data.Forms._
import play.api.data._
import play.api.i18n._
import play.api.mvc._
import views._
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

/**
  * Manage a database of computers
  */
class HomeController @Inject()(customerService: CustomerRepository,
                               cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  private val logger = play.api.Logger(this.getClass)


  // -- Actions

  /**
    * Handle default path requests; show that service is running
    */
   
  def index = Action { implicit request =>
    Ok("REST API is running! Query for customers at endpoint:/customers/id")
    }


  /**
    * Display the customer.
    *
    * @param accountnbr Account Number of the customer to search for
    */
  
  def search(accountnbr: Int) = Action.async { implicit request =>
    customerService.findById(accountnbr).map {
      customer => Ok(Json.toJson(customer))
    }
  }


}
            
