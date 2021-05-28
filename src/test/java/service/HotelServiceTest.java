package service;

import model.emun.HotelName;
import service.Interface.HotelService;
import model.Hotel;
import model.emun.Ranks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class HotelServiceTest {

    private static HotelService service;

    @BeforeAll
    public static void setup() {
        service = new HotelServiceImpl();
    }

    @Test
    public void checkPreConfiguredHotelList() {

        List<Hotel> hotels = service.loadPreConfiguredHotelList();

        Assertions.assertEquals(HotelName.PARQUE_DAS_FLORES.getValue(), hotels.get(0).getName());
        Assertions.assertEquals(Ranks.RANK_THREE, hotels.get(0).getRank());
        Assertions.assertEquals(110, hotels.get(0).getPriceToClientOnRegularWeekDays());
        Assertions.assertEquals(80, hotels.get(0).getPriceToClientOnFidelityWeekDays());
        Assertions.assertEquals(90, hotels.get(0).getPriceToClientOnRegularWeekendDays());
        Assertions.assertEquals(80, hotels.get(0).getPriceToClientOnFidelityWeekendDays());

        Assertions.assertEquals(HotelName.JARDIN_BOTANICO.getValue(), hotels.get(1).getName());
        Assertions.assertEquals(Ranks.RANK_FOUR, hotels.get(1).getRank());
        Assertions.assertEquals(160, hotels.get(1).getPriceToClientOnRegularWeekDays());
        Assertions.assertEquals(110, hotels.get(1).getPriceToClientOnFidelityWeekDays());
        Assertions.assertEquals(60, hotels.get(1).getPriceToClientOnRegularWeekendDays());
        Assertions.assertEquals(50, hotels.get(1).getPriceToClientOnFidelityWeekendDays());

        Assertions.assertEquals(HotelName.MAR_ATLANTICO.getValue(), hotels.get(2).getName());
        Assertions.assertEquals(Ranks.RANK_FIVE, hotels.get(2).getRank());
        Assertions.assertEquals(220, hotels.get(2).getPriceToClientOnRegularWeekDays());
        Assertions.assertEquals(100, hotels.get(2).getPriceToClientOnFidelityWeekDays());
        Assertions.assertEquals(150, hotels.get(2).getPriceToClientOnRegularWeekendDays());
        Assertions.assertEquals(40, hotels.get(2).getPriceToClientOnFidelityWeekendDays());
    }
}
