package connection

import java.sql.{Connection, DriverManager}

object ConnectionProvider {

  private val url: String = "jdbc:postgresql://localhost:5432/challenge_scala"
  private val user: String = "postgres"
  private val password: String = "123456789"

  // https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
  // Class.forName("org.postgresql.Driver") - Driver JDBC >= JDBC 4.0, don't need
  def openConnection(): Connection = {
    DriverManager.getConnection(url, user, password)
  }

}
