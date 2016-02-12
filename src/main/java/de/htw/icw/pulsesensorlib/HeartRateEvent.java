package de.htw.icw.pulsesensorlib;

public interface HeartRateEvent {
	
	void subscribe(HeartRateListener heartRateListener);
	void unsubscribe(HeartRateListener heartRateListener);
	void onUpdate(HeartRateMonitor heartRateMonitor);

}
