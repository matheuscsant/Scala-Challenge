package dao.`trait`

import dao.Room

// SOLID -> I -> Interface Segregation Principle -> Many interfaces are better than a general interface
trait RoomDAO extends DAO[Room] {

  def findById(id: Long): Room

  def findAll: List[Room]

  def update(id: Long, room: Room): Unit

  def delete(id: Long): Unit

  def insert(room: Room): Long
}
