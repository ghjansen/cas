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

package com.ghjansen.cas.control.simulation;

import com.ghjansen.cas.control.exception.SimulationAlreadyActiveException;
import com.ghjansen.cas.control.exception.SimulationBuilderException;
import com.ghjansen.cas.control.task.CompleteTask;
import com.ghjansen.cas.control.task.IterationTask;
import com.ghjansen.cas.control.task.Task;
import com.ghjansen.cas.control.task.UnitTask;
import com.ghjansen.cas.core.simulation.Simulation;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class SimulationController {

	private final Simulation simulation;
	private final Task completeTask;
	private final Task iterationTask;
	private final Task unitTask;

	public SimulationController(SimulationBuilder simulationBuilder) throws SimulationBuilderException {
		this.simulation = simulationBuilder.build();
		this.completeTask = new CompleteTask(this);
		this.iterationTask = new IterationTask(this);
		this.unitTask = new UnitTask(this);
	}

	public void startCompleteTask() throws SimulationAlreadyActiveException {
		if (this.simulation.isActive()) {
			throw new SimulationAlreadyActiveException();
		}
		this.completeTask.run();
	}

	public void startIterationTask() throws SimulationAlreadyActiveException {
		if (this.simulation.isActive()) {
			throw new SimulationAlreadyActiveException();
		}
		this.iterationTask.run();
	}

	public void startUnitTask() throws SimulationAlreadyActiveException {
		if (this.simulation.isActive()) {
			throw new SimulationAlreadyActiveException();
		}
		this.unitTask.run();
	}

	public void stop() {
		this.simulation.setActive(false);
	}

	public Simulation getSimulation() {
		return simulation;
	}

	public void processThreadInterruption(Throwable e) {
		// TODO create observer agent to interact with ui interface when the
		// simulation finishes or something bad happens
	}

}
