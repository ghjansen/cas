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

package com.ghjansen.cas.ui.desktop.manager;

import com.ghjansen.cas.control.exception.InvalidSimulationParameterException;
import com.ghjansen.cas.control.exception.SimulationAlreadyActiveException;
import com.ghjansen.cas.control.exception.SimulationBuilderException;
import com.ghjansen.cas.ui.desktop.swing.Main;
import com.ghjansen.cas.unidimensional.control.UnidimensionalInitialConditionParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalLimitsParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalRuleConfigurationParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalRuleTypeParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSequenceParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationBuilder;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationController;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationParameter;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalUniverse;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class EventManager {
	
	private Main main;

	public EventManager(Main main) {
		this.main = main;
	}
	
	public void executeComplete(){
		UnidimensionalRuleTypeParameter ruleType = new UnidimensionalRuleTypeParameter(true);
		UnidimensionalRuleConfigurationParameter ruleConfiguration = new UnidimensionalRuleConfigurationParameter(0,0,0,1,1,1,1,0);
		UnidimensionalLimitsParameter limits = new UnidimensionalLimitsParameter(500, 500);
		UnidimensionalSequenceParameter sequence1 = new UnidimensionalSequenceParameter(1, 224, 0);
		UnidimensionalSequenceParameter sequence2 = new UnidimensionalSequenceParameter(225, 225, 1);
		UnidimensionalSequenceParameter sequence3 = new UnidimensionalSequenceParameter(226, 500, 0);
		UnidimensionalInitialConditionParameter initialCondition = new UnidimensionalInitialConditionParameter(sequence1, sequence2, sequence3);
		try {
			UnidimensionalSimulationParameter simulationParameter = new UnidimensionalSimulationParameter(ruleType, ruleConfiguration, limits, initialCondition);
			UnidimensionalSimulationBuilder simulationBuilder = new UnidimensionalSimulationBuilder(simulationParameter);
			UnidimensionalSimulationController simulationController = new UnidimensionalSimulationController(simulationBuilder);
			main.simulationView.setUniverse((UnidimensionalUniverse) simulationController.getSimulation().getUniverse());
			simulationController.startCompleteTask();
		} catch (InvalidSimulationParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SimulationBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SimulationAlreadyActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
