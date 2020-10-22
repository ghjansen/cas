/*
 * CAS - Cellular Automata Simulator
 * Copyright (C) 2020  Guilherme Humberto Jansen
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

import com.ghjansen.cas.control.exception.SimulationBuilderException;
import com.ghjansen.cas.control.task.TaskNotification;
import com.ghjansen.cas.core.simulation.Simulation;

public class DimensionalSimulationController extends SimulationController<Simulation,SimulationBuilder> {


    public DimensionalSimulationController(SimulationBuilder simulationBuilder, TaskNotification notification) throws SimulationBuilderException {
        super(simulationBuilder, notification);
    }
}
