package com.jin.theater.service

import com.jin.theater.domain.Booking
import com.jin.theater.domain.Performance
import com.jin.theater.domain.Seat
import com.jin.theater.repository.BookingRepository
import com.jin.theater.repository.SeatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookingService() {

    @Autowired
    lateinit var bookingRepository: BookingRepository

    @Autowired
    lateinit var seatRepository: SeatRepository

    fun isSeatFree(seat: Seat, performance: Performance): Boolean {
        val matchedBookings = findBooking(seat, performance)
        return matchedBookings == null
    }

    fun findSeat(seatNum: Int, seatRow: Char): Seat? {
        val allSeats = seatRepository.findAll()
        val foundSeat = allSeats.filter { it.num == seatNum && it.row == seatRow }
        return foundSeat.firstOrNull()
    }

    fun findBooking(seat: Seat, performance: Performance): Booking? {
        val bookings = bookingRepository.findAll()
        val matchedBookings = bookings.filter { it.seat == seat && it.performance == performance }
        return matchedBookings.firstOrNull()
    }

    fun reserveSeat(seat: Seat, performance: Performance, customerName: String): Any {
        val booking = Booking(0, customerName)
        booking.seat = seat
        booking.performance = performance

        bookingRepository.save(booking)
        return booking
    }

}