package org.johnnuy.helium.service;

import java.util.Optional;

import org.johnnuy.helium.service.domain.Pairing;

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
