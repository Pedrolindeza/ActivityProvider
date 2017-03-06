package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class HotelHasVacancyMethodTest {
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		new Room(this.hotel, "01", Type.DOUBLE);
		Room room = new Room(this.hotel, "02", Type.SINGLE);

		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 23);
		room.reserve(Type.SINGLE, arrival, departure);
	}

	//Disponibilidade quarto duplo e testa a reserva de quartos de tipos diferentes em datas conflituosas
	@Test
	public void hasVacancy() {
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 23);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);

		Assert.assertEquals("01", room.getNumber());
	}

	/*Testa indisponibilidade:
	        19 20 21 22 23 24
	Ocupado:   X  X  X  X
	T01:          X  X
	T02:    X  X  X
	T03:             X  X  X
	T04:    X  X  X  X  X  X */

	@Test
	public void hasNoVacancy01() {
		LocalDate arrival = new LocalDate(2016, 12, 21);
		LocalDate departure = new LocalDate(2016, 12, 22);

		Room room = this.hotel.hasVacancy(Type.SINGLE, arrival, departure);

		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy02() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Room room = this.hotel.hasVacancy(Type.SINGLE, arrival, departure);

		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy03() {
		LocalDate arrival = new LocalDate(2016, 12, 22);
		LocalDate departure = new LocalDate(2016, 12, 24);

		Room room = this.hotel.hasVacancy(Type.SINGLE, arrival, departure);

		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy04() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);

		Room room = this.hotel.hasVacancy(Type.SINGLE, arrival, departure);

		Assert.assertNull(room);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
