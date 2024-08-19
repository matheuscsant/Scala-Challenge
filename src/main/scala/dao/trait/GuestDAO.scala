package dao.`trait`

import dao.Guest

// SOLID -> I -> Interface Segregation Principle -> Many interfaces are better than a general interface
trait GuestDAO extends DAO[Guest] {

  def findById(id: Long): Guest

  def findAll: List[Guest]

  def update(id: Long, guest: Guest): Unit

  def delete(id: Long): Unit

  def insert(guest: Guest): Long
}
