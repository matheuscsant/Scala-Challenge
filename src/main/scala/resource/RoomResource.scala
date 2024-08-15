package resource

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import dao.*
import service.RoomService
import spray.json.DefaultJsonProtocol.*

object RoomResource {

  private val roomService: RoomService.type = RoomService

  implicit val roomMarshaller: spray.json.RootJsonFormat[Room] = jsonFormat3(Room.apply)
  implicit val roomsMarshaller: spray.json.RootJsonFormat[RoomsList] = jsonFormat1(RoomsList.apply)

  // https://doc.akka.io/docs/akka-http/current/introduction.html
  val allRoutesRooms: Route = {
    path("room" / LongNumber) { id =>
      get {
        val result: Room = roomService.getRoomById(id)
        complete(result)
      }
    }
      ~
      path("room") {
        get {
          complete {
            roomService.getAllRooms
          }
        }
      }

  }

}
