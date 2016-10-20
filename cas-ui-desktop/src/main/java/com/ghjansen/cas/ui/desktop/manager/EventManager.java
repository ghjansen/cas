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

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;

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
	private boolean avoidRuleNumberListener;
	private Color invalidFieldColor;
	private boolean isStatusAvailable;

	public EventManager(Main main) {
		this.main = main;
		this.avoidRuleNumberListener = false;
		this.invalidFieldColor = Color.red;
		this.isStatusAvailable = true;
	}
	
	public void executeComplete(){
		int[] s = main.ruleTransitions.getStates();
		int iterations = Integer.valueOf(main.txtIterations.getText());
		int cells = Integer.valueOf(main.txtCells.getText());
		UnidimensionalRuleTypeParameter ruleType = new UnidimensionalRuleTypeParameter(true);
		UnidimensionalRuleConfigurationParameter ruleConfiguration = new UnidimensionalRuleConfigurationParameter(s[7],s[6],s[5],s[4],s[3],s[2],s[1],s[0]);
		UnidimensionalLimitsParameter limits = new UnidimensionalLimitsParameter(iterations, cells);
		UnidimensionalSequenceParameter sequence1 = new UnidimensionalSequenceParameter(1, cells/2, 0);
		UnidimensionalSequenceParameter sequence2 = new UnidimensionalSequenceParameter((cells/2)+1, (cells/2)+1, 1);
		UnidimensionalSequenceParameter sequence3 = new UnidimensionalSequenceParameter((cells/2)+2, cells, 0);
		UnidimensionalInitialConditionParameter initialCondition = new UnidimensionalInitialConditionParameter(sequence1, sequence2, sequence3);
		try {
			UnidimensionalSimulationParameter simulationParameter = new UnidimensionalSimulationParameter(ruleType, ruleConfiguration, limits, initialCondition);
			UnidimensionalSimulationBuilder simulationBuilder = new UnidimensionalSimulationBuilder(simulationParameter);
			UnidimensionalSimulationController simulationController = new UnidimensionalSimulationController(simulationBuilder);
			main.simulationView.setUniverse((UnidimensionalUniverse) simulationController.getSimulation().getUniverse());
			main.simulationView.reset();
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
	
	public void showElementaryTransitions(){
		
	}
	
	public void showTotalisticTransitions(){
		
	}
	
	public void refreshRuleNumber(){
		int[] states = main.ruleTransitions.getStates();
		int result = 0;
		for(int i = 0; i < states.length; i++){
			result = (int) (result + (states[i] == 1 ? Math.pow(2, i) : 0));
		}
		this.avoidRuleNumberListener = true;
		main.txtRuleNumber.setText(String.valueOf(result));
		main.txtRuleNumber.setBackground(SystemColor.text);
		this.avoidRuleNumberListener = false;
	}
	
	public void refreshTransitions(){
		if(isRuleNumberValid()){
			int value = Integer.valueOf(main.txtRuleNumber.getText());
			main.txtRuleNumber.setBackground(SystemColor.text);
			char[] binary = Integer.toBinaryString(value).toCharArray();
			int[] states = new int[8];
			for(int i = 0; i < states.length; i++){
				if(i < binary.length){
					states[i] = Integer.parseInt(String.valueOf(binary[binary.length - 1 - i]));
				} else {
					states[i] = 0;
				}
			}
			main.ruleTransitions.setStates(states);
		}
	}
	
	public boolean isAvoidRuleNumberListener(){
		return this.avoidRuleNumberListener;
	}
	
	public boolean isRuleNumberValid(){
		String errorMessage = "O numero da regra deve ser um numero inteiro maior ou igual a 0 e menor ou igual a 255";
		String value = main.txtRuleNumber.getText();
		if(isValidPositiveInteger(value) && Integer.valueOf(value) > -1 && Integer.valueOf(value) < 256){
			main.txtRuleNumber.setBackground(SystemColor.text);
			removeStatus(errorMessage);
			return true;
		}
		main.txtRuleNumber.setBackground(invalidFieldColor);
		setStatus(errorMessage, invalidFieldColor);
		return false;
	}
	
	public boolean isCellsValid(){
		String errorMessage = "A quantidade de células deve ser um numero inteiro maior ou igual a 1";
		String value = main.txtCells.getText();
		if(isValidPositiveInteger(value) && Integer.valueOf(value) > 0){
			main.txtCells.setBackground(SystemColor.text);
			removeStatus(errorMessage);
			return true;
		}
		main.txtCells.setBackground(invalidFieldColor);
		setStatus(errorMessage, invalidFieldColor);
		return false;
	}
	
	public boolean isIterationsValid(){
		String errorMessage = "A quantidade de iterações deve ser um numero inteiro maior ou igual a 1";
		String value = main.txtIterations.getText();
		if(isValidPositiveInteger(value) && Integer.valueOf(value) > 0){
			main.txtIterations.setBackground(SystemColor.text);
			removeStatus(errorMessage);
			return true;
		}
		main.txtIterations.setBackground(invalidFieldColor);
		setStatus(errorMessage, invalidFieldColor);
		return false;
	}
	
	private boolean isValidPositiveInteger(String value){
		try{
			int integer = Integer.valueOf(value);
			if(integer > -1){
				return true;
			} 
			return false;
		} catch(Exception e){
			return false;
		}
	}
	
	private void setStatus(String message, Color color){
		if(main.lblStatus.getText().equals("Pronto.")){
			main.lblStatus.setText(message);
			main.lblStatus.setForeground(color);
		}
	}
	
	private void removeStatus(String message){
		if(main.lblStatus.getText().equals(message)){
			main.lblStatus.setText("Pronto.");
			main.lblStatus.setForeground(SystemColor.textText);
		}
	}

}
