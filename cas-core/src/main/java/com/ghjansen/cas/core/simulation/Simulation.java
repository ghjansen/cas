/*
 * CAS - Cellular Automata Simulator
 * Copyright (C) 2016  Guilherme Humberto Jansen
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ghjansen.cas.core.simulation;

import com.ghjansen.cas.core.ca.CellularAutomaton;
import com.ghjansen.cas.core.exception.InvalidCellularAutomataException;
import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;
import com.ghjansen.cas.core.exception.InvalidUniverseException;
import com.ghjansen.cas.core.exception.TimeLimitReachedException;
import com.ghjansen.cas.core.physics.Universe;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class Simulation {

	private Universe universe;
	private CellularAutomaton ca;
	private boolean active;

	protected Simulation(Universe universe, CellularAutomaton ca)
			throws InvalidUniverseException, InvalidCellularAutomataException {
		if (universe == null) {
			throw new InvalidUniverseException();
		}
		if (ca == null) {
			throw new InvalidCellularAutomataException();
		}
		this.universe = universe;
		this.ca = ca;
		this.active = false;
	}

	public void simulateComplete() throws InvalidStateException, InvalidCombinationException, InvalidTransitionException,
			TimeLimitReachedException {
		this.active = true;
		while (active) {
			executeRule();
		}
	}

	public void simulateIteration() throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, TimeLimitReachedException {
		this.active = true;
		final int referenceTime = this.universe.getTime().getAbsolute();
		while (active) {
			if (referenceTime == this.universe.getTime().getAbsolute()) {
				executeRule();
			} else {
				this.active = false;
			}
		}
	}

	public void simulateUnit() throws InvalidStateException, InvalidCombinationException, InvalidTransitionException,
			TimeLimitReachedException {
		this.active = true;
		executeRule();
		this.active = false;
	}

	private void executeRule() throws InvalidStateException, InvalidCombinationException, InvalidTransitionException,
			TimeLimitReachedException {
		try {
			this.ca.executeRule(this.universe.getSpace(), this.universe.getTime());
		} catch (InvalidStateException e) {
			this.active = false;
			throw e;
		} catch (InvalidCombinationException e) {
			this.active = false;
			throw e;
		} catch (InvalidTransitionException e) {
			this.active = false;
			throw e;
		} catch (TimeLimitReachedException e) {
			this.active = false;
			throw e;
		}
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Universe getUniverse() {
		return this.universe;
	}

	public CellularAutomaton getCellularAutomata() {
		return this.ca;
	}

}
