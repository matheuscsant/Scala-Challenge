package resource.exception

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes.*
import akka.http.scaladsl.server.*
import akka.http.scaladsl.server.Directives.*
import spray.json.DefaultJsonProtocol.*

import java.sql.SQLException
import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}

case class ResourceNotFoundException(private val message: String = "",
                                     private val cause: Throwable = None.orNull)
  extends Exception(message, cause)

case class ValidationException(private val message: String = "",
                               private val cause: Throwable = None.orNull)
  extends Exception(message, cause)

case class StandardResponse(message: String, result: String, moment: String)

object ResourceExceptionHandler {

  implicit val resultMarshaller: spray.json.RootJsonFormat[StandardResponse] = jsonFormat3(StandardResponse.apply)

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC"))

  // https://doc.akka.io/docs/akka-http/current/routing-dsl/exception-handling.html
  val customExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: ResourceNotFoundException =>
      extractUri {
        uri =>
          complete(StatusCodes.NotFound -> StandardResponse(e.getMessage, "Resource not found", formatter.format(Instant.now())))
      }
    case e: ValidationException =>
      extractUri {
        uri =>
          complete(StatusCodes.BadRequest -> StandardResponse(e.getMessage, "Validation failure", formatter.format(Instant.now())))
      }
    case e: NullPointerException =>
      extractUri {
        uri =>
          complete(StatusCodes.InternalServerError -> StandardResponse(e.getMessage, "Server error", formatter.format(Instant.now())))
      }
    case e: SQLException =>
      extractUri {
        uri =>
          complete(StatusCodes.BadRequest -> StandardResponse(e.getMessage, "Database failure", formatter.format(Instant.now())))
      }
  }

  // https://doc.akka.io/docs/akka-http/current/routing-dsl/rejections.html?language=scala#customizing-rejection-handling
  val customRejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handle {
        case MalformedRequestContentRejection(message, cause) =>
          complete(BadRequest -> StandardResponse(message, "API Rejection", formatter.format(Instant.now())))
      }
      .handleAll[MethodRejection] {
        methodRejections =>
          complete(MethodNotAllowed, StandardResponse("Method not allowed", "API Rejection", formatter.format(Instant.now())))
      }
      .handleNotFound {
        complete((NotFound, StandardResponse("No endpoint were found", "API Rejection", formatter.format(Instant.now()))))
      }
      .result()

}
