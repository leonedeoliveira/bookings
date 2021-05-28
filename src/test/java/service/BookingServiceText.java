package service;

import model.BestHotelVO;
import model.Booking;
import model.Hotel;
import model.emun.ClientType;
import model.emun.HotelName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookingServiceText {

    private static BookingServiceImpl bookingService;
    private static HotelServiceImpl hotelService;

    List<Booking> bookings = new ArrayList<>();
    List<Hotel> hotels = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        bookingService = new BookingServiceImpl();
        hotelService = new HotelServiceImpl();
    }

    @Test
    public void checkCorrectScenarioOfTheBestBooking() {

        String bestBooking;

        bestBooking = bookingService.findByHotelWithBetterValueAndRank(ClientType.REGULAR, LocalDate.parse("2020-03-16"), LocalDate.parse("2020-03-17"), LocalDate.parse("2020-03-18"));
        Assertions.assertEquals(HotelName.PARQUE_DAS_FLORES.getValue(), bestBooking);

        bestBooking = bookingService.findByHotelWithBetterValueAndRank(ClientType.REGULAR, LocalDate.parse("2020-03-20"), LocalDate.parse("2020-03-21"), LocalDate.parse("2020-03-22"));
        Assertions.assertEquals(HotelName.JARDIN_BOTANICO.getValue(), bestBooking);

        bestBooking = bookingService.findByHotelWithBetterValueAndRank(ClientType.FIDELITY, LocalDate.parse("2020-03-26"), LocalDate.parse("2020-03-27"), LocalDate.parse("2020-03-28"));
        Assertions.assertEquals(HotelName.MAR_ATLANTICO.getValue(), bestBooking);
    }

    @Test
    public void checkVerifyThatTheReservationWasLoadedWithInformationFromTheHotel() {

        hotels = hotelService.loadPreConfiguredHotelList();
        bookingService.loadHotelInformationOnBooking(bookings, hotels);

        Assertions.assertEquals(0d, bookings.get(0).getTotalPriceToRegularClientOnWeekDays());
        Assertions.assertEquals(0d, bookings.get(0).getTotalPriceToFidelityClientOnyWeekDays());
        Assertions.assertEquals(0d, bookings.get(0).getTotalPriceToRegularClientOnWeekendDays());
        Assertions.assertEquals(0d, bookings.get(0).getTotalPriceToFidelityClientOnWeekendDays());

        Assertions.assertEquals(0d, bookings.get(1).getTotalPriceToRegularClientOnWeekDays());
        Assertions.assertEquals(0d, bookings.get(1).getTotalPriceToFidelityClientOnyWeekDays());
        Assertions.assertEquals(0d, bookings.get(1).getTotalPriceToRegularClientOnWeekendDays());
        Assertions.assertEquals(0d, bookings.get(1).getTotalPriceToFidelityClientOnWeekendDays());

        Assertions.assertEquals(0d, bookings.get(2).getTotalPriceToRegularClientOnWeekDays());
        Assertions.assertEquals(0d, bookings.get(2).getTotalPriceToFidelityClientOnyWeekDays());
        Assertions.assertEquals(0d, bookings.get(2).getTotalPriceToRegularClientOnWeekendDays());
        Assertions.assertEquals(0d, bookings.get(2).getTotalPriceToFidelityClientOnWeekendDays());
    }

    @Test
    public void checkBestHotelWithBestValue() {

        hotels = hotelService.loadPreConfiguredHotelList();
        bookingService.loadHotelInformationOnBooking(bookings, hotels);

        bookings.forEach(x -> {
            x.setTotalPriceToFidelityClientOnWeekendDays(
                    Double.sum(x.getHotel().getPriceToClientOnFidelityWeekendDays(),
                            x.getTotalPriceToFidelityClientOnWeekendDays()));
        });

        var minBooking = bookingService.getBestHotelWithMinValue(bookings);
        Assertions.assertEquals(HotelName.MAR_ATLANTICO.getValue(), minBooking);
    }

    @Test
    public void checkBestHotelWithValueDuplicateWithBestValueAndRank() {

        hotels = hotelService.loadPreConfiguredHotelList();
        var bestHotelList = Arrays.asList(
                new BestHotelVO(hotels.get(0), 100d),
                new BestHotelVO(hotels.get(1), 100d),
                new BestHotelVO(hotels.get(2), 100d));

        var bestHotelValueMinAndRank =
                bookingService.getBestHotelWithValueDuplicateWithBestValueAndRank(bestHotelList, bestHotelList.get(1));

        Assertions.assertEquals(HotelName.MAR_ATLANTICO.getValue(), bestHotelValueMinAndRank);
    }


}
