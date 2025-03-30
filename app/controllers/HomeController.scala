package controllers

import javax.inject._
import java.util.UUID
import java.time.LocalDateTime
import play.api._
import play.api.mvc._
import forms.ContactForm._
import play.api.i18n.I18nSupport
import models.services.ContactService
import models.domains.Contact
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(contactService: ContactService, val controllerComponents: ControllerComponents)
  (implicit val ec: ExecutionContext) extends BaseController with I18nSupport {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action.async { implicit request: Request[AnyContent] =>
    contactService.get.map { contacts => Ok(views.html.index(contacts)) }
  }

  def create() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.create(contactForm))
  }

  // returns an asynchronous result so chain the async method for this action
  def store() = Action.async { implicit request: Request[AnyContent] =>
    contactForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.create(formWithErrors)))
      },
      contact => {
        val id = UUID.randomUUID
        val timestamp = LocalDateTime.now

        val newContact = new Contact(
          id, 
          contact.firstName, 
          contact.middleName, 
          contact.lastName, 
          contact.email, 
          contact.phoneNumber, 
          Some(timestamp)
        )

        contactService.store(newContact).map {
          case 1 => Redirect(routes.HomeController.index())
          case 0 => BadRequest("Insertion failed!")
        }
      }
    )
  }

  def edit(contactId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    contactService.findById(contactId).map {
      case Some(contact) => 
        val editContactForm = contactForm.fill(
          ContactData(
            contact.firstName, 
            contact.middleName, 
            contact.lastName, 
            contact.email, 
            contact.phoneNumber
          )
        )

        Ok(views.html.edit(editContactForm, contactId))

      case None => BadRequest("Contact not found!")
    }
  }

  def update(contactId: UUID) = Action.async { implicit request: Request[AnyContent] => 
    contactForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.edit(formWithErrors, contactId)))
      },
      updatedContact => {
        contactService.update(updatedContact, contactId).map { rows =>
          if(rows > 0) {
            Redirect(routes.HomeController.index())
          } else {
            BadRequest("Update Failed!")
          }
        }
      }
    )
  }

  def delete(contactId: UUID) = Action.async { implicit request: Request[AnyContent] => 
    contactService.delete(contactId).map { rows =>
      if(rows > 0) {
        Redirect(routes.HomeController.index())
      } else {
        BadRequest("Cannot delete contact!")
      }
    }
  }
}
