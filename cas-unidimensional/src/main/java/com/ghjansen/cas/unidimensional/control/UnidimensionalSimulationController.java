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

package com.ghjansen.cas.unidimensional.control;

import com.ghjansen.cas.control.exception.SimulationBuilderException;
import com.ghjansen.cas.control.simulation.SimulationController;
import com.ghjansen.cas.control.task.TaskNotification;
import com.ghjansen.cas.unidimensional.simulation.UnidimensionalSimulation;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalSimulationController extends SimulationController<UnidimensionalSimulation, UnidimensionalSimulationBuilder> {

    public UnidimensionalSimulationController(UnidimensionalSimulationBuilder simulationBuilder, TaskNotification notification) throws SimulationBuilderException {
        super(simulationBuilder, notification);
    }

}
