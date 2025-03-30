package models.services

import javax.inject._
import java.util.UUID
import models.repositories.ContactRepo
import scala.concurrent.Future
import models.domains.Contact
import forms.ContactForm.ContactData

@Singleton
class ContactService @Inject()(repo: ContactRepo) {
	
	def get: Future[Seq[Contact]] = repo.all

	def store(contact: Contact): Future[Int] = repo.insert(contact)

	def findById(contactId: UUID): Future[Option[Contact]] = repo.find(contactId)

	def update(contactData: ContactData, contactId: UUID): Future[Int] = repo.update(contactData, contactId)

	def delete(contactId: UUID): Future[Int] = repo.delete(contactId)
}