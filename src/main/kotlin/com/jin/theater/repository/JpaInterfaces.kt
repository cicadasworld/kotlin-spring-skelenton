package com.jin.theater.repository

import com.jin.theater.domain.Booking
import com.jin.theater.domain.Performance
import com.jin.theater.domain.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatRepository : JpaRepository<Seat, Long>

interface PerformanceRepository : JpaRepository<Performance, Long>

interface BookingRepository : JpaRepository<Booking, Long>