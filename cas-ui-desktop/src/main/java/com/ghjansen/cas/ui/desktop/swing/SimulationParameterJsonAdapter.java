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

package com.ghjansen.cas.ui.desktop.swing;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
import java.lang.reflect.Type;

import com.ghjansen.cas.control.exception.InvalidSimulationParameterException;
import com.ghjansen.cas.unidimensional.control.UnidimensionalInitialConditionParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalLimitsParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalRuleConfigurationParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalRuleTypeParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSequenceParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationParameter;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SimulationParameterJsonAdapter<T>
    implements JsonSerializer<T>, JsonDeserializer<T> {

    public final JsonElement serialize(final T object, final Type interfaceType, final JsonSerializationContext context) 
    {
    	UnidimensionalSimulationParameter parameter = (UnidimensionalSimulationParameter) object;
        final JsonObject member = new JsonObject();
        final JsonObject ruleTypeParameter = new JsonObject();
        final JsonObject ruleConfigurationParameter = new JsonObject();
        final JsonArray stateValues = new JsonArray();
        final JsonObject limitsParameter = new JsonObject();
        final JsonObject initialConditionParameter = new JsonObject();
        final JsonArray sequences = new JsonArray();
        ruleTypeParameter.addProperty("elementar", parameter.getRuleTypeParameter().isElementar());
        int[] states = parameter.getRuleConfigurationParameter().getStateValues();
        for(int i = 0; i < states.length; i++){
        	stateValues.add(new JsonPrimitive(parameter.getRuleConfigurationParameter().getStateValues()[states.length-1-i]));
        }
        ruleConfigurationParameter.add("stateValues", stateValues);
        limitsParameter.addProperty("cells", parameter.getLimitsParameter().getCells());
        limitsParameter.addProperty("iterations", parameter.getLimitsParameter().getIterations());
        for(int i = 0; i < parameter.getInitialConditionParameter().getSequences().size(); i++){
        	UnidimensionalSequenceParameter s = (UnidimensionalSequenceParameter) parameter.getInitialConditionParameter().getSequences().get(i);
        	JsonObject sequence = new JsonObject();
        	sequence.addProperty("initialPosition", s.getInitialPosition());
        	sequence.addProperty("finalPosition", s.getFinalPosition());
        	sequence.addProperty("value", s.getValue());
        	sequences.add(sequence);
        }
        initialConditionParameter.add("sequences", sequences);
        
        member.add("ruleTypeParameter", ruleTypeParameter);
        member.add("ruleConfigurationParameter", ruleConfigurationParameter);
        member.add("limitsParameter", limitsParameter);
        member.add("initialConditionParameter", initialConditionParameter);
        return member;
    }

    public final T deserialize(final JsonElement elem, final Type interfaceType, final JsonDeserializationContext context){
        final JsonObject member = (JsonObject) elem;
        final JsonObject ruleTypeParameterJson = member.getAsJsonObject("ruleTypeParameter");
        final JsonObject ruleConfigurationParameterJson = member.getAsJsonObject("ruleConfigurationParameter");
        final JsonArray stateValuesJson = ruleConfigurationParameterJson.get("stateValues").getAsJsonArray();
        final JsonObject limitsParameterJson = member.getAsJsonObject("limitsParameter");
        final JsonObject initialConditionParameterJson = member.getAsJsonObject("initialConditionParameter");
        final JsonArray sequencesJson = initialConditionParameterJson.get("sequences").getAsJsonArray();
        
        UnidimensionalRuleTypeParameter ruleTypeParameter = new UnidimensionalRuleTypeParameter(ruleTypeParameterJson.get("elementar").getAsBoolean());
        int[] stateValues = new int[stateValuesJson.size()];
        for(int i = 0; i < stateValuesJson.size(); i++){
        	stateValues[i] = stateValuesJson.get(i).getAsInt();
        }
        UnidimensionalRuleConfigurationParameter ruleConfigurationParameter = new UnidimensionalRuleConfigurationParameter(stateValues[7], stateValues[6], stateValues[5], stateValues[4], stateValues[3], stateValues[2], stateValues[1], stateValues[0]);
        UnidimensionalLimitsParameter limitsParameter = new UnidimensionalLimitsParameter(limitsParameterJson.get("cells").getAsInt(), limitsParameterJson.get("iterations").getAsInt());
        UnidimensionalSequenceParameter[] sequences = new UnidimensionalSequenceParameter[sequencesJson.size()];
        for(int i = 0; i < sequencesJson.size(); i++){
        	JsonObject s = sequencesJson.get(i).getAsJsonObject();
        	UnidimensionalSequenceParameter sequence = new UnidimensionalSequenceParameter(s.get("initialPosition").getAsInt(), s.get("finalPosition").getAsInt(), s.get("value").getAsInt());
        	sequences[i] = sequence;
        }
        UnidimensionalInitialConditionParameter initialConditionParameter = new UnidimensionalInitialConditionParameter(sequences);
        try {
			return (T) new UnidimensionalSimulationParameter(ruleTypeParameter, ruleConfigurationParameter, limitsParameter, initialConditionParameter);
		} catch (InvalidSimulationParameterException e) {
			e.printStackTrace();
		}
        return null;
    }

}
