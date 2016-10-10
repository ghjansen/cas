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

import com.ghjansen.cas.control.exception.InvalidSimulationParameterException;
import com.ghjansen.cas.control.parameter.InitialConditionParameter;
import com.ghjansen.cas.control.parameter.LimitsParameter;
import com.ghjansen.cas.control.parameter.RuleConfigurationParameter;
import com.ghjansen.cas.control.parameter.RuleTypeParameter;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public abstract class SimulationParameter {

	private final RuleTypeParameter ruleTypeParameter;
	private final RuleConfigurationParameter ruleConfigurationParameter;
	private final LimitsParameter limitsParameter;
	private final InitialConditionParameter initialConditionParameter;

	public SimulationParameter(RuleTypeParameter ruleTypeParameter,
			RuleConfigurationParameter ruleConfigurationParameter, LimitsParameter limitsParameter,
			InitialConditionParameter initialConditionParameter) throws InvalidSimulationParameterException {
		this.ruleTypeParameter = ruleTypeParameter;
		this.ruleConfigurationParameter = ruleConfigurationParameter;
		this.limitsParameter = limitsParameter;
		this.initialConditionParameter = initialConditionParameter;
		validate();
	}

	public RuleTypeParameter getRuleTypeParameter() {
		return ruleTypeParameter;
	}

	public RuleConfigurationParameter getRuleConfigurationParameter() {
		return ruleConfigurationParameter;
	}

	public LimitsParameter getLimitsParameter() {
		return limitsParameter;
	}

	public InitialConditionParameter getInitialConditionParameter() {
		return initialConditionParameter;
	}

	public void validate() throws InvalidSimulationParameterException {
		validateRuleType();
		validateRuleConfiguration();
		validateLimits();
		validateInitialCondition();
	}

	protected abstract void validateRuleType() throws InvalidSimulationParameterException;

	protected abstract void validateRuleConfiguration() throws InvalidSimulationParameterException;

	protected abstract void validateLimits() throws InvalidSimulationParameterException;

	protected abstract void validateInitialCondition() throws InvalidSimulationParameterException;

}
