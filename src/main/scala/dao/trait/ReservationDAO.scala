package dao.`trait`

import dao.{Occupancy, Reservation}

import java.sql.{SQLException, Timestamp}

// SOLID -> I -> Interface Segregation Principle -> Many specific interfaces are better than a general interface
trait ReservationDAO extends DAO[Reservation] {

  @throws(classOf[SQLException])
  def findById(id: Long): Reservation

  @throws(classOf[SQLException])
  def findByRoomIdCheckInCheckOut(id: Long, checkIn: Timestamp,
                                  checkOut: Timestamp): Reservation

  @throws(classOf[SQLException])
  def findAllByDate(firstMoment: Timestamp, lastMoment: Timestamp): List[Occupancy]

  @throws(classOf[SQLException])
  def findAll: List[Reservation]

  @throws(classOf[SQLException])
  def update(id: Long, reservation: Reservation): Unit

  @throws(classOf[SQLException])
  def delete(id: Long): Unit

  @throws(classOf[SQLException])
  def insert(reservation: Reservation): Long
}
