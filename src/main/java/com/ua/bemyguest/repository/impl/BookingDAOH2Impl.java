package com.ua.bemyguest.repository.impl;

import com.ua.bemyguest.exception.BookingIncorrectId;
import com.ua.bemyguest.exception.BookingIncorrectStartDate;
import com.ua.bemyguest.exception.DuplicateBookingException;
import com.ua.bemyguest.model.Booking;
import com.ua.bemyguest.repository.BookingDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ua.bemyguest.repository.impl.ConnectionFactory.*;

public class BookingDAOH2Impl implements BookingDAO {

    private static final String ADD_BOOKING = String.format("INSERT INTO bookings(%s, %s, %s, %s) " +
            "VALUES(?, ?, ?, ?)", Booking.ACCOMMODATION_ID, Booking.GUEST_ID, Booking.START_DATE, Booking.END_DATE);

    private static final String GET_ALL_BOOKINGS = "SELECT * FROM bookings";

    private static final String FIND_ALL_SORTED_BOOKINGS = "SELECT * FROM bookings ORDER BY start_date ASC";

    private static final String UPDATE_BOOKING = String.format("UPDATE bookings SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
            Booking.ACCOMMODATION_ID, Booking.GUEST_ID, Booking.START_DATE, Booking.END_DATE, Booking.ID);

    private static final String DELETE_BOOKING_BY_ID = String.format("DELETE FROM bookings WHERE %s=?", Booking.ID);

    private Connection connection;
    private PreparedStatement pst;
    private ResultSet rs;

    private static BookingDAOH2Impl instance;

    public static BookingDAOH2Impl instance() {
        if (instance == null) {
            instance = new BookingDAOH2Impl();
        }
        return instance;
    }

    @Override
    public Booking findBookingById(int bookingId) throws BookingIncorrectId {
        Booking booking = null;
        for (Booking booking1 : getAllBookings()) {
            if (booking1.getId() == bookingId) {
                booking = booking1;
            }
        }
        if (booking == null) {
            throw new BookingIncorrectId();
        }
        return booking;
    }

    @Override
    public List<Booking> findBookingByStartDate(LocalDate startDate) throws BookingIncorrectStartDate {
        List<Booking> bookingList = new ArrayList<>();
        for (Booking booking1 : getAllBookings()) {
            if (booking1.getStartDate().equals(startDate)) {
                bookingList.add(booking1);
            }
        }
        if (bookingList.size() == 0){
            throw new BookingIncorrectStartDate();
        }
        return bookingList;
    }

    @Override
    public List<Booking> findAllSortedBookings() {
        List<Booking> sortedResult = new ArrayList<>();

        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(FIND_ALL_SORTED_BOOKINGS);
            rs = pst.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt(Booking.ID));
                booking.setAccommodationId(rs.getInt(Booking.ACCOMMODATION_ID));
                booking.setGuestId(rs.getInt(Booking.GUEST_ID));
                booking.setStartDate(rs.getDate(Booking.START_DATE).toLocalDate());
                booking.setEndDate(rs.getDate(Booking.END_DATE).toLocalDate());
                sortedResult.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeResultSet(rs);
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
        return sortedResult;
    }

    @Override
    public void addBooking(Booking booking) throws DuplicateBookingException {
        for (Booking booking1 : getAllBookings()) {
            if (booking.getId() == booking1.getId()) {
                throw new DuplicateBookingException();
            }
        }
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(ADD_BOOKING);
            pst.setInt(1, booking.getAccommodationId());
            pst.setInt(2, booking.getGuestId());
            pst.setObject(3, booking.getStartDate());
            pst.setObject(4, booking.getEndDate());
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> result = new ArrayList<>();
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(GET_ALL_BOOKINGS);
            rs = pst.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt(Booking.ID));
                booking.setAccommodationId(rs.getInt(Booking.ACCOMMODATION_ID));
                booking.setGuestId(rs.getInt(Booking.GUEST_ID));
                booking.setStartDate(rs.getDate(Booking.START_DATE).toLocalDate());
                booking.setEndDate(rs.getDate(Booking.END_DATE).toLocalDate());
                result.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeResultSet(rs);
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
        return result;
    }

    @Override
    public void updateBooking(Booking booking) {
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(UPDATE_BOOKING);
            pst.setInt(1, booking.getAccommodationId());
            pst.setInt(2, booking.getGuestId());
            pst.setObject(3, booking.getStartDate());
            pst.setObject(4, booking.getEndDate());
            pst.setInt(5, booking.getId());
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
    }

    @Override
    public void deleteBookingById(int bookingById) throws BookingIncorrectId {
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(DELETE_BOOKING_BY_ID);
            pst.setInt(1, bookingById);
            int result = pst.executeUpdate();
            if (result == 0) {
                throw new BookingIncorrectId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
    }
}
