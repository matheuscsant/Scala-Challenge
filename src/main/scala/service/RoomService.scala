package service

import dao.`trait`.Dao
import dao.{Room, RoomDao, RoomsList}
import resource.exception.ResourceNotFound

object RoomService {

  private val dao: Dao[Room] = RoomDao

  def getRoomById(id: Long): Room = {
    val result: Room = dao.findById(id)
    if result == null then
      throw ResourceNotFound("Room not found")
    else
      result
  }

  def getAllRooms: RoomsList = {
    val result: RoomsList = RoomsList(dao.findAll)
    if result.RoomsList.isEmpty then
      throw ResourceNotFound("No rooms were found.")
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
