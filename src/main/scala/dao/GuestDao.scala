package dao

import connection.ConnectionProvider
import dao.`trait`.Dao

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import scala.collection.immutable.Nil.:::

case class Guest(id: Long, name: String)

case class GuestsList(guestsList: List[Guest])

object GuestDao extends Dao[Guest] {

  def findById(id: Long): Guest = {
    val connection: Connection = ConnectionProvider.openConnection()
    var resultSet: ResultSet = null
    var guest: Guest = null
    try {
      resultSet = connection.createStatement().executeQuery(s"""SELECT id, name FROM \"Guest\" WHERE id = $id LIMIT 1""")
      if (resultSet.next()) {
        guest = Guest(resultSet.getLong("id"), resultSet.getString("name"))
      }
      guest
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      connection.close()
    }
  }

  def findAll: List[Guest] = {
    val connection: Connection = ConnectionProvider.openConnection()
    var resultSet: ResultSet = null
    var guests: List[Guest] = List()
    try {
      resultSet = connection.createStatement().executeQuery(s"""SELECT id, name FROM \"Guest\"""")
      while (resultSet.next()) {
        guests = guests ::: Guest(resultSet.getLong("id"), resultSet.getString("name")) :: Nil
      }
      guests
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      connection.close()
    }
  }

  def update(id: Long, guest: Guest): Unit = {
    val connection: Connection = ConnectionProvider.openConnection()
    var preparedStatement: PreparedStatement = null
    try {
      preparedStatement = connection.prepareStatement(s"""UPDATE "Guest" SET name = ? WHERE id = ?""")
      preparedStatement.setString(1, guest.name)
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
      preparedStatement = connection.prepareStatement(s"""DELETE FROM "Guest" WHERE id = ?""")
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

  def insert(guest: Guest): Long = {
    val connection: Connection = ConnectionProvider.openConnection()
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null
    try {
      preparedStatement = connection.prepareStatement(s"""INSERT INTO "Guest" (name) VALUES (?) returning newId""")
      preparedStatement.setString(1, guest.name)
      preparedStatement.executeQuery()
      resultSet = preparedStatement.getResultSet
      if (resultSet.next())
        resultSet.getLong("newId")
      else
        throw new SQLException("No guests affected.")
    } catch {
      case e: Exception => throw e
    } finally {
      resultSet.close()
      preparedStatement.close()
      connection.close()
    }

  }
}
