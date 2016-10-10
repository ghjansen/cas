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

package com.ghjansen.cas.control.task;

import com.ghjansen.cas.control.simulation.SimulationController;
import com.ghjansen.cas.core.exception.InvalidCombinationException;
import com.ghjansen.cas.core.exception.InvalidStateException;
import com.ghjansen.cas.core.exception.InvalidTransitionException;
import com.ghjansen.cas.core.exception.TimeLimitReachedException;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class IterationTask extends Task {

	public IterationTask(SimulationController simulationController) {
		super(simulationController);
	}

	public void run() {
		try {
			this.simulationController.getSimulation().simulateIteration();
		} catch (InvalidStateException e) {
			this.simulationController.processThreadInterruption(e);
		} catch (InvalidCombinationException e) {
			this.simulationController.processThreadInterruption(e);
		} catch (InvalidTransitionException e) {
			this.simulationController.processThreadInterruption(e);
		} catch (TimeLimitReachedException e) {
			this.simulationController.processThreadInterruption(e);
		}
	}
}
