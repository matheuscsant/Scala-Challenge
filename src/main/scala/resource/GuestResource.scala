package resource

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import dao.*
import service.GuestService
import spray.json.DefaultJsonProtocol.*

object GuestResource {

  private val guestService: GuestService.type = GuestService

  implicit val guestMarshaller: spray.json.RootJsonFormat[Guest] = jsonFormat2(Guest.apply)
  implicit val guestsMarshaller: spray.json.RootJsonFormat[GuestsList] = jsonFormat1(GuestsList.apply)

  val allRoutesGuests: Route = {
    path("guest" / LongNumber) { id =>
      get {
        val result: Guest = guestService.getGuestById(id)
        complete(result)
      }
    }
      ~
      path("guest") {
        get {
          complete {
            guestService.getAllGuest
          }
        }
      }
      ~
      path("guest" / LongNumber) { id =>
        put {
          entity(as[Guest]) {
            guest =>
              complete {
                guestService.updateGuest(id, guest)
                HttpResponse(StatusCodes.OK)
              }
          }

        }
      }
      ~
      path("guest") {
        post {
          extractUri {
            uri =>
              entity(as[Guest]) {
                guest =>
                  val newId: Long = guestService.createGuest(guest)
                  val headers = Location(s"${uri.toString}/$newId")
                  complete(HttpResponse(StatusCodes.Created, headers = List(headers)))
              }
          }

        }
      } ~
      path("guest" / LongNumber) { id =>
        delete {
          complete {
            guestService.deleteGuest(id)
            HttpResponse(StatusCodes.NoContent)
          }

        }
      }

  }

}
