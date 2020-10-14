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

package com.ghjansen.cas.core.physics;

import com.ghjansen.cas.core.ca.State;
import com.ghjansen.cas.core.ca.Transition;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class Cell<A extends State,N extends Transition> {

	protected A state;
	protected N transition;

	protected Cell(N transition) throws InvalidTransitionException {
		if (transition == null) {
			throw new InvalidTransitionException();
		}
		this.transition = transition;
		this.state = (A) transition.getState();
	}

	protected Cell(A state) throws InvalidStateException {
		if (state == null) {
			throw new InvalidStateException();
		}
		this.state = state;
	}

	public A getState() {
		return state;
	}

	public N getTransition() {
		return this.transition;
	}

}
