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
import com.ghjansen.cas.core.exception.InvalidRuleException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;
import com.ghjansen.cas.core.exception.TimeLimitReachedException;
import com.ghjansen.cas.core.physics.Space;
import com.ghjansen.cas.core.physics.Time;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class CellularAutomaton<R extends Rule, S extends Space, T extends Time> {

	private R rule;

	protected CellularAutomaton(R rule) throws InvalidRuleException {
		if (rule == null) {
			throw new InvalidRuleException();
		}
		this.rule = rule;
	}

	public void executeRule(S space, T time) throws InvalidStateException, InvalidCombinationException,
			InvalidTransitionException, TimeLimitReachedException {
		Combination combination = space.getCombination(time);
		Transition transition = this.rule.getTransition(combination);
		space.setState(time, transition);
		time.increase();
	}

	public R getRule() {
		return this.rule;
	}

}
