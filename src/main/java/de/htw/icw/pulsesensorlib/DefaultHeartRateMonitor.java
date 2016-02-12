package de.htw.icw.pulsesensorlib;

import java.util.ArrayList;
import java.util.List;

public class DefaultHeartRateMonitor implements HeartRateMonitor {

	/** list with all measured heartrates */
	private List<Double> heartRates = new ArrayList<Double>();

	/** list with all measured timestamps */
	private List<Long> timestamps = new ArrayList<Long>();

	/** list with all subscribers */
	private List<HeartRateEvent> subscribers = new ArrayList<HeartRateEvent>();

	/**
	 * adds a given hearrate. the timestamp is the time the heartrate was added.
	 * also triggers the listeners.
	 * 
	 * @param heartrate
	 *            the measured heartrate (has to be &gt; 0)
	 * @return the timestamp at the time the heartrate was added
	 * @throws NoNegativeHeartRatesPossibleException
	 *             If a heartrate &lt; 0 is added
	 */
	@Override
	public long addHeartRate(double heartrate)
			throws NoNegativeHeartRatesPossibleException {
		long timestamp = System.currentTimeMillis();

		if (heartrate >= 0) {
			heartRates.add(heartrate);
			timestamps.add(timestamp);
		} else {
			throw new NoNegativeHeartRatesPossibleException();
		}

		notifySubscribers();

		return timestamp;
	}

	/**
	 * adds given heartrate-timestamp tuple and triggers the listeners.
	 * 
	 * @param heartrate
	 *            the measured heartrate (has to be &gt; 0)
	 * @return the timestamp at the time the heartrate was added
	 * @throws NoNegativeHeartRatesPossibleException
	 *             If a heartrate &lt; 0 is added
	 */
	@Override
	public long addHeartRate(double heartrate, long timestamp)
			throws NoNegativeHeartRatesPossibleException {

		if (heartrate >= 0) {
			heartRates.add(heartrate);
			timestamps.add(timestamp);
		} else {
			throw new NoNegativeHeartRatesPossibleException();
		}

		notifySubscribers();

		return timestamp;
	}

	/**
	 * returns the last measured heartrate.
	 * 
	 * @return the last measured heartrate
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if their is no heartrate that could be returned
	 */
	@Override
	public double getLastHeartRate() {
		return heartRates.get(heartRates.size() - 1);
	}

	/**
	 * returns the last measured timestamp.
	 * 
	 * @return the last measured timestamp
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if their is no timestamp that could be returned
	 */
	@Override
	public long getLastTimestamp() {
		return timestamps.get(timestamps.size() - 1);
	}

	/**
	 * returns all measured heartrates as a list
	 * 
	 * @return all measured heartrates
	 */
	@Override
	public List<Double> getAllMeasuredHeartRates() {
		return heartRates;
	}

	/**
	 * returns all measured timestamp as a list
	 * 
	 * @return all measured timestamps
	 */
	@Override
	public List<Long> getAllMeasuredTimestamps() {
		return timestamps;
	}

	/**
	 * creates a sublist containing the heartrates of the last given seconds.
	 * returns the whole list if the given interval is bigger than the possible
	 * 
	 * @param interval_in_ms
	 *            timespan in milliseconds
	 * @return sublist containing the heartrates
	 */
	@Override
	public List<Double> getMeasuredHeartRatesOfLastSeconds(double interval_in_ms) {

		long lastMeasurement = getLastTimestamp();
		long firstMeasurement = timestamps.get(0);
		long hypotheticalBeginTimestamp = lastMeasurement
				- (long) interval_in_ms;

		double possibleRange = (double) (lastMeasurement - firstMeasurement);

		// if expected interval is bigger than actual, return whole list
		if (interval_in_ms > possibleRange) {
			return heartRates;
		}

		int fromIndex = (timestamps.size() - 1);
		while (timestamps.get(fromIndex) > hypotheticalBeginTimestamp
				&& fromIndex > 0) {
			fromIndex--;
		}

		return heartRates.subList(fromIndex, heartRates.size());
	}

	// TODO: implementation needed
	@Override
	public List<Double> getMeasuredHeartRatesOverInterval(
			double interval_in_ms, long from_timestamp) {
		return null;
	}

	/**
	 * creates a sublist containing the timestamps of the last given seconds.
	 * returns the whole list if the given interval is bigger than the possible
	 * 
	 * @param interval_in_ms
	 *            timespan in milliseconds
	 * @return sublist containing the timestamps
	 */
	@Override
	public List<Long> getMeasuredTimestampsOfLastSeconds(double interval_in_ms) {

		long lastMeasurement = getLastTimestamp();
		long firstMeasurement = timestamps.get(0);
		long hypotheticalBeginTimestamp = lastMeasurement
				- (long) interval_in_ms;

		double possibleRange = (double) (lastMeasurement - firstMeasurement);

		// if expected interval is bigger than actual, return whole list
		if (interval_in_ms > possibleRange) {
			return timestamps;
		}

		int fromIndex = (timestamps.size() - 1);
		while (timestamps.get(fromIndex) > hypotheticalBeginTimestamp
				&& fromIndex > 0) {
			fromIndex--;
		}

		return timestamps.subList(fromIndex, timestamps.size());
	}

