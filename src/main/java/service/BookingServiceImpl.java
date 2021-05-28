package service;

import service.Interface.BookingService;
import service.Interface.HotelService;
import Util.DataUtils;
import model.BestHotelVO;
import model.Booking;
import model.Hotel;
import model.emun.ClientType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {

    HotelService service = new HotelServiceImpl();

    public String findByHotelWithBetterValueAndRank(ClientType clientType, LocalDate... dates) {
        List<Booking> bookings = new ArrayList<>();

        List<Hotel> hotels = service.loadPreConfiguredHotelList();

        this.loadHotelInformationOnBooking(bookings, hotels);

        Arrays.asList(dates).forEach(dateBooking -> {

            bookings.forEach(booking -> {
                if (DataUtils.isWeekend(dateBooking)) {
                    if (ClientType.REGULAR.equals(clientType)) {
                        booking.setTotalPriceToRegularClientOnWeekendDays(Double.sum(booking.getTotalPriceToRegularClientOnWeekendDays(), booking.getHotel().getPriceToClientOnRegularWeekendDays()));
                    } else {
                        booking.setTotalPriceToFidelityClientOnWeekendDays(Double.sum(booking.getTotalPriceToFidelityClientOnWeekendDays(), booking.getHotel().getPriceToClientOnFidelityWeekendDays()));
                    }
                } else {
                    if (ClientType.REGULAR.equals(clientType)) {
                        booking.setTotalPriceToRegularClientOnWeekDays(Double.sum(booking.getTotalPriceToRegularClientOnWeekDays(), booking.getHotel().getPriceToClientOnRegularWeekDays()));
                    } else {
                        booking.setTotalPriceToFidelityClientOnyWeekDays(Double.sum(booking.getTotalPriceToFidelityClientOnyWeekDays(), booking.getHotel().getPriceToClientOnFidelityWeekDays()));
                    }
                }
            });
        });

        return this.getBestHotelWithMinValue(bookings);
    }

    protected void loadHotelInformationOnBooking(List<Booking> bookings, List<Hotel> hotels) {
        hotels.forEach(hotel -> {
            bookings.add(new Booking(hotel, 0d, 0d, 0d, 0d));
        });
    }

    protected String getBestHotelWithMinValue(List<Booking> bookings) {

        var totalValuesPerHotels = bookings.stream()
                .map(BestHotelVO::new)
                .collect(Collectors.toList());

        var lowestHotelValue = totalValuesPerHotels.stream()
                .min(Comparator.comparing(BestHotelVO::getPriceToClientTotalDays)).get();

        return getBestHotelWithValueDuplicateWithBestValueAndRank(totalValuesPerHotels, lowestHotelValue);

    }

    protected String getBestHotelWithValueDuplicateWithBestValueAndRank(List<BestHotelVO> totalValuesPerHotels, BestHotelVO bestMinHotelValue) {

        var hotelsWithTheSameValues = totalValuesPerHotels.stream()
                .filter(hotelsWithTheSameValue ->
                        !hotelsWithTheSameValue.equals(bestMinHotelValue) &&
                                hotelsWithTheSameValue.getPriceToClientTotalDays().equals(bestMinHotelValue.getPriceToClientTotalDays()))
                .collect(Collectors.toList());

        if (hotelsWithTheSameValues != null && !hotelsWithTheSameValues.isEmpty()) {
            hotelsWithTheSameValues.add(bestMinHotelValue);
            var highestRankedHotel = hotelsWithTheSameValues
                    .stream()
                    .max(Comparator.comparing(x -> x.getHotel().getRank()))
                    .get();

            return highestRankedHotel.getHotel().getName();
        }
        return bestMinHotelValue.getHotel().getName();
    }
}
