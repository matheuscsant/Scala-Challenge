package service

import dao.*
import dao.`trait`.GuestDAO
import resource.exception.ResourceNotFoundException

object GuestService {

  // SOLID -> D -> Dependency Inversion Principle -> Depend on abstractions, not concrete implementations
  private val dao: GuestDAO = GuestDao

  def getGuestById(id: Long): Guest = {
    val result: Guest = dao.findById(id)
    if result == null then
      throw ResourceNotFoundException("Guest not found")
    else
      result
  }

  def getAllGuest: GuestsList = {
    val result: GuestsList = GuestsList(dao.findAll)
    if result.guestsList.isEmpty then
      throw ResourceNotFoundException("No guests were found.")
    else
      result
  }

  def updateGuest(id: Long, guest: Guest): Unit = {
    dao.update(id, guest)
  }

  def deleteGuest(id: Long): Unit = {
    dao.delete(id)
  }

  def createGuest(guest: Guest): Long = {
    dao.insert(guest)
  }

}
