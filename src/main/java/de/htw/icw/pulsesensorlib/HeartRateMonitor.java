package de.htw.icw.pulsesensorlib;

import java.util.List;

public interface HeartRateMonitor {
	
	long addHeartRate(double heartrate) throws NoNegativeHeartRatesPossibleException;
	long addHeartRate(double heartrate, long timestamp) throws NoNegativeHeartRatesPossibleException;
	
	double getLastHeartRate();
	long getLastTimestamp();
	
	List<Double> getAllMeasuredHeartRates();
	List<Long> getAllMeasuredTimestamps();
	
	List<Double> getMeasuredHeartRatesOfLastSeconds(double interval_in_ms);
	List<Double> getMeasuredHeartRatesOverInterval(double interval_in_ms, long from_timestap);
	List<Double> getMeasuredHeartRatesOverClosedInterval(long from_timestap, long to_timestamp);
	
	List<Long> getMeasuredTimestampsOfLastSeconds(double interval_in_ms);
	List<Long> getMeasuredTimestampsOverInterval(double interval_in_ms, long from_timestap);
	List<Long> getMeasuredTimestampsOverClosedInterval(long from_timestap, long to_timestamp);
	
	List<Double> getLastNumHeartRates(int num);
	List<Long> getLastNumTimestamps(int num);
	
	void subscribe(HeartRateEvent heartRateEvent);
	void unsubscribe(HeartRateEvent heartRateEvent);
}