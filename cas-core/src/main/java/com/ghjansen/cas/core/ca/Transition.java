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

package com.ghjansen.cas.core.ca;

import com.ghjansen.cas.core.exception.InvalidCombination;
import com.ghjansen.cas.core.exception.InvalidState;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class Transition {

	private Combination combination;
	private State state;

	public Transition(Combination combination, State state) throws InvalidCombination, InvalidState {
		if (combination == null) {
			throw new InvalidCombination();
		}
		if (state == null) {
			throw new InvalidState();
		}
		this.combination = combination;
		this.state = state;
	}

	public Combination getCombination() {
		return combination;
	}

	public State getState() {
		return state;
	}

}
