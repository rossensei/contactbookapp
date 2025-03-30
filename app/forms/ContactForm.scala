package forms

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

object ContactForm {
	// model the form
	case class ContactData(
		firstName: Option[String], 
		middleName: Option[String], 
		lastName: Option[String], 
		email: String, 
		phoneNumber: String
	)

	val contactForm = Form(
		mapping(	
			"firstName" -> optional(text),
			"middleName" -> optional(text),
			"lastName" -> optional(text),
			"email" -> nonEmptyText,
			"phoneNumber" -> nonEmptyText
		)(ContactData.apply)(ContactData.unapply)
		.verifying("At least one name must be provided (first name, middle name, or last name)", form =>
			form.firstName.isDefined || form.middleName.isDefined || form.lastName.isDefined
		)
	)
}