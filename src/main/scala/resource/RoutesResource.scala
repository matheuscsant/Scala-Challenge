package resource

import akka.http.scaladsl.server.*
import akka.http.scaladsl.server.Directives.*
import resource.GuestResource.allRoutesGuests
import resource.RoomResource.allRoutesRooms
import resource.exception.ResourceExceptionHandler.{customExceptionHandler, customRejectionHandler}

object RoutesResource {

  def allRoutesUnified(roomRoutes: Route = allRoutesRooms, guestRoutes: Route = allRoutesGuests): Route = {
    handleRejections(customRejectionHandler) {
      handleExceptions(customExceptionHandler) {
        roomRoutes ~ guestRoutes
      }
    }

  }
}
