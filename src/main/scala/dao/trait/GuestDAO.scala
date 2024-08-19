package dao.`trait`

import dao.Guest

import java.sql.SQLException

// SOLID -> I -> Interface Segregation Principle -> Many interfaces are better than a general interface
trait GuestDAO extends DAO[Guest] {

  @throws(classOf[SQLException])
  def findById(id: Long): Guest

  @throws(classOf[SQLException])
  def findAll: List[Guest]

  @throws(classOf[SQLException])
  def update(id: Long, guest: Guest): Unit

  @throws(classOf[SQLException])
  def delete(id: Long): Unit

  @throws(classOf[SQLException])
  def insert(guest: Guest): Long
}
