package de.htw.icw.pulsesensorlib.test;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.htw.icw.pulsesensorlib.HeartRateEvent;
import de.htw.icw.pulsesensorlib.HeartRateListener;
import de.htw.icw.pulsesensorlib.HeartRateMonitor;
import de.htw.icw.pulsesensorlib.NoNegativeHeartRatesPossibleException;
import de.htw.icw.pulsesensorlib.DefaultHeartRateEvent;
import de.htw.icw.pulsesensorlib.DefaultHeartRateMonitor;

@RunWith(MockitoJUnitRunner.class)
public class DefaultHeartRateEventTest {

	private HeartRateEvent heartRateEvent;
	private static final double HIGH_HEART_RATE = 120;
	private static final double LOW_HEART_RATE = 60;
	private static final double WAITING_TIME_IN_MS = 5000;
	private static final double INCREASING_DECREASING_TRESHOLD_PERCENTAGE = 50;
	private static final double OBSERVATION_TIME = 10000;

	@Mock
	private HeartRateListener mockHeartRateListener;

	@Before
	public void setUp() {
		heartRateEvent = new DefaultHeartRateEvent(HIGH_HEART_RATE,
				LOW_HEART_RATE, WAITING_TIME_IN_MS, OBSERVATION_TIME,
				INCREASING_DECREASING_TRESHOLD_PERCENTAGE);
	}

	@Test
	public void onSubscribeOnUnsubscribeShouldBeExecutedAfterSubscribingUnSubscribing() {

		heartRateEvent.subscribe(mockHeartRateListener);

		verify(mockHeartRateListener).onSubscribed();

		heartRateEvent.unsubscribe(mockHeartRateListener);

		verify(mockHeartRateListener).onUnsubscribed();

	}

	@Test
	public void onNoPulseShouldBeExecutedIfThereIsNoPulseOverASpecificTime() {

		heartRateEvent.subscribe(mockHeartRateListener);

		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();
		heartRateMonitor.subscribe(heartRateEvent);

		for (int i = 0; i <= WAITING_TIME_IN_MS; i += 1000) {

			try {
				heartRateMonitor.addHeartRate(0, i);
			} catch (NoNegativeHeartRatesPossibleException e) {
				fail(e.getMessage());
				e.printStackTrace();
			}
		}

		verify(mockHeartRateListener).onNoPulse();

	}

	@Test
	public void onHighPulseShouldBeExecutedEveryTimeIfThereIsAHighPulse() {

		heartRateEvent.subscribe(mockHeartRateListener);

		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();
		heartRateMonitor.subscribe(heartRateEvent);

		try {
			heartRateMonitor.addHeartRate(HIGH_HEART_RATE);
			heartRateMonitor.addHeartRate(0);
			heartRateMonitor.addHeartRate(HIGH_HEART_RATE);
		} catch (NoNegativeHeartRatesPossibleException e) {
			e.printStackTrace();
		}

		verify(mockHeartRateListener, times(2)).onHighPulse(
				heartRateMonitor.getLastHeartRate());
	}

	@Test
	public void onLowPulseShouldBeExecutedEveryTimeThereIsALowPulse() {

		heartRateEvent.subscribe(mockHeartRateListener);

		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();
		heartRateMonitor.subscribe(heartRateEvent);

		try {
			heartRateMonitor.addHeartRate(LOW_HEART_RATE);
			heartRateMonitor.addHeartRate(LOW_HEART_RATE + 1);
			heartRateMonitor.addHeartRate(LOW_HEART_RATE);
		} catch (NoNegativeHeartRatesPossibleException e) {
			e.printStackTrace();
		}

		verify(mockHeartRateListener, times(2)).onLowPulse(
				heartRateMonitor.getLastHeartRate());

	}

	@Test
	public void onPulseInscreasedShouldBeExecutedOnIncreasingPulse() {

		heartRateEvent.subscribe(mockHeartRateListener);

		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();
		heartRateMonitor.subscribe(heartRateEvent);

		// add heartrates which are slowly increasing from 60 to 90 (= 50%) over
		// an
		// interval of 10 seconds to our monitor
		int start_heartrate = 60;
		int start_time = 0;
		int end_time = 10000;

		for (int i = start_time; i <= end_time; i += 1000) {
			try {
				heartRateMonitor.addHeartRate(start_heartrate, i);
				start_heartrate += 3;
			} catch (NoNegativeHeartRatesPossibleException ex) {
				fail(ex.getMessage());
			}
		}

		// increasing means that the pulse raises a (before defined) percentage
		// in an (also before) defined time.
		verify(mockHeartRateListener).onPulseIncreased();
	}

	@Test
	public void onPulseDecreasedShouldBeExecutedOnDecreasingPulse() {

		heartRateEvent.subscribe(mockHeartRateListener);

		HeartRateMonitor heartRateMonitor = new DefaultHeartRateMonitor();
		heartRateMonitor.subscribe(heartRateEvent);

		// add heartrates which are decreasing from 90 to 60 (= 50%) over an
		// interval of 5 seconds to our monitor
		int start_heartrate = 100;
		int start_time = 0;
		int end_time = 5000;

		for (int i = start_time; i <= end_time; i += 1000) {
			try {
				heartRateMonitor.addHeartRate(start_heartrate, i);
				start_heartrate -= 10;
			} catch (NoNegativeHeartRatesPossibleException ex) {
				fail(ex.getMessage());
			}
		}

		// decreasing means that the pulse falls a (before defined) percentage
		// in an (also before) defined time.
		verify(mockHeartRateListener).onPulseDecreased();
	}

	@Test
	@Ignore
	public void onDisconnectedShouldBeExecutedIfThereWasNoMeasurementOverASpecificTime() {

		HeartRateMonitor dummyHeartRateMonitor = new DefaultHeartRateMonitor();
		dummyHeartRateMonitor.subscribe(heartRateEvent);

		try {
			dummyHeartRateMonitor.addHeartRate(100, 0);
		} catch (NoNegativeHeartRatesPossibleException ex) {
			fail(ex.getMessage());
		}

	}
}