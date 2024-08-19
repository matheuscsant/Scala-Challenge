package dao.`trait`

import dao.{Occupancy, Reservation}

import java.sql.Timestamp

// SOLID -> I -> Interface Segregation Principle -> Many interfaces are better than a general interface
trait ReservationDAO extends DAO[Reservation] {

  def findById(id: Long): Reservation

  def findByRoomIdCheckInCheckOut(id: Long, checkIn: Timestamp,
                                  checkOut: Timestamp): Reservation

  def findAllByDate(firstMoment: Timestamp, lastMoment: Timestamp): List[Occupancy]

  def findAll: List[Reservation]

  def update(id: Long, reservation: Reservation): Unit

  def delete(id: Long): Unit

  def insert(reservation: Reservation): Long
}
