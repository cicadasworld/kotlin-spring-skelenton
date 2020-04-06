package com.jin.theater.controller

import com.jin.theater.domain.Booking
import com.jin.theater.domain.Performance
import com.jin.theater.domain.Seat
import com.jin.theater.repository.PerformanceRepository
import com.jin.theater.repository.SeatRepository
import com.jin.theater.service.BookingService
import com.jin.theater.service.TheaterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class MainController {

    @Autowired
    lateinit var theaterService: TheaterService

    @Autowired
    lateinit var bookingService: BookingService

    @Autowired
    lateinit var seatRepository: SeatRepository

    @Autowired
    lateinit var performanceRepository: PerformanceRepository

    @RequestMapping("")
    fun homePage(): ModelAndView {
        val model = mapOf(
            "bean" to CheckAvailabilityBackingBean(),
            "performances" to performanceRepository.findAll(),
            "seatNums" to 1..36,
            "seatRows" to 'A'..'O'
        )
        return ModelAndView("seatBooking", model)
    }

    @RequestMapping(value = ["checkAvailability"], method = [RequestMethod.POST])
    fun checkAvailability(bean: CheckAvailabilityBackingBean): ModelAndView {
        val selectedSeat: Seat = bookingService.findSeat(bean.selectedSeatNum, bean.selectedSeatRow)!!
        val slectedPerformance = performanceRepository.findById(bean.selectedPerformance!!).get()
        bean.seat = selectedSeat
        bean.performance = slectedPerformance
        val result = bookingService.isSeatFree(selectedSeat, slectedPerformance)
        bean.available = result
        if (!result) {
            bean.booking = bookingService.findBooking(selectedSeat, slectedPerformance)
        }
        val model = mapOf(
            "bean" to bean,
            "performances" to performanceRepository.findAll(),
            "seatNums" to 1..36,
            "seatRows" to 'A'..'O'
        )
        return ModelAndView("seatBooking", model)
    }

    @PostMapping("booking")
    fun bookASeat(bean: CheckAvailabilityBackingBean): ModelAndView {
        val booking = bookingService.reserveSeat(bean.seat!!, bean.performance!!, bean.customerName)
        return ModelAndView("bookingConfirmed", "booking", booking)
    }

//    @RequestMapping("bootstrap")
//    fun createInitialData(): ModelAndView {
//        val seats = theaterService.seats
//        seatRepository.saveAll(seats)
//        return homePage()
//    }
}

class CheckAvailabilityBackingBean() {
    var selectedSeatNum: Int = 1
    var selectedSeatRow: Char = 'A'
    var selectedPerformance: Long? = null
    var customerName: String = ""
    var available: Boolean? = null
    var seat: Seat? = null
    var performance: Performance? = null
    var booking: Booking? = null
}