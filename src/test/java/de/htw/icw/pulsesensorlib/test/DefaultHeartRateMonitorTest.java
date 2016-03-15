package de.htw.icw.pulsesensorlib.test;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.htw.icw.pulsesensorlib.HeartRateEvent;
import de.htw.icw.pulsesensorlib.HeartRateMonitor;
import de.htw.icw.pulsesensorlib.NoNegativeHeartRatesPossibleException;
import de.htw.icw.pulsesensorlib.DefaultHeartRateEvent;
import de.htw.icw.pulsesensorlib.DefaultHeartRateMonitor;

@RunWith(MockitoJUnitRunner.class)
public class DefaultHeartRateMonitorTest {

	private static final long DUMMY_LIST_START_TIME = 1446561000000l;

	@Test
	public void addHeartRateShouldAddAHeartRateAndReturnItInGetLastHeartRate() {

		// given
		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();
		double expected = 10;

		// when
		try {
			heartRateMonitor.addHeartRate(expected);
		} catch (NoNegativeHeartRatesPossibleException ex) {
			fail(ex.getMessage());
		}

		// then
		Assert.assertEquals(
				"addHeartRate should add a HeartRate correctly and getLastHeartRate should return it ",
				10, heartRateMonitor.getLastHeartRate(), expected);

	}

	@Test
	public void getLastHeartRateShouldReturnCorrectLastHeartRate() {

		// given
		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();

		// when
		try {
			heartRateMonitor.addHeartRate(10);
			heartRateMonitor.addHeartRate(10.43);
			heartRateMonitor.addHeartRate(0.12);
		} catch (NoNegativeHeartRatesPossibleException ex) {
			fail(ex.getMessage());
		}

		// then
		Assert.assertEquals(
				"the correct last added HeartRate should be returned", 0.12,
				heartRateMonitor.getLastHeartRate(), 0);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void getLastHeartRateShouldThrowExceptionIfThereIsNoHearRate() {

		// given
		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();

		// when
		heartRateMonitor.getLastHeartRate();

		// then
		// throw exception (see above)
	}

	@Test(expected = NoNegativeHeartRatesPossibleException.class)
	public void addingNegativeHeartRatesShouldThrowException()
			throws NoNegativeHeartRatesPossibleException {

		// given
		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();

		// when
		try {
			heartRateMonitor.addHeartRate(-10);
		} catch (NoNegativeHeartRatesPossibleException ex) {
			// then
			throw new NoNegativeHeartRatesPossibleException();
		}
	}

	@Test
	public void aNegativeHeartRateShouldNotBeAddedToTheHeartRateMonitor() {

		// given
		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();
		List<Double> expectedHeartRates = new ArrayList<Double>();
		expectedHeartRates.add(10.0);

		// when
		try {
			heartRateMonitor.addHeartRate(10);
			heartRateMonitor.addHeartRate(-10);
		} catch (NoNegativeHeartRatesPossibleException ex) {
			// Nothing to do there...
		}

		// then
		Assert.assertEquals("only positive heart rates should be added ",
				expectedHeartRates, heartRateMonitor.getAllMeasuredHeartRates());

	}

	@Test
	public void aHeartRateTimestampTupleShouldBeAddedCorrectly() {

		// given
		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();

		double expectedHeartRate = 10;
		long expectedTimestamp = System.currentTimeMillis();

		// when
		try {
			heartRateMonitor.addHeartRate(expectedHeartRate, expectedTimestamp);
		} catch (NoNegativeHeartRatesPossibleException ex) {
			fail(ex.getMessage());
		}

		// then
		Assert.assertEquals("HeartRate should be added correctly",
				expectedHeartRate, heartRateMonitor.getLastHeartRate(), 0);

		Assert.assertEquals("Timestamp should be added correctly",
				expectedTimestamp, heartRateMonitor.getLastTimestamp());
	}

	@Test
	public void addHeartRateWithoutTimestampShouldAddCreateAndAddTimestampCorrectly() {

		// given
		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();
		long timestamp = 0;

		// when
		try {
			timestamp = heartRateMonitor.addHeartRate(1);
		} catch (NoNegativeHeartRatesPossibleException ex) {
			fail(ex.getMessage());
		}

		// then
		Assert.assertEquals(
				"When adding a HeartRate without a Timestamp the method creates a timestamp and adds it, the timestamp can be reached by getLastTimestamp",
				timestamp, heartRateMonitor.getLastTimestamp());

	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void getLastHeaertRateTimestampShouldThrowExceptionIfThereIsNoTimestamp() {

		// given
		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();

		// when
		heartRateMonitor.getLastTimestamp();

		// then
		// throw exception (see above)
	}

	@Test
	public void getAllMeasuredHearRatesShouldReturnAListOfAllHeartRates() {

		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();

		// create list of dummy heartrates and add them to our heartRateMonitor
		List<Double> heartRates = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			heartRates.add((double) i);

			try {
				heartRateMonitor.addHeartRate(heartRates.get(i));
			} catch (NoNegativeHeartRatesPossibleException ex) {
				fail(ex.getMessage());
			}
		}

		Assert.assertEquals(
				"A List with all added HeartRates should be returned",
				heartRates, heartRateMonitor.getAllMeasuredHeartRates());
	}

	@Test
	public void getAllMeasuredTimestampsShouldReturnAListOfAllTimestamps() {

		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();

		// create list of dummy heartrates and add them to our heartRateMonitor
		List<Long> timeStamps = new ArrayList<>();
		for (int i = 0; i < 100; i++) {

			try {
				long currentTimestamp = heartRateMonitor.addHeartRate(i);
				timeStamps.add(currentTimestamp);
			} catch (NoNegativeHeartRatesPossibleException ex) {
				fail(ex.getMessage());
			}
		}

		Assert.assertEquals(
				"A List with all added Timestamps should be returned",
				timeStamps, heartRateMonitor.getAllMeasuredTimestamps());
	}

	@Test
	public void getMeasuredHeartRatesOverIntervalOfFiveSecondsShouldReturnListOfCorrectSize() {

		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);

		// get all heartrates over a interval of 5 seconds we should return a
		// list with 50 values
		Assert.assertEquals(
				"get all heartrates over a interval of 5 seconds should return a list with 51 values",
				51, heartRateMonitor.getMeasuredHeartRatesOfLastSeconds(5000)
						.size());
	}

