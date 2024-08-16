package dao

import connection.ConnectionProvider
import dao.`trait`.Dao

import java.sql.*
import scala.collection.immutable.Nil.:::

case class Reservation(id: Long, guestId: Long, roomId: Long, checkIn: String, checkOut: String)

case class ReservationsList(reservationsList: List[Reservation])

case class Occupancy(guest: Guest, room: Room, checkIn: String, checkOut: String)

case class OccupancyList(occupancyList: List[Occupancy])

object ReservationDao extends Dao[Reservation] {

  def findById(id: Long): Reservation = {
    val connection: Connection = ConnectionProvider.openConnection()
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null
    var reservation: Reservation = null
    try {
      preparedStatement = connection.prepareCall(s"""SELECT id, guest_id, room_id, check_in, check_out FROM \"reservation\" WHERE id = ? LIMIT 1""")
      preparedStatement.setLong(1, id)
      resultSet = preparedStatement.executeQuery()
      if resultSet.next() then
        reservation = Reservation(resultSet.getLong("id"), resultSet.getLong("guest_id"),
          resultSet.getLong("room_id"), resultSet.getTimestamp("check_in").toString, resultSet.getTimestamp("check_out").toString)
      reservation
    } catch {
      case e: Exception => throw e
    } finally {
      if resultSet != null then
        resultSet.close()
      preparedStatement.close()
      connection.close()
    }
  }

  def findByRoomIdCheckInCheckOut(id: Long, checkIn: Timestamp,
                                  checkOut: Timestamp): Reservation = {
    val connection: Connection = ConnectionProvider.openConnection()
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null
    var reservation: Reservation = null
    try {
      preparedStatement = connection.prepareStatement(
        s"""SELECT id, guest_id, room_id, check_in, check_out
           FROM \"reservation\" WHERE room_id = ? AND ((check_in BETWEEN ? AND ?)
       OR (check_out BETWEEN ? AND ?)) LIMIT 1""")

      preparedStatement.setLong(1, id)
      preparedStatement.setTimestamp(2, checkIn)
      preparedStatement.setTimestamp(3, checkOut)
      preparedStatement.setTimestamp(4, checkIn)
      preparedStatement.setTimestamp(5, checkOut)


      resultSet = preparedStatement.executeQuery()

      if resultSet.next() then
        reservation = Reservation(resultSet.getLong("id"), resultSet.getLong("guest_id"),
          resultSet.getLong("room_id"), resultSet.getTimestamp("check_in").toString, resultSet.getTimestamp("check_out").toString)
      reservation
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      connection.close()
    }
  }

  def findAllByDate(firstMoment: Timestamp, lastMoment: Timestamp): List[Occupancy] = {
    val connection: Connection = ConnectionProvider.openConnection()
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null
    var occupancies: List[Occupancy] = List()
    try {
      preparedStatement = connection.prepareStatement(
        s"""SELECT id, guest_id, guest_name, room_id, room_number, room_type, check_in, check_out
           FROM \"view_occupancy\" WHERE (check_in BETWEEN ? AND ?)
       OR (check_out BETWEEN ? AND ?) order by check_in""")


      preparedStatement.setTimestamp(1, firstMoment)
      preparedStatement.setTimestamp(2, lastMoment)
      preparedStatement.setTimestamp(3, firstMoment)
      preparedStatement.setTimestamp(4, lastMoment)

      resultSet = preparedStatement.executeQuery()

      while (resultSet.next()) {
        occupancies = occupancies ::: Occupancy(Guest(resultSet.getLong("guest_id"), resultSet.getString("guest_name")),
          Room(resultSet.getLong("room_id"), resultSet.getString("room_number"), resultSet.getString("room_type")),
          resultSet.getTimestamp("check_in").toString, resultSet.getTimestamp("check_out").toString) :: Nil
      }
      occupancies
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      connection.close()
    }
  }

  def findAll: List[Reservation] = {
    val connection: Connection = ConnectionProvider.openConnection()
    var resultSet: ResultSet = null
    var reservations: List[Reservation] = List()
    try {
      resultSet = connection.createStatement().executeQuery(s"""SELECT id, guest_id, room_id, check_in, check_out FROM \"reservation\"""")
      while (resultSet.next()) {
        reservations = reservations ::: Reservation(resultSet.getLong("id"), resultSet.getLong("guest_id"), resultSet.getLong("room_id"), resultSet.getTimestamp("check_in").toString, resultSet.getTimestamp("check_out").toString) :: Nil
      }
      reservations
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      connection.close()
    }
  }

  def update(id: Long, reservation: Reservation): Unit = {
    val connection: Connection = ConnectionProvider.openConnection()
    var preparedStatement: PreparedStatement = null
    try {
      preparedStatement = connection.prepareStatement(s"""UPDATE "reservation" SET name = ? WHERE id = ?""")
      preparedStatement.setString(1, "")
      preparedStatement.setLong(2, id)
      val rows: Integer = preparedStatement.executeUpdate()

      if rows == 0 then
        throw new SQLException("No guests affected.")
    } catch {
      case e: Exception => throw e
    } finally {
      preparedStatement.close()
      connection.close()
    }
  }

  def delete(id: Long): Unit = {
    val connection: Connection = ConnectionProvider.openConnection()
    var preparedStatement: PreparedStatement = null
    try {
      preparedStatement = connection.prepareStatement(s"""DELETE FROM "reservation" WHERE id = ?""")
      preparedStatement.setLong(1, id)
      val rows: Integer = preparedStatement.executeUpdate()

      if rows == 0 then
        throw new SQLException("No guests affected.")
    } catch {
      case e: Exception => throw e
    } finally {
      preparedStatement.close()
      connection.close()
    }
  }

  def insert(reservation: Reservation): Long = {
    val connection: Connection = ConnectionProvider.openConnection()
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null
    try {
      preparedStatement = connection.prepareStatement(s"""INSERT INTO "reservation" (guest_id, room_id, check_in, check_out) VALUES (?, ?, ?, ?) returning id""")
      preparedStatement.setLong(1, reservation.guestId)
      preparedStatement.setLong(2, reservation.roomId)
      preparedStatement.setTimestamp(3, Timestamp.valueOf(reservation.checkIn))
      preparedStatement.setTimestamp(4, Timestamp.valueOf(reservation.checkOut))
      resultSet = preparedStatement.executeQuery()
      if (resultSet.next())
        resultSet.getLong("id")
      else
        throw new SQLException("No guests affected.")
    } catch {
      case e: Exception => throw e
    } finally {
      if resultSet != null then
        resultSet.close()
      preparedStatement.close()
      connection.close()
    }

  }
}
