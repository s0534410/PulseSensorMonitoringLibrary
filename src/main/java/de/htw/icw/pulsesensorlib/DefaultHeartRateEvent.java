package de.htw.icw.pulsesensorlib;

import java.util.ArrayList;
import java.util.List;

public class DefaultHeartRateEvent implements HeartRateEvent {

	private static final double HIGH_HEART_RATE = 300;
	private static final double LOW_HEART_RATE = 10;
	private static final double WAITING_TIME_IN_MS = 5000;

	private List<HeartRateListener> subscribers = new ArrayList<HeartRateListener>();

	@Override
	public void subscribe(HeartRateListener heartRateListener) {
		subscribers.add(heartRateListener);
		heartRateListener.onSubscribed();
	}

	@Override
	public void unsubscribe(HeartRateListener heartRateListener) {
		subscribers.remove(heartRateListener);
		heartRateListener.onUnsubscribed();
	}

	@Override
	public void onUpdate(HeartRateMonitor heartRateMonitor) {
		testForOccuredEvent(heartRateMonitor);
	}

	private void testForOccuredEvent(HeartRateMonitor heartRateMonitor) {
		testForOnHighPulse(heartRateMonitor);
		testForOnLowPulse(heartRateMonitor);
		testForOnNoPulse(heartRateMonitor); 
	}

	private void testForOnLowPulse(HeartRateMonitor heartRateMonitor) {

		if (heartRateMonitor.getLastHeartRate() <= LOW_HEART_RATE) {
			notifyOnLowPulse(heartRateMonitor.getLastHeartRate());
		}

	}

	private void testForOnHighPulse(HeartRateMonitor heartRateMonitor) {

		if (heartRateMonitor.getLastHeartRate() >= HIGH_HEART_RATE) {
			notifyOnHighPulse(heartRateMonitor.getLastHeartRate());
		}

	}

	private void notifyOnHighPulse(double pulse) {
		for (HeartRateListener heartRateListener : subscribers) {
			heartRateListener.onHighPulse(pulse);
		}
	}

	private void notifyOnLowPulse(double pulse) {
		for (HeartRateListener heartRateListener : subscribers) {
			heartRateListener.onLowPulse(pulse);
		}
	}

	private void testForOnNoPulse(HeartRateMonitor heartRateMonitor) {

		List<Double> heartRates = heartRateMonitor
				.getMeasuredHeartRatesOfLastSeconds(WAITING_TIME_IN_MS);
		List<Long> timestamps = heartRateMonitor
				.getMeasuredTimestampsOfLastSeconds(WAITING_TIME_IN_MS);

		// if (heartRates.size() >= WAITING_TIME_IN_MS) {
		if (timestamps.get(timestamps.size() - 1) - timestamps.get(0) >= WAITING_TIME_IN_MS) {
			for (int i = 0; i < heartRates.size(); i++) {
				if (heartRates.get(i) != 0) {
					return;
				}
			}
			notifyOnNoPulse();
		}
	}

	private void notifyOnNoPulse() {
		for (HeartRateListener heartRateListener : subscribers) {
			heartRateListener.onNoPulse();
		}
	}

}
