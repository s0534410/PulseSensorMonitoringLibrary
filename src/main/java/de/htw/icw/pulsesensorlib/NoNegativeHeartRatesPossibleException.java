package de.htw.icw.pulsesensorlib;

/**
 * This Exception is raised if a negative value is tried to be added to the
 * HeartRateMonitor. Negative heart rates are not possible and therefore not
 * allowed.
 * 
 * @author s0534410
 */
public class NoNegativeHeartRatesPossibleException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoNegativeHeartRatesPossibleException() {
		super("Adding negative HeartRates is not allowed");
	}

}