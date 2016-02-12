package de.htw.icw.pulsesensorlib;

import java.util.ArrayList;
import java.util.List;

public class DefaultHeartRateEvent implements HeartRateEvent {

	private double highHeartRate;
	private double lowHeartRate;
	private double waitingTimeInMilliseconds;

	private List<HeartRateListener> subscribers = new ArrayList<HeartRateListener>();

	/**
	 * Parameterized constructor enables to specify at which point the callbacks
	 * are executed.
	 * 
	 * @param highHeartRate
	 *            - sets which heartrate is interpreted as a high heartrate (see {@link HeartRateListener#onHighPulse(double) onHighPulse}
	 * @param lowHeartRate
	 *            - sets which heartrate is interpreted as a low heartrate (see {@link HeartRateListener#onLowPulse(double) onLowPulse}
	 * @param waitingTimeInMilliseconds
	 *            - set how long to wait until the
	 *            {@link HeartRateListener#onNoPulse() onNoPulse} method is
	 *            executed
	 */
	public DefaultHeartRateEvent(double highHeartRate, double lowHeartRate,
			double waitingTimeInMilliseconds) {
		this.highHeartRate = highHeartRate;
		this.lowHeartRate = lowHeartRate;
		this.waitingTimeInMilliseconds = waitingTimeInMilliseconds;
	}

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

		if (heartRateMonitor.getLastHeartRate() <= this.lowHeartRate) {
			notifyOnLowPulse(heartRateMonitor.getLastHeartRate());
		}

	}

	private void testForOnHighPulse(HeartRateMonitor heartRateMonitor) {

		if (heartRateMonitor.getLastHeartRate() >= this.highHeartRate) {
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
				.getMeasuredHeartRatesOfLastSeconds(this.waitingTimeInMilliseconds);
		List<Long> timestamps = heartRateMonitor
				.getMeasuredTimestampsOfLastSeconds(this.waitingTimeInMilliseconds);

		// if (heartRates.size() >= WAITING_TIME_IN_MS) {
		if (timestamps.get(timestamps.size() - 1) - timestamps.get(0) >= this.waitingTimeInMilliseconds) {
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
