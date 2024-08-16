package resource

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import dao.*
import resource.GuestResource.guestMarshaller
import resource.RoomResource.roomMarshaller
import service.ReservationService
import spray.json.DefaultJsonProtocol.*

object ReservationResource {


  private val reservationService: ReservationService.type = ReservationService

  implicit val reservationMarshaller: spray.json.RootJsonFormat[Reservation] = jsonFormat5(Reservation.apply)
  implicit val reservationsMarshaller: spray.json.RootJsonFormat[ReservationsList] = jsonFormat1(ReservationsList.apply)
  implicit val occupancyMarshaller: spray.json.RootJsonFormat[Occupancy] = jsonFormat4(Occupancy.apply)
  implicit val occupancyListMarshaller: spray.json.RootJsonFormat[OccupancyList] = jsonFormat1(OccupancyList.apply)

  val allRoutesReservation: Route = {
    //    path("reservation" / LongNumber) { id =>
    //      get {
    //        val result: Reservation = reservationService.getReservationById(id)
    //        complete(result)
    //      }
    //    }
    //      ~
    //      path("reservation") {
    //        get {
    //          complete {
    //            reservationService.getAllReservation
    //          }
    //        }
    //      }
    //      ~
    path("reservation" slash "occupancy") {
      parameters("date") {
        date =>
          get {
            complete {
              reservationService.getOccupancyByDate(date)
            }
          }
      }

    }
      ~
      //      path("reservation" / LongNumber) { id =>
      //        put {
      //          entity(as[Reservation]) {
      //            reservation =>
      //              complete {
      //                reservationService.updateReservation(id, reservation)
      //                HttpResponse(StatusCodes.OK)
      //              }
      //          }
      //
      //        }
      //      }
      //      ~
      path("reservation") {
        post {
          extractUri {
            uri =>
              entity(as[Reservation]) {
                reservation =>
                  val newId: Long = reservationService.createReservation(reservation)
                  val headers = Location(s"${uri.toString}/$newId")
                  complete(HttpResponse(StatusCodes.Created, headers = List(headers)))
              }
          }

        }
      } ~
      path("reservation" / LongNumber) { id =>
        delete {
          complete {
            reservationService.deleteReservation(id)
            HttpResponse(StatusCodes.NoContent)
          }

        }
      }

  }

}
