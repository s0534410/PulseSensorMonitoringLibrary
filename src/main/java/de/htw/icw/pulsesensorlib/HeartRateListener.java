package de.htw.icw.pulsesensorlib;

public interface HeartRateListener {
	
    void onHighPulse(double pulse);
    void onLowPulse(double pulse);    
    void onNoPulse();
    void onDisconnected();
	void onPulseIncreased(double startHeartRate, double endHeartRate,
			long startTimestamp, long endTimestamp, double increaseingPercantage);
	void onPulseDecreased(double startHeartRate, double endHeartRate,
			long startTimestamp, long endTimestamp, double decreasingPercentage);
	void onSubscribed();
	void onUnsubscribed();    
}
