package de.htw.icw.pulsesensorlib;

public interface HeartRateListener {
	
    void onHighPulse(double pulse);
    void onLowPulse(double pulse);    
    void onNoPulse();
    void onDisconnected();
    void onPulseIncreased(double startHeartRate, double endHeartRate);
    void onPulseDecreased(double startHeartRate, double endHeartRate);
	void onSubscribed();
	void onUnsubscribed();
    
}
