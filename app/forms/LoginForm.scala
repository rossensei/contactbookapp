package forms

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

object LoginForm {

	case class LoginData(email: String, password: String);

	val loginForm = Form(
		mapping(
			"email" -> nonEmptyText,
			"password" -> nonEmptyText
		)(LoginData.apply)(LoginData.unapply)
	)
}