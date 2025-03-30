package models.domains

import java.util.UUID
import java.time.LocalDateTime

case class Contact(
	id: UUID, 
	firstName: Option[String], 
	middleName: Option[String], 
	lastName: Option[String],
	email: String,
	phoneNumber: String,
	createdAt: Option[LocalDateTime]
);