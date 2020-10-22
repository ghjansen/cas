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

import com.ghjansen.cas.control.exception.SimulationAlreadyActiveException;
import com.ghjansen.cas.control.exception.SimulationBuilderException;
import com.ghjansen.cas.control.task.*;
import com.ghjansen.cas.core.exception.TimeLimitReachedException;
import com.ghjansen.cas.core.simulation.Simulation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ExecutorService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimulationControllerTest {

    @Mock private SimulationBuilder simulationBuilder;
    @Mock private Simulation simulation;
    @Mock private ExecutorService executor;
    @Mock private Task completeTask;
    @Mock private Task iterationTask;
    @Mock private Task unitTask;
    @Mock private TaskNotification notification;
    @InjectMocks private DimensionalSimulationController dscm;

    @Before
    public void before() throws SimulationBuilderException {
        Whitebox.setInternalState(dscm, "simulation", simulation);
        Whitebox.setInternalState(dscm, "completeTask", completeTask);
        Whitebox.setInternalState(dscm, "iterationTask", iterationTask);
        Whitebox.setInternalState(dscm, "unitTask", unitTask);
        Whitebox.setInternalState(dscm, "executor", executor);
    }

    @Test
    public void dimensionalSimulationControllerConstructorSuccess() throws SimulationBuilderException {
        given(simulationBuilder.build()).willReturn(simulation);
        DimensionalSimulationController dsci = new DimensionalSimulationController(simulationBuilder, notification);
        Assert.assertNotNull(dsci);
        Assert.assertEquals(simulation, dsci.getSimulation());
    }

    @Test(expected = SimulationBuilderException.class)
    public void dimensionalSimulationControllerConstructorFail() throws SimulationBuilderException {
        given(simulationBuilder.build()).willThrow(SimulationBuilderException.class);
        new DimensionalSimulationController(simulationBuilder, notification);
    }

    @Test
    public void dimensionalSimulationControllerStartCompleteTaskSuccess() throws SimulationAlreadyActiveException {
        given(simulation.isActive()).willReturn(false);
        dscm.startCompleteTask();
        verify(executor).execute(completeTask);
    }

    @Test(expected = SimulationAlreadyActiveException.class)
    public void dimensionalSimulationControllerStartCompleteTaskFail() throws SimulationAlreadyActiveException {
        given(simulation.isActive()).willReturn(true);
        dscm.startCompleteTask();
    }

    @Test
    public void dimensionalSimulationControllerStartIterationTaskSuccess() throws SimulationAlreadyActiveException {
        given(simulation.isActive()).willReturn(false);
        dscm.startIterationTask();
        verify(executor).execute(iterationTask);
    }

    @Test(expected = SimulationAlreadyActiveException.class)
    public void dimensionalSimulationControllerStartIterationTaskFail() throws SimulationAlreadyActiveException {
        given(simulation.isActive()).willReturn(true);
        dscm.startIterationTask();
    }

    @Test
    public void dimensionalSimulationControllerStartUnitTaskSuccess() throws SimulationAlreadyActiveException {
        given(simulation.isActive()).willReturn(false);
        dscm.startUnitTask();
        verify(unitTask).run();
    }

    @Test(expected = SimulationAlreadyActiveException.class)
    public void dimensionalSimulationControllerStartUnitTaskFail() throws SimulationAlreadyActiveException {
        given(simulation.isActive()).willReturn(true);
        dscm.startUnitTask();
    }

    @Test
    public void stop(){
        dscm.stop();
        verify(simulation).setActive(false);
    }

    @Test
    public void processThreadInterruptionGeneric(){
        Exception e = new Exception();
        dscm.processThreadInterruption(e);
        verify(notification).generic(e);
    }

    @Test
    public void processThreadInterruptionTimeLimitReached(){
        TimeLimitReachedException e = new TimeLimitReachedException();
        dscm.processThreadInterruption(e);
        verify(notification).timeLimitReached(e);
    }
}
