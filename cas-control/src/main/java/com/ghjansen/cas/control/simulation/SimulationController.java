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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ghjansen.cas.control.exception.SimulationAlreadyActiveException;
import com.ghjansen.cas.control.exception.SimulationBuilderException;
import com.ghjansen.cas.control.task.CompleteTask;
import com.ghjansen.cas.control.task.IterationTask;
import com.ghjansen.cas.control.task.Task;
import com.ghjansen.cas.control.task.TaskNotification;
import com.ghjansen.cas.control.task.UnitTask;
import com.ghjansen.cas.core.exception.TimeLimitReachedException;
import com.ghjansen.cas.core.simulation.Simulation;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class SimulationController<I extends Simulation, M extends SimulationBuilder> {

    private final I simulation;
    private final ExecutorService executor;
    private final Task completeTask;
    private final Task iterationTask;
    private final Task unitTask;
    private final TaskNotification notification;

    public SimulationController(M simulationBuilder, TaskNotification notification) throws SimulationBuilderException {
        this.simulation = (I) simulationBuilder.build();
        this.completeTask = new CompleteTask(this);
        this.iterationTask = new IterationTask(this);
        this.unitTask = new UnitTask(this);
        this.notification = notification;
        this.executor = Executors.newCachedThreadPool();
    }

    public void startCompleteTask() throws SimulationAlreadyActiveException {
        if (this.simulation.isActive()) {
            throw new SimulationAlreadyActiveException();
        }
        executor.execute(this.completeTask);
    }

    public void startIterationTask() throws SimulationAlreadyActiveException {
        if (this.simulation.isActive()) {
            throw new SimulationAlreadyActiveException();
        }
        executor.execute(this.iterationTask);
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

    public I getSimulation() {
        return simulation;
    }

    public void processThreadInterruption(Throwable e) {
        if (notification != null) {
            if (e.getClass().equals(TimeLimitReachedException.class)) {
                notification.timeLimitReached(e);
            } else {
                notification.generic(e);
            }
        }
    }

}
