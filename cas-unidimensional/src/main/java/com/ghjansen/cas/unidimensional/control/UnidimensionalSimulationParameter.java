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

import java.util.ArrayList;
import java.util.List;

import com.ghjansen.cas.control.exception.InvalidInitialConditionException;
import com.ghjansen.cas.control.exception.InvalidLimitsException;
import com.ghjansen.cas.control.exception.InvalidRuleConfigurationException;
import com.ghjansen.cas.control.exception.InvalidRuleTypeException;
import com.ghjansen.cas.control.exception.InvalidSimulationParameterException;
import com.ghjansen.cas.control.parameter.InitialConditionParameter;
import com.ghjansen.cas.control.parameter.LimitsParameter;
import com.ghjansen.cas.control.parameter.RuleConfigurationParameter;
import com.ghjansen.cas.control.parameter.RuleTypeParameter;
import com.ghjansen.cas.control.parameter.SequenceParameter;
import com.ghjansen.cas.control.simulation.SimulationParameter;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class UnidimensionalSimulationParameter extends SimulationParameter {

    public UnidimensionalSimulationParameter(RuleTypeParameter ruleTypeParameter,
                                             RuleConfigurationParameter ruleConfigurationParameter, LimitsParameter limitsParameter,
                                             InitialConditionParameter initialConditionParameter) throws InvalidSimulationParameterException {
        super(ruleTypeParameter, ruleConfigurationParameter, limitsParameter, initialConditionParameter);
    }

    @Override
    protected void validateRuleType() throws InvalidRuleTypeException {
        if (this.getRuleTypeParameter() == null) {
            throw new InvalidRuleTypeException();
        }
    }

    @Override
    protected void validateRuleConfiguration() throws InvalidRuleConfigurationException {
        if (this.getRuleConfigurationParameter() == null) {
            throw new InvalidRuleConfigurationException();
        }
        ArrayList<Integer> allowedValues = new ArrayList<Integer>();
        allowedValues.add(0);
        allowedValues.add(1);
        if (!this.getRuleTypeParameter().isElementar()) {
            allowedValues.add(2);
        }
        int[] states = this.getRuleConfigurationParameter().getStateValues();
        if (states.length != 8) {
            throw new InvalidRuleConfigurationException();
        }
        for (int i = 0; i < states.length; i++) {
            if (!allowedValues.contains(states[i])) {
                throw new InvalidRuleConfigurationException();
            }
        }
    }

    @Override
    protected void validateLimits() throws InvalidLimitsException {
        if (this.getLimitsParameter() == null) {
            throw new InvalidLimitsException();
        }
        if (this.getLimitsParameter().getCells() <= 0) {
            throw new InvalidLimitsException();
        }
        if (this.getLimitsParameter().getIterations() <= 0) {
            throw new InvalidLimitsException();
        }
    }

    @Override
    protected void validateInitialCondition() throws InvalidInitialConditionException {
        if (this.getInitialConditionParameter() == null) {
            throw new InvalidInitialConditionException();
        }
        int control = 1;
        List<SequenceParameter> sequences = this.getInitialConditionParameter().getSequences();
        for (SequenceParameter sequence : sequences) {
            if (sequence.getInitialPosition() != control) {
                throw new InvalidInitialConditionException();
            }
            control = sequence.getFinalPosition() + 1;
        }
        if (this.getLimitsParameter().getCells() != control - 1) {
            throw new InvalidInitialConditionException();
        }
    }
}
