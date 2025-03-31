package controllers

import play.api._
import play.api.mvc._
import javax.inject._
import play.api.i18n.I18nSupport
import forms.LoginForm._

@Singleton
class AuthController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport{
	
	def login() = Action { implicit request: Request[AnyContent] =>
		Ok(views.html.auth.login(loginForm))
	}

	def authenticate() = Action { implicit request: Request[AnyContent] =>
		loginForm.bindFromRequest().fold(
			formWithErrors => {
				BadRequest(views.html.auth.login(formWithErrors))
			},
			credentials => {
				Ok(s"${credentials.toString}")
			}
		)
	}
}