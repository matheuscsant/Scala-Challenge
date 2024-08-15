package service

import dao.`trait`.Dao
import dao.*
import resource.exception.ResourceNotFound

object GuestService {

  private val dao: Dao[Guest] = GuestDao

  def getGuestById(id: Long): Guest = {
    val result: Guest = dao.findById(id)
    if result == null then
      throw ResourceNotFound("Guest not found")
    else
      result
  }

  def getAllGuest: GuestsList = {
    val result: GuestsList = GuestsList(dao.findAll)
    if result.guestsList.isEmpty then
      throw ResourceNotFound("No guests were found.")
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
