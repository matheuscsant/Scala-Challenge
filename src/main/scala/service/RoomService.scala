package service

import dao.`trait`.RoomDAO
import dao.{Room, RoomDao, RoomsList}
import resource.exception.ResourceNotFoundException

object RoomService {

  // SOLID -> D -> Dependency Inversion Principle -> Depend on abstractions, not concrete implementations
  private val dao: RoomDAO = RoomDao

  def getRoomById(id: Long): Room = {
    val result: Room = dao.findById(id)
    if result == null then
      throw ResourceNotFoundException("Room not found")
    else
      result
  }

  def getAllRooms: RoomsList = {
    val result: RoomsList = RoomsList(dao.findAll)
    if result.roomsList.isEmpty then
      throw ResourceNotFoundException("No rooms were found.")
    else
      result
  }

  def updateRoom(id: Long, room: Room): Unit = {
    dao.update(id, room)
  }

  def deleteRoom(id: Long): Unit = {
    dao.delete(id)
  }

  def createRoom(room: Room): Long = {
    dao.insert(room)
  }

}
