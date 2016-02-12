package de.htw.icw.pulsesensorlib;

public interface HeartRateListener {
	
    void onHighPulse(double pulse);
    void onLowPulse(double pulse);    
    void onNoPulse();
    void onDisconnected();
    void onPulseIncreased();
    void onPulseDecreased();
	void onSubscribed();
	void onUnsubscribed();
    
}
