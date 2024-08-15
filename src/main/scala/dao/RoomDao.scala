package dao

import connection.ConnectionProvider
import dao.`trait`.Dao

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import scala.collection.immutable.Nil.:::

case class Room(id: Long, number: String, rtype: String)

case class RoomsList(roomsList: List[Room])

// select field1, field2 from is better than to select * from
// by default jdbc rolls back transaction when throw exception
object RoomDao extends Dao[Room] {

  // https://www.oreilly.com/library/view/scala-cookbook/9781449340292/ch16s02.html
  def findById(id: Long): Room = {
    val connection: Connection = ConnectionProvider.openConnection()
    var resultSet: ResultSet = null
    var room: Room = null
    try {
      resultSet = connection.createStatement().executeQuery(s"""SELECT id, number, type FROM \"Room\" WHERE id = $id LIMIT 1""")
      if (resultSet.next()) {
        room = Room(resultSet.getLong("id"), resultSet.getString("number"), resultSet.getString("type"))
      }
      room
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      connection.close()
    }
  }

  def findAll: List[Room] = {
    val connection: Connection = ConnectionProvider.openConnection()
    var resultSet: ResultSet = null
    var rooms: List[Room] = List()
    try {
      resultSet = connection.createStatement().executeQuery(s"""SELECT id, number, type FROM \"Room\"""")
      while (resultSet.next()) {
        rooms = rooms ::: Room(resultSet.getLong("id"), resultSet.getString("number"), resultSet.getString("type")) :: Nil
      }
      rooms
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      connection.close()
    }
  }

  def update(id: Long, room: Room): Unit = {
    val connection: Connection = ConnectionProvider.openConnection()
    var preparedStatement: PreparedStatement = null
    try {
      preparedStatement = connection.prepareStatement(s"""UPDATE "Room" SET number = ?, type = ? WHERE id = ?""")
      preparedStatement.setString(1, room.number)
      preparedStatement.setString(2, room.rtype)
      preparedStatement.setLong(3, id)
      val rows: Integer = preparedStatement.executeUpdate()

      if rows == 0 then
        throw new SQLException("No rooms affected.")
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
      preparedStatement = connection.prepareStatement(s"""DELETE FROM "Room" WHERE id = ?""")
      preparedStatement.setLong(1, id)
      val rows: Integer = preparedStatement.executeUpdate()

      if rows == 0 then
        throw new SQLException("No rooms affected.")
    } catch {
      case e: Exception => throw e
    } finally {
      preparedStatement.close()
      connection.close()
    }
  }

  def insert(room: Room): Long = {
    val connection: Connection = ConnectionProvider.openConnection()
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null
    try {
      preparedStatement = connection.prepareStatement(s"""INSERT INTO "Room" (number, type) VALUES (?, ?) returning newId""")
      preparedStatement.setString(1, room.number)
      preparedStatement.setString(2, room.rtype)
      resultSet = preparedStatement.executeQuery()
      if (resultSet.next())
        resultSet.getLong("newId")
      else
        throw new SQLException("No rooms affected.")
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      preparedStatement.close()
      connection.close()
    }

  }
}
