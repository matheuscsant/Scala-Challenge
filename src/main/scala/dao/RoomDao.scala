package dao

import java.sql.*
import scala.collection.immutable.Nil.:::

case class Room(id: Long, number: String, rtype: String)

case class RoomsList(RoomsList: List[Room])

// select field1, field2 from is better than to select * from
// by default jdbc rolls back transaction when throw exception
object RoomDao {

  private val url: String = "jdbc:postgresql://localhost:5432/challenge_scala"
  private val user: String = "postgres"
  private val password: String = "123456789"

  // https://www.oreilly.com/library/view/scala-cookbook/9781449340292/ch16s02.html
  def findById(id: Long): Room = {
    // https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
    // Class.forName("org.postgresql.Driver") - Driver JDBC >= JDBC 4.0, don't need
    val connection: Connection = DriverManager.getConnection(url, user, password)
    var resultSet: ResultSet = null
    var room: Room = null
    try {
      resultSet = connection.createStatement().executeQuery(s"""SELECT id, number, type FROM \"Room\" WHERE id = $id LIMIT 1""")
      if (resultSet.next()) {
        room = Room(resultSet.getLong("id"), resultSet.getString("number"), resultSet.getString("type"))
      }
      room
    } catch {
      case e: SQLException => throw e
    } finally {
      resultSet.close()
      connection.close()
    }
  }

  def findAll: RoomsList = {
    val connection: Connection = DriverManager.getConnection(url, user, password)
    var resultSet: ResultSet = null
    var rooms: List[Room] = List()
    try {
      resultSet = connection.createStatement().executeQuery(s"""SELECT id, number, type FROM \"Room\"""")
      while (resultSet.next()) {
        rooms = rooms ::: Room(resultSet.getLong("id"), resultSet.getString("number"), resultSet.getString("type")) :: Nil
      }
      RoomsList(rooms)
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      connection.close()
    }
  }

  def update(id: Long, room: Room): Unit = {
    val connection: Connection = DriverManager.getConnection(url, user, password)
    var preparedStatement: PreparedStatement = null
    try {
      preparedStatement = connection.prepareStatement(s"""UPDATE "Room" SET number = ?, type = ? WHERE id = ?""")
      preparedStatement.setString(1, room.number)
      preparedStatement.setString(2, room.rtype)
      preparedStatement.setLong(3, id)
      val rows: Integer = preparedStatement.executeUpdate()

      if rows == 0 then
        throw new SQLException("Operation failed.")
    } catch {
      case e: Exception => throw e
    } finally {
      preparedStatement.close()
      connection.close()
    }
  }

  def insert(room: Room): Long = {
    val connection: Connection = DriverManager.getConnection(url, user, password)
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null
    try {
      preparedStatement = connection.prepareStatement(s"""INSERT INTO "Room" (number, type) VALUES (?, ?) returning newId""")
      preparedStatement.setString(1, room.number)
      preparedStatement.setString(2, room.rtype)
      preparedStatement.executeQuery()
      resultSet = preparedStatement.getResultSet
      if (resultSet.next())
        resultSet.getLong("newId")
      else
        throw new SQLException("Operation failed.")
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      preparedStatement.close()
      connection.close()
    }

  }
}
