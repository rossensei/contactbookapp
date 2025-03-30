package models.repositories

import javax.inject._
import java.util.UUID
import java.time.LocalDateTime
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.PostgresProfile
import play.api.db.slick._
import models.domains.Contact
import scala.concurrent.Future
import forms.ContactForm.ContactData

@Singleton
class ContactRepo @Inject()(val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[PostgresProfile] {
	import profile.api._

	// Contact table definition
	case class ContactsTable(tag: Tag) extends Table[Contact](tag, "CONTACTS") {
		def id = column[UUID]("ID", O.PrimaryKey)
		def firstName = column[Option[String]]("FIRST_NAME")
		def middleName = column[Option[String]]("MIDDLE_NAME")
		def lastName = column[Option[String]]("LAST_NAME")
		def email = column[String]("EMAIL")
		def phoneNumber = column[String]("PHONE_NUMBER")
		def createdAt = column[Option[LocalDateTime]]("CREATED_AT")

		def * = (id, firstName, middleName, lastName, email, phoneNumber, createdAt).mapTo[Contact]
	}

	lazy val contacts = TableQuery[ContactsTable]

	def all: Future[Seq[Contact]] = db.run(contacts.result)

	def insert(contact: Contact): Future[Int] = db.run(contacts += contact)

	def find(contactId: UUID): Future[Option[Contact]] = db.run(contacts.filter(_.id === contactId).result.headOption)

	def update(contactData: ContactData, contactId: UUID): Future[Int] = 
		db.run(
			contacts.filter(_.id === contactId).map(contact => 
				(contact.firstName, contact.middleName, contact.lastName, contact.email, contact.phoneNumber)
			).update(
				contactData.firstName, 
				contactData.middleName, 
				contactData.lastName, 
				contactData.email,
				contactData.phoneNumber
			)
		)

	def delete(contactId: UUID): Future[Int] = db.run(contacts.filter(_.id === contactId).delete)
}