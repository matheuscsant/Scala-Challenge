package service

import dao.*
import resource.exception.{ResourceNotFoundException, ValidationException}

import java.sql.Timestamp
import java.time.{LocalDate, LocalDateTime, LocalTime}

object ReservationService {

  private val dao: ReservationDao.type = ReservationDao
  private val guestService: GuestService.type = GuestService
  private val roomService: RoomService.type = RoomService

  def getReservationById(id: Long): Reservation = {
    val result: Reservation = dao.findById(id)
    if result == null then
      throw ResourceNotFoundException("Reservation not found")
    else
      result
  }

  def getAllReservation: ReservationsList = {
    val result: ReservationsList = ReservationsList(dao.findAll)
    if result.reservationsList.isEmpty then
      throw ResourceNotFoundException("No reservations were found.")
    else
      result
  }

  def updateReservation(id: Long, reservation: Reservation): Unit = {
    alreadyExistsReservation(reservation)
    dao.update(id, reservation)
  }

  def deleteReservation(id: Long): Unit = {
    dao.delete(id)
  }

  def getOccupancyByDate(date: String): OccupancyList = {

    val firstMoment: Timestamp = Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN))
    val lastMoment: Timestamp = Timestamp.valueOf(LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX))

    val occupancy: List[Occupancy] = dao.findAllByDate(firstMoment, lastMoment)
    if occupancy.isEmpty then
      throw ResourceNotFoundException(s"No reservation for this date: $date")
    OccupancyList(occupancy)
  }

  def createReservation(reservation: Reservation): Long = {
    alreadyExistsReservation(reservation)
    dao.insert(reservation)
  }

  private def alreadyExistsReservation(reservation: Reservation): Unit = {
    // checking if the information exists
    guestService.getGuestById(reservation.guestId)
    roomService.getRoomById(reservation.roomId)

    val checkIn: Timestamp = Timestamp.valueOf(reservation.checkIn)
    val checkOut: Timestamp = Timestamp.valueOf(reservation.checkOut)

    // adding four hours to clean up
    val checkInWithFourHours: Timestamp = Timestamp.valueOf(checkIn.toLocalDateTime.minusHours(4))
    val checkOutWithFourHours: Timestamp = Timestamp.valueOf(checkOut.toLocalDateTime.plusHours(4))

    // validating moments
    if checkIn.after(checkOut) then
      throw ValidationException("Check-In date is after Check-Out date")

    if checkIn.equals(checkOut) then
      throw ValidationException("Check-In date is the same at Check-Out date")

    // checking if there is a reservation for these moments
    val existsReservation: Reservation = dao.findByRoomIdCheckInCheckOut(reservation.roomId, checkInWithFourHours, checkOutWithFourHours)

    if (existsReservation != null) {
      val existsCheckIn: Timestamp = Timestamp.valueOf(Timestamp.valueOf(existsReservation.checkIn).toLocalDateTime.minusHours(4))
      val existsCheckOut: Timestamp = Timestamp.valueOf(Timestamp.valueOf(existsReservation.checkOut).toLocalDateTime.plusHours(4))
      if checkIn.after(existsCheckIn) && checkIn.before(existsCheckOut) then
        throw ValidationException(s"There is already a reservation for this check-in, the next reservation, is after to: $existsCheckOut")
      else
        throw ValidationException(s"There is already a reservation for this check-Out, the possible check-Out, is before to: $existsCheckIn. Or " +
          s"the next reservation, is after to: $existsCheckOut")
    }
  }
}
