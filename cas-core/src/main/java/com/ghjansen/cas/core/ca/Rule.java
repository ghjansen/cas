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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class Rule {

	private Map<State, List<Transition>> transitions;

	protected Rule(Transition... transitions) throws InvalidTransitionException {
		if (transitions == null) {
			throw new InvalidTransitionException();
		}
		initialize(transitions);
	}

	private void initialize(Transition... transitions) {
		this.transitions = new HashMap<State, List<Transition>>();
		for (Transition t : transitions) {
			State s = t.getCombination().getReferenceState();
			if (this.transitions.containsKey(s)) {
				this.transitions.get(s).add(t);
			} else {
				ArrayList<Transition> l = new ArrayList<Transition>();
				l.add(t);
				this.transitions.put(s, l);
			}
		}
	}

	public Transition getTransition(Combination combination) throws InvalidStateException, InvalidCombinationException {
		State s = combination.getReferenceState();
		List<Transition> l = this.transitions.get(s);
		if (l == null) {
			throw new InvalidStateException();
		}
		for (Transition t : l) {
			boolean found = true;
			for (int i = 0; i < combination.getNeighborhood().size(); i++) {
				State neighborCombination = combination.getNeighborhood().get(i);
				State neighborTransition = t.getCombination().getNeighborhood().get(i);
				if (!neighborCombination.equals(neighborTransition)) {
					found = false;
					break;
				}
			}
			if (found) {
				return t;
			}
		}
		throw new InvalidCombinationException();
	}
	
	public Map<State, List<Transition>> getTransitions(){
		return this.transitions;
	}

}
