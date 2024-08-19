package dao.`trait`

import dao.Room

import java.sql.SQLException

// SOLID -> I -> Interface Segregation Principle -> Many specific interfaces are better than a general interface
trait RoomDAO extends DAO[Room] {

  @throws(classOf[SQLException])
  def findById(id: Long): Room

  @throws(classOf[SQLException])
  def findAll: List[Room]

  @throws(classOf[SQLException])
  def update(id: Long, room: Room): Unit

  @throws(classOf[SQLException])
  def delete(id: Long): Unit

  @throws(classOf[SQLException])
  def insert(room: Room): Long
}
