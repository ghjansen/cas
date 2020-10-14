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

import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidStateException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class Transition<O extends Combination, A extends State> {

	private O combination;
	private A state;

	protected Transition(O combination, A state) throws InvalidCombinationException, InvalidStateException {
		if (combination == null) {
			throw new InvalidCombinationException();
		}
		if (state == null) {
			throw new InvalidStateException();
		}
		this.combination = combination;
		this.state = state;
	}

	public O getCombination() {
		return this.combination;
	}

	public A getState() {
		return this.state;
	}
	
}
