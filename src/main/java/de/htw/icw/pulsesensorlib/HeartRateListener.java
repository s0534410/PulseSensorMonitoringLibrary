package de.htw.icw.pulsesensorlib;

public interface HeartRateListener {
	
    void onHighPulse();
    void onLowPulse();    
    void onNoPulse();
    void onDisconnected();
    void onPulseIncreased();
    void onPulseDecreased();
	void onSubscribed();
	void onUnsubscribed();
    
}
