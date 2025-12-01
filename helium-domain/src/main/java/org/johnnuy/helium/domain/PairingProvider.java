package org.johnnuy.helium.domain;

import java.util.Optional;

/**
 * Interface used for retrieving pairing information
 */
public interface PairingProvider {

	/**
	 * returns the next pairing if one is available, or empty if none is available
	 * 
	 * @return
	 */
	public Optional<Pairing> nextPairing();
}
