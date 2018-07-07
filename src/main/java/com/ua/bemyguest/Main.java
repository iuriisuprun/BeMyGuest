package com.ua.bemyguest;

import com.ua.bemyguest.exception.DuplicateGuestException;
import com.ua.bemyguest.model.Guest;
import com.ua.bemyguest.model.Host;
import com.ua.bemyguest.repository.AccommodationDAO;
import com.ua.bemyguest.repository.GuestDAO;
import com.ua.bemyguest.repository.HostDAO;
import com.ua.bemyguest.repository.impl.AccommodationDAOH2Impl;
import com.ua.bemyguest.model.Accommodation;
import com.ua.bemyguest.model.AccommodationType;
import com.ua.bemyguest.repository.impl.GuestDAOH2Impl;
import com.ua.bemyguest.repository.impl.HostDAOH2Impl;
import com.ua.bemyguest.service.Init;

public class Main {

    public static void main(String[] args) {
        /*Guest guest = Guest.builder()
                    .preferredLanguage("English")
                    .build();
        System.out.println(guest);

        Host host = Host.newBuilder()
                    .setCountry("Netherlands")
                    .setFirstName("Bram")
                    .build();
        System.out.println(host);

        Accommodation accommodation = Accommodation.newBuilder()
                    .setAccommodationType(AccommodationType.APARTMENT)
                    .build();
        System.out.println(accommodation);*/

        /*HostDAO hostDAO = new HostDAOH2Impl();
        hostDAO.addHost(Host.newBuilder()
                .setFirstName("David")
                .setLastName("Schwimmer")
                .setEmail("dav@gmail.com")
                .setPhoneNumber("0983688221")
                .setCountry("Belgium")
                .setBirthDate(LocalDate.of(1982, Month.JANUARY, 1))
                .setLocality("Antwerp")
                .setJoinDate(LocalDate.of(2015, Month.JULY, 21))
                .setWork("programmer")
                .build());*/

        Init init = new Init();

        AccommodationDAO accommodationDAO = new AccommodationDAOH2Impl();
        /*accommodationDAO.addAccommodation(Accommodation.newBuilder()
                .setTitle("Cozy apartment")
                .setAccommodationType(AccommodationType.APARTMENT)
                .setCountry("Italy")
                .build());
        System.out.println(accommodationDAO);*/

        /*Host host = Host.newBuilder()
                .setFirstName("Kevin")
                .build();

        accommodationDAO.addAccommodation(Accommodation.newBuilder()
                .setTitle("Light room in Barcelona")
                .setHost(host)
                .build());

        System.out.println(accommodationDAO.getAllAccommodations());*/

        GuestDAO guestDAO = new GuestDAOH2Impl();
        try {
            guestDAO.addGuest(Guest.builder()
                    .preferredLanguage("Chinese")
                    .build());
        } catch (DuplicateGuestException e) {
            e.printStackTrace();
        }

        System.out.println(guestDAO.getAllGuests());

        HostDAO hostDAO = new HostDAOH2Impl();
        Host host = Host.builder()
                .firstName("Levis")
                .work("University: lecturer")
                .build();

        Accommodation accommodation = Accommodation.newBuilder()
                .setCountry("England")
                .setAccommodationType(AccommodationType.HOSTEL)
                .setHost(host)
                .build();
    }
}