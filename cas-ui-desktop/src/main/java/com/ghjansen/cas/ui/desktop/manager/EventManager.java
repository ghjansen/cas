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
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.ghjansen.cas.control.exception.InvalidSimulationParameterException;
import com.ghjansen.cas.control.exception.SimulationAlreadyActiveException;
import com.ghjansen.cas.control.exception.SimulationBuilderException;
import com.ghjansen.cas.ui.desktop.swing.GUIValidator;
import com.ghjansen.cas.ui.desktop.swing.SimulationParameterJsonAdapter;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class EventManager {
	
	private Main main;
	private boolean skipRuleNumberEvent;
	private Color invalidFieldColor;
	private GUIValidator validator;
	private UnidimensionalSimulationParameter simulationParameter;
	private Gson gson;

	public EventManager(Main main) {
		this.main = main;
		this.skipRuleNumberEvent = false;
		this.invalidFieldColor = Color.red;
		this.validator = new GUIValidator(main, invalidFieldColor);
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UnidimensionalSimulationParameter.class, new SimulationParameterJsonAdapter<UnidimensionalSimulationParameter>());
		gsonBuilder.setPrettyPrinting();
		this.gson = gsonBuilder.create();
	}
	
	private void createSimulationParameter() throws InvalidSimulationParameterException, SimulationBuilderException{
		int[] s = main.transitionsView.getStates();
		int iterations = Integer.valueOf(main.txtIterations.getText());
		int cells = Integer.valueOf(main.txtCells.getText());
		UnidimensionalRuleTypeParameter ruleType = new UnidimensionalRuleTypeParameter(true);
		UnidimensionalRuleConfigurationParameter ruleConfiguration = new UnidimensionalRuleConfigurationParameter(s[7],s[6],s[5],s[4],s[3],s[2],s[1],s[0]);
		UnidimensionalLimitsParameter limits = new UnidimensionalLimitsParameter(cells, iterations);
		UnidimensionalSequenceParameter sequence1 = new UnidimensionalSequenceParameter(1, cells/2, 0);
		UnidimensionalSequenceParameter sequence2 = new UnidimensionalSequenceParameter((cells/2)+1, (cells/2)+1, 1);
		UnidimensionalSequenceParameter sequence3 = new UnidimensionalSequenceParameter((cells/2)+2, cells, 0);
		UnidimensionalInitialConditionParameter initialCondition = new UnidimensionalInitialConditionParameter(sequence1, sequence2, sequence3);
		this.simulationParameter = new UnidimensionalSimulationParameter(ruleType, ruleConfiguration, limits, initialCondition);
	}
	
	public void executeComplete(){
		try {
			if(this.simulationParameter == null){
				createSimulationParameter();
			}
			UnidimensionalSimulationBuilder simulationBuilder = new UnidimensionalSimulationBuilder(this.simulationParameter);
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
	
	public void elementaryRuleTypeEvent(){
		
	}
	
	public void totalisticRuleTypeEvent(){
		
	}
	
	public void transitionsEvent(){
		int[] states = main.transitionsView.getStates();
		int result = 0;
		for(int i = 0; i < states.length; i++){
			result = (int) (result + (states[i] == 1 ? Math.pow(2, i) : 0));
		}
		this.skipRuleNumberEvent = true;
		main.txtRuleNumber.setText(String.valueOf(result));
		main.txtRuleNumber.setBackground(SystemColor.text);
		validator.updateStatus();
		this.skipRuleNumberEvent = false;
	}
	
	public void ruleNumberEvent(){
		if(validator.isRuleNumberValid()){
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
			main.transitionsView.setStates(states);
		}
	}
	
	public void cellsEvent(){
		validator.isCellsValid();
	}
	
	public void iterationsEvent(){
		validator.isIterationsValid();
	}
	
	public void uniqueCellEvent(){
		if(main.scrollPane != null){
			main.scrollPane.setEnabled(false);
			main.table.setEnabled(false);
			main.btnAdicionar.setEnabled(false);
			main.btnRemover.setEnabled(false);
			main.btnNewButton.setEnabled(false);
		}
	}
	
	public void informPatternCellEvent(){
		main.scrollPane.setEnabled(true);
		main.table.setEnabled(true);
		main.btnAdicionar.setEnabled(true);
		main.btnRemover.setEnabled(true);
		main.btnNewButton.setEnabled(true);
	}
	
	public boolean isSkipRuleNumberEvent(){
		return this.skipRuleNumberEvent;
	}
	
	public void saveEvent(){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setSelectedFile(fc.getCurrentDirectory() );
		fc.setDialogTitle("Salvar arquivo");
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(new FileNameExtensionFilter("Arquivo CAS (*.cas)", "cas"));
		int result = fc.showSaveDialog(main.frame);
		if(result == JFileChooser.APPROVE_OPTION){
			if(this.simulationParameter == null){
				try {
					createSimulationParameter();
				} catch (InvalidSimulationParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SimulationBuilderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String fileName = String.valueOf(fc.getSelectedFile());
			if(!fileName.endsWith(".cas")){
				fileName = fileName + ".cas";
			}
			String content = gson.toJson(simulationParameter);
			FileWriter fw;
			try {
				fw = new FileWriter(fileName);
				fw.write(content);
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void openEvent(){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setSelectedFile(fc.getCurrentDirectory() );
		fc.setDialogTitle("Abrir arquivo");
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(new FileNameExtensionFilter("Arquivo CAS (*.cas)", "cas"));
		int result = fc.showOpenDialog(main.frame);
		if(result == JFileChooser.APPROVE_OPTION){
			BufferedReader br = null;
			StringBuilder content = new StringBuilder();
			String line = null;
			try {
	    		br = new BufferedReader(new FileReader(fc.getSelectedFile()));
	    		while((line = br.readLine()) != null){
	    			content.append(line);
	    		}
	    		if(content.length() > 0){
	    			this.simulationParameter = gson.fromJson(content.toString(), UnidimensionalSimulationParameter.class);
	    			updateVisualParameters();
	    		} else {
	    			// arquivo invalido ou vazio
	    		}
	    	} catch (FileNotFoundException e) {
	    		e.printStackTrace();
	    	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void updateVisualParameters(){
		int[] statesParameter = this.simulationParameter.getRuleConfigurationParameter().getStateValues();
		int[] statesVisual = new int[statesParameter.length];
		for(int i = 0; i < statesParameter.length; i++){
			statesVisual[statesVisual.length-1-i] = statesParameter[i];
		}
		main.transitionsView.setStates(statesVisual);
		transitionsEvent();
		main.txtCells.setText(String.valueOf(this.simulationParameter.getLimitsParameter().getCells()));
		main.txtIterations.setText(String.valueOf(this.simulationParameter.getLimitsParameter().getIterations()));
	}
	
}