	@Test
	public void getMeasuredHeartRatesOverAIntervalWhichIsBiggerThanActualMeasuredIntervelShouldReturnWholeList() {

		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);

		Assert.assertEquals(
				"get all heartrates over a interval of 20 seconds where we only recorded the heartrates over the last 5 seconds should return all heartrates we have",
				heartRateMonitor.getAllMeasuredHeartRates(),
				heartRateMonitor.getMeasuredHeartRatesOfLastSeconds(20000));
	}

	@Test
	public void getMeasuredTimestampsOverintervalOfFiveSecondsShouldReturnAListOfCorrectSize() {

		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);

		// get all heartrates over a interval of 5 seconds we should return a
		// list with 50 values
		Assert.assertEquals(
				"get all timestamps over a interval of 5 seconds should return a list with 51 values",
				51, heartRateMonitor.getMeasuredTimestampsOfLastSeconds(5000)
						.size());
	}

	@Test
	public void getMeasuredTimestampsOfLastFiveSecondsShouldReturnCorrectList() {

		HeartRateMonitor actualHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);
		HeartRateMonitor expectedHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME + 5000, 5000, 0);

		List<Long> actualTimestamps = actualHeartRateMonitor
				.getMeasuredTimestampsOfLastSeconds(5000);

		List<Long> expectedTimestamps = expectedHeartRateMonitor
				.getAllMeasuredTimestamps();

		Assert.assertEquals(
				"getMeasuredTimestampsOverinterval of 5 seconds should return the correct List",
				expectedTimestamps, actualTimestamps);

	}

	// TODO: this test is obsolete because in the dummy list all heart rates are
	// constant 10 this test can't really verify that the method under test is
	// correct because only a list with 10's is compared with a list of 10's we
	// do need a dummy list with real measured data
	@Test
	public void getMeasuredHeartRatesOfLastFiveSecondsShouldReturnCorrectList() {

		HeartRateMonitor actualHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);
		HeartRateMonitor expectedHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME + 5000, 5000, 5000);

		List<Double> actualHeartRates = actualHeartRateMonitor
				.getMeasuredHeartRatesOfLastSeconds(5000);

		List<Double> expectedHeartRates = expectedHeartRateMonitor
				.getAllMeasuredHeartRates();

		Assert.assertEquals(
				"getMeasuredHeartRatesOverInterval of 5 seconds should return the correct List",
				expectedHeartRates, actualHeartRates);

	}

	@Test
	public void getMeasuredTimestampsOverIntervalWithASpecificStartTimeShouldReturnCorrectList() {

		HeartRateMonitor actualHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);
		HeartRateMonitor expectedHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME + 2000, 2000, 0);

		List<Long> actualTimestamps = actualHeartRateMonitor
				.getMeasuredTimestampsOverInterval(2000,
						DUMMY_LIST_START_TIME + 2000);

		List<Long> expectedTimestamps = expectedHeartRateMonitor
				.getAllMeasuredTimestamps();

		Assert.assertEquals(
				"getMeasuredTimestampsOverInterval with a specific start time should return correct List",
				expectedTimestamps, actualTimestamps);
	}

	@Test
	public void getMeasuredTimestampsOverIntervalWhereIntervalIsToBigReturnsAllRemainingValues() {

		HeartRateMonitor actualHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 1000, 0);

		List<Long> expectedTimestamps = actualHeartRateMonitor
				.getMeasuredTimestampsOverInterval(500,
						DUMMY_LIST_START_TIME + 500);

		List<Long> actualTimestamps = actualHeartRateMonitor
				.getMeasuredTimestampsOverInterval(1000,
						DUMMY_LIST_START_TIME + 500);

		Assert.assertEquals(
				"getMeasuredTimestampsOverInterval where the interval is bigger than the possible range, all possible values should be returned",
				expectedTimestamps, actualTimestamps);
	}

	@Test
	public void getMeasuredTimestampsOverIntervalWhereStarttimeIsOutOfRangeShouldReturnEmptyList() {

		HeartRateMonitor actualHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 1000, 0);

		List<Long> actualTimestamps = actualHeartRateMonitor
				.getMeasuredTimestampsOverInterval(1000, 0);

		Assert.assertEquals(
				"getMeasuredTimestampsOverInterval should return an empty list if the given timestamp is not in the possible range",
				new ArrayList<Long>(), actualTimestamps);

	}

	@Test
	public void getLastNumHeartRatesShouldReturnListWithCorrectSize() {

		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 1000, 0);

		Assert.assertEquals(
				"getLastNumHeartRates(10) should return a list with the size of 10",
				10, heartRateMonitor.getLastNumHeartRates(10).size());

	}

	@Test
	public void getLastNumHeartRatesShouldReturnCorrectList() {

		final int NUM_OF_ELEMENTS = 10;

		// create dummy list with 100 entries
		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, NUM_OF_ELEMENTS * 1000, 0);

		// get last 10 elements from the heartRateMonitor manually
		int size = heartRateMonitor.getAllMeasuredHeartRates().size();
		List<Double> expectedList = heartRateMonitor.getAllMeasuredHeartRates()
				.subList(size - NUM_OF_ELEMENTS, size);

		Assert.assertEquals(
				"getLastNumHeartRates should return correct list with num last elements",
				expectedList,
				heartRateMonitor.getLastNumHeartRates(NUM_OF_ELEMENTS));

	}

	@Test
	public void getLastNumTimestampsShouldReturnCorrectList() {

		final int NUM_OF_ELEMENTS = 10;

		// create dummy list with 100 entries
		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, NUM_OF_ELEMENTS * 1000, 0);

		// get last 10 elements from the heartRateMonitor manually
		int size = heartRateMonitor.getAllMeasuredTimestamps().size();
		List<Long> expectedList = heartRateMonitor.getAllMeasuredTimestamps()
				.subList(size - NUM_OF_ELEMENTS, size);

		Assert.assertEquals(
				"getLastNumHeartRates should return correct list with num last elements",
				expectedList,
				heartRateMonitor.getLastNumTimestamps(NUM_OF_ELEMENTS));

	}

	@Test
	public void getLastNumTimestampsShouldReturnListWithCorrectSize() {

		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 1000, 0);

		Assert.assertEquals(
				"getLastNumTimestamps(10) should return a list with the size of 10",
				10, heartRateMonitor.getLastNumTimestamps(10).size());

	}

	@Test
	public void getLastNumHeartRatesShouldReturnWholeListIfNumBiggerShanActualListSize() {

		// create a dummy-list with exactly 100 entries
		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 1000, 0);

		Assert.assertEquals(
				"getLastNumHeartRates should return the whole list if Num is bigger than the size of the actual list",
				heartRateMonitor.getAllMeasuredHeartRates(),
				heartRateMonitor.getLastNumHeartRates(1000));

	}

	@Test
	public void getLastNumTimestampsShouldReturnWholeListIfNumBiggerShanActualListSize() {

		// create a dummy-list with exactly 100 entries
		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 1000, 0);

		Assert.assertEquals(
				"getLastNumTimestamps should return the whole list if Num is bigger than the size of the actual list",
				heartRateMonitor.getAllMeasuredTimestamps(),
				heartRateMonitor.getLastNumTimestamps(1000));

	}

	@Test
	public void getLastNumHeartRatesShouldReturnEmptyListIfNumIsSmallerEqualZero() {

		// create a dummy-list with 100 entries
		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 1000, 0);

		Assert.assertEquals(
				"getLastNumHeartRates should return empty list if Num is 0",
				new ArrayList<Double>(),
				heartRateMonitor.getLastNumHeartRates(0));

		Assert.assertEquals(
				"getLastNumHeartRates should return empty list if Num smaller than 0",
				new ArrayList<Double>(),
				heartRateMonitor.getLastNumHeartRates(-1));

	}

	@Test
	public void getLastNumTimestampsShouldReturnEmptyListIfNumIsSmallerEqualZero() {

		// create a dummy-list with 100 entries
		HeartRateMonitor heartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 1000, 0);

		Assert.assertEquals(
				"getLastNumTimestamps should return empty list if Num is 0",
				new ArrayList<Double>(),
				heartRateMonitor.getLastNumTimestamps(0));

		Assert.assertEquals(
				"getLastNumTimestamps should return empty list if Num smaller than 0",
				new ArrayList<Double>(),
				heartRateMonitor.getLastNumTimestamps(-1));
	}

	@Test
	public void getMeasuredHeartRatesOverClosedIntervalShouldReturnCorrectList() {

		HeartRateMonitor actualHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);

		HeartRateMonitor expectedHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME + 2000, 2000, 2000);

		List<Double> actualHeartRates = actualHeartRateMonitor
				.getMeasuredHeartRatesOverClosedInterval(
						DUMMY_LIST_START_TIME + 2000,
						DUMMY_LIST_START_TIME + 4000);

		List<Double> expectedHeartRates = expectedHeartRateMonitor
				.getAllMeasuredHeartRates();

		Assert.assertEquals(
				"getMeasuredHeartRatesOverClosedInterval should return correct list",
				expectedHeartRates, actualHeartRates);

	}

	@Test
	public void getMeasuredHeartRatesOverClosedIntervalWhereGivenParaemeterIsNotInListShouldReturnEmptyList() {

		HeartRateMonitor actualHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);

		List<Double> actualHeartRates = actualHeartRateMonitor
				.getMeasuredHeartRatesOverClosedInterval(0,
						DUMMY_LIST_START_TIME);

		Assert.assertEquals(
				"getMeasuredHeartRatesOverClosedInterval should return an empty list when a parameter is not in the possible range",
				new ArrayList<Double>(), actualHeartRates);
	}

	@Test
	public void getMeasuredHeartRatesOverClodesIntervalWhereFromIsBiggerThanToShouldReturnEmptyList() {

		HeartRateMonitor actualHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);

		List<Double> actualHeartRates = actualHeartRateMonitor
				.getMeasuredHeartRatesOverClosedInterval(
						DUMMY_LIST_START_TIME + 10, DUMMY_LIST_START_TIME);

		Assert.assertEquals(
				"getMeasuredHeartRatesOverClosedInterval should return an empty list if the beginning time is bigger than the end time of the interval",
				new ArrayList<Double>(), actualHeartRates);
	}

	@Test
	public void getMeasuredTimestampsOverClosedIntervalShouldReturnCorrectList() {

		HeartRateMonitor actualHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);

		HeartRateMonitor expectedHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME + 2000, 2000, 0);

		List<Long> actualTimestamps = actualHeartRateMonitor
				.getMeasuredTimestampsOverClosedInterval(
						DUMMY_LIST_START_TIME + 2000,
						DUMMY_LIST_START_TIME + 4000);

		List<Long> expectedTimestamps = expectedHeartRateMonitor
				.getAllMeasuredTimestamps();

		Assert.assertEquals(
				"getMeasuredTimestampsOverClosedInterval should return correct list",
				expectedTimestamps, actualTimestamps);

	}

	@Test
	public void getMeasuredTimestampsOverClosedIntervalWhereGivenParaemeterIsNotInListShouldReturnEmptyList() {

		HeartRateMonitor actualHeartRateMonitor = heartRateMonitorWithFakeValues(
				DUMMY_LIST_START_TIME, 10000, 0);

		List<Long> actualTimestamps = actualHeartRateMonitor
				.getMeasuredTimestampsOverClosedInterval(0,
						DUMMY_LIST_START_TIME);

		Assert.assertEquals(
				"getMeasuredHeartRatesOverClosedInterval should return an empty list when a parameter is not in the possible range",
				new ArrayList<Long>(), actualTimestamps);
	}

	// create dummy list with heartrates of X seconds
	// this list contains 10 heartrates per second
	// at the end we have a list with X*10 values
	private HeartRateMonitor heartRateMonitorWithFakeValues(long startTime,
			int seconds_in_ms, int time_offset) {

		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();

		final long endTime = startTime + (long) seconds_in_ms + time_offset;

		for (int i = time_offset; (startTime + i) <= endTime; i += 100) {
			try {
				heartRateMonitor.addHeartRate(i, startTime + i);
			} catch (NoNegativeHeartRatesPossibleException e) {
				fail(e.getMessage());
			}
		}

		return heartRateMonitor;
	}

	@Mock
	DefaultHeartRateEvent mockHeartRateEvent;

	@Test
	public void onUpdateShouldBeExecutedEveryTimeAValueIsAdded() {

		// given
		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();

		heartRateMonitor.subscribe(mockHeartRateEvent);

		int expectedInvocations = 15;

		// when
		for (int i = 0; i < 15; i++) {
			try {
				heartRateMonitor.addHeartRate(0, 0);
			} catch (NoNegativeHeartRatesPossibleException e) {
				fail(e.getMessage());
			}
		}

		// then
		verify(mockHeartRateEvent, times(expectedInvocations)).onUpdate(
				heartRateMonitor);
	}
}
