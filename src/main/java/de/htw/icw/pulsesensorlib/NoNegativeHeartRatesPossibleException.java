package de.htw.icw.pulsesensorlib;

public class NoNegativeHeartRatesPossibleException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoNegativeHeartRatesPossibleException() {
		super("Adding negative HeartRates is not allowed");
	}
	
}