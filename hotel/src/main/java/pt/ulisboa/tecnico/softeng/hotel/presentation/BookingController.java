package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.BookingData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData.CopyDepth;

@Controller
@RequestMapping(value = "/hotel/{hotelCode}/room/{number}/bookings")
public class BookingController {

	private static Logger logger = LoggerFactory.getLogger(BookingController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showBookings(Model model, @PathVariable String number, @PathVariable String hotelCode) {
		logger.info("showBookings hotelCode:{}, number:{}", hotelCode, number);
		
		HotelData hotelData = HotelInterface.getHotelDataByCode(hotelCode,CopyDepth.BOOKING);
		RoomData roomData = HotelInterface.getRoomDataByNumber(hotelCode, number);
		
		
		if (hotelData == null) {
			model.addAttribute("error", "Error: it does not exist a hotel with the code " + hotelCode);
			model.addAttribute("hotel", new HotelData());
			model.addAttribute("hotels", HotelInterface.getHotels());
			return "hotels";
		}	else if (roomData == null) {
			model.addAttribute("error", "Error: it does not exist a room with the number " + number);
			model.addAttribute("room", new RoomData());
			model.addAttribute("rooms", hotelData.getRooms());
			return "rooms";
		} else {
			model.addAttribute("booking", new BookingData());
			model.addAttribute("room", roomData);
			model.addAttribute("hotel", hotelData);
			return "bookings";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitBooking(Model model, @PathVariable String hotelCode, @PathVariable String number, @ModelAttribute BookingData bookingData) {
		logger.info("submitbooking hotelCode:{}, number{}, arrival:{}, departure:{}", hotelCode, number, bookingData.getArrival(), bookingData.getDeparture());

		try {
			HotelInterface.createBooking(hotelCode, number, bookingData);
		} catch (HotelException he) {
			model.addAttribute("error", "Error: it was not possible to create the booking ");
			model.addAttribute("booking", bookingData);
			model.addAttribute("room", HotelInterface.getRoomDataByNumber(hotelCode, number));
			model.addAttribute("hotel", HotelInterface.getHotelDataByCode(hotelCode,CopyDepth.BOOKING));
			return "bookings";
		}

		return "redirect:/hotel/" + hotelCode + "/room/" + number + "/bookings";
	}

}