	/**
	 * returns a sublist with the timestamp of an interval where the length of
	 * the interval and the beginning of the interval are specified by
	 * parameters. an empty list is retunred if the paramters are invalid.
	 * 
	 * @param interval_in_ms
	 *            the length of the desired interval in milliseconds
	 * @param start_timestamp
	 *            the timestamp which defines the beginning of the interval
	 */
	@Override
	public List<Long> getMeasuredTimestampsOverInterval(double interval_in_ms,
			long start_timestamp) {

		long hypotheticalStartTimestamp = start_timestamp;
		long hypotheticalEndTimestamp = start_timestamp + (long) interval_in_ms;

		int startIndex = (timestamps.size() - 1);
		while (timestamps.get(startIndex) > hypotheticalStartTimestamp
				&& startIndex > 0) {
			startIndex--;
		}

		int endIndex = startIndex;
		while (timestamps.get(endIndex) < hypotheticalEndTimestamp
				&& endIndex < (timestamps.size() - 1)) {
			endIndex++;
		}
		if (startIndex == endIndex)
			return new ArrayList<Long>();

		// TODO: What if startIndex > endIndex?

		return timestamps.subList(startIndex, endIndex + 1);
	}

	/**
	 * returns a list with the last X heartrates. where X is specified by the
	 * parameter. if num is bigger as the size of the list the whole list is
	 * returned. if num is smaller/equal zero an empty list is retuned.
	 * 
	 * @param num
	 *            the amount of heartrates to return
	 */
	@Override
	public List<Double> getLastNumHeartRates(int num) {
		if (num >= 0) {
			if (heartRates.size() - num >= 0) {
				int fromIndex = heartRates.size() - num;
				int toIndex = heartRates.size();

				return heartRates.subList(fromIndex, toIndex);
			} else {
				return heartRates;
			}
		} else {
			return new ArrayList<Double>();
		}
	}

	/**
	 * returns a list with the last X timestamps. where X is specified by the
	 * parameter. if num is bigger as the size of the list the whole list is
	 * returned. if num is smaller/equal zero an empty list is retuned.
	 * 
	 * @param num
	 *            the amount of timestamps to return
	 */
	@Override
	public List<Long> getLastNumTimestamps(int num) {
		if (num > 0) {
			if (timestamps.size() - num >= 0) {
				int fromIndex = timestamps.size() - num;
				int toIndex = timestamps.size();

				return timestamps.subList(fromIndex, toIndex);
			} else {
				return timestamps;
			}
		} else {
			return new ArrayList<Long>();
		}
	}

	/**
	 * returns a sublist with heartrates that are within a given interval. if
	 * the interval is invalid an empty list will be returned
	 * 
	 * @param from_timestamp
	 *            the timestamp with the start time of the desired interval
	 * @param to_timestamp
	 *            then end of the desired interval
	 */
	@Override
	public List<Double> getMeasuredHeartRatesOverClosedInterval(
			long from_timestamp, long to_timestamp) {

		int from_index = timestamps.indexOf(from_timestamp);
		int to_index = timestamps.indexOf(to_timestamp);

		if (from_index == -1 || to_index == -1) {
			return new ArrayList<Double>();
		}

		return heartRates.subList(from_index, to_index + 1);
	}

	/**
	 * returns a sublist with timestamps that are within a given interval. if
	 * the interval is invalid an empty list will be returned
	 * 
	 * @param from_timestamp
	 *            the timestamp with the start time of the desired interval
	 * @param to_timestamp
	 *            then end of the desired interval
	 */
	@Override
	public List<Long> getMeasuredTimestampsOverClosedInterval(
			long from_timestamp, long to_timestamp) {

		int from_index = timestamps.indexOf(from_timestamp);
		int to_index = timestamps.indexOf(to_timestamp);

		if (from_index == -1 || to_index == -1) {
			return new ArrayList<Long>();
		}

		return timestamps.subList(from_index, to_index + 1);
	}

	/**
	 * subscribes a new listener to the observer. the function of the observer
	 * is to notify the subscribers that a new heartrate was added to the
	 * monitor
	 * 
	 * @param heartRateEvent
	 *            the listener which will be notified
	 */
	@Override
	public void subscribe(HeartRateEvent heartRateEvent) {
		subscribers.add(heartRateEvent);
	}

	/**
	 * unsubscribes a listener from the observer
	 * 
	 * @param heartRateEvent
	 *            the listener
	 */
	@Override
	public void unsubscribe(HeartRateEvent heartRateEvent) {
		subscribers.remove(heartRateEvent);
	}

	/**
	 * notifies all subscribers that their was an update. an update occurs if a
	 * heartrate was added
	 */
	private void notifySubscribers() {

		for (HeartRateEvent heartRateEvent : subscribers) {
			heartRateEvent.onUpdate(this);
		}

	}
}
