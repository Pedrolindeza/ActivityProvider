//Hotel Task 5 - stephaneduarte 81186

package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

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
	        19 20 21 22 23 24 25
	Ocupado:   X  X  X  X
	T01:          X  X            - Testa intervalo entre os limites
	T02:    X  X  X               - Testa intervalo com chegada antes e partida dentro dos limites
	T03:             X  X  X      - Testa intervalo com chegada dentro dos limites e partida depois
	T04:    X  X  X  X  X  X      - Testa intervalo com chegada antes e partida depois dos limites
	T05:       X  X  X  X         - Testa intervalo igual aos limites [BUG FIXED]
	T06:       X  X  X  X  X  X   - Teste um limite tendo outro qualquer data*/

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

	@Test
	public void hasNoVacancy05() {
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 23);

		Room room = this.hotel.hasVacancy(Type.SINGLE, arrival, departure);

		Assert.assertNull(room);
	}

	@Test
	public void hasNoVacancy06() {
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 25);

		Room room = this.hotel.hasVacancy(Type.SINGLE, arrival, departure);

		Assert.assertNull(room);
	}

	//Teste entradas null
	@Test(expected = HotelException.class)
	public void nullType(){
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 23);
		Room room = this.hotel.hasVacancy(null, arrival, departure);
	}

	@Test(expected = HotelException.class)
	public void nullArrival(){
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 23);
		Room room = this.hotel.hasVacancy(Type.DOUBLE, null, departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture(){
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 23);
		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival, null);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
