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
    var preparedStatement: PreparedStatement = null
    var resultSet: ResultSet = null
    var guest: Guest = null
    try {
      preparedStatement = connection.prepareStatement(s"""SELECT id, name FROM \"guest\" WHERE id = ? LIMIT 1""")
      preparedStatement.setLong(1, id)
      resultSet = preparedStatement.executeQuery()
      if (resultSet.next()) {
        guest = Guest(resultSet.getLong("id"), resultSet.getString("name"))
      }
      guest
    } catch {
      case e: Exception => throw e
    } finally {
      if resultSet != null then
        resultSet.close()
      preparedStatement.close()
      connection.close()
    }
  }

  def findAll: List[Guest] = {
    val connection: Connection = ConnectionProvider.openConnection()
    var resultSet: ResultSet = null
    var guests: List[Guest] = List()
    try {
      resultSet = connection.createStatement().executeQuery(s"""SELECT id, name FROM \"guest\"""")
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
      preparedStatement = connection.prepareStatement(s"""UPDATE "guest" SET name = ? WHERE id = ?""")
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
      preparedStatement = connection.prepareStatement(s"""DELETE FROM "guest" WHERE id = ?""")
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
      preparedStatement = connection.prepareStatement(s"""INSERT INTO "guest" (name) VALUES (?) returning id""")
      preparedStatement.setString(1, guest.name)
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
