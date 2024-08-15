package service

import dao.{Room, RoomDao, RoomsList}
import resource.exception.ResourceNotFound

object RoomService {

  private val dao: RoomDao.type = RoomDao

  def getRoomById(id: Long): Room = {
    val result: Room = dao.findById(id)
    if result == null then
      throw ResourceNotFound("Room not found")
    else
      result
  }

  def getAllRooms: RoomsList = {
    val result: RoomsList = dao.findAll
    if result.RoomsList.isEmpty then
      throw ResourceNotFound("No rooms were found.")
    else
      result
  }

}
