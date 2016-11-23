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
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.ghjansen.cas.control.exception.InvalidSimulationParameterException;
import com.ghjansen.cas.control.exception.SimulationBuilderException;
import com.ghjansen.cas.ui.desktop.swing.ActivityState;
import com.ghjansen.cas.ui.desktop.swing.GUIValidator;
import com.ghjansen.cas.ui.desktop.swing.Main;
import com.ghjansen.cas.ui.desktop.swing.SimulationParameterJsonAdapter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalInitialConditionParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalLimitsParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalRuleConfigurationParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalRuleTypeParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSequenceParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationBuilder;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationController;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationParameter;
import com.ghjansen.cas.unidimensional.physics.UnidimensionalCell;
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
	public GUIValidator validator;
	private Gson gson;
	private UnidimensionalSimulationParameter simulationParameter;
	private UnidimensionalSimulationController simulationController;
	private ActivityState activityState;
	private Notification notification;
	private boolean omitDiscardConfirmation = false;

	public EventManager(Main main) {
		this.main = main;
		this.skipRuleNumberEvent = false;
		this.invalidFieldColor = Color.red;
		this.validator = new GUIValidator(main, invalidFieldColor);
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UnidimensionalSimulationParameter.class, new SimulationParameterJsonAdapter<UnidimensionalSimulationParameter>());
		gsonBuilder.setPrettyPrinting();
		this.gson = gsonBuilder.create();
		this.notification = new Notification(this);
	}
	
	private void createSimulationParameter() throws InvalidSimulationParameterException, SimulationBuilderException{
		int[] s = main.transitionsView.getStates();
		int iterations = Integer.valueOf(main.txtIterations.getText());
		int cells = Integer.valueOf(main.txtCells.getText());
		UnidimensionalRuleTypeParameter ruleType = new UnidimensionalRuleTypeParameter(true);
		UnidimensionalRuleConfigurationParameter ruleConfiguration = new UnidimensionalRuleConfigurationParameter(s[7],s[6],s[5],s[4],s[3],s[2],s[1],s[0]);
		UnidimensionalLimitsParameter limits = new UnidimensionalLimitsParameter(cells, iterations);
//		UnidimensionalSequenceParameter sequence1 = new UnidimensionalSequenceParameter(1, cells/2, 0);
//		UnidimensionalSequenceParameter sequence2 = new UnidimensionalSequenceParameter((cells/2)+1, (cells/2)+1, 1);
//		UnidimensionalSequenceParameter sequence3 = new UnidimensionalSequenceParameter((cells/2)+2, cells, 0);
		UnidimensionalInitialConditionParameter initialCondition = new UnidimensionalInitialConditionParameter(getSequences(cells));
		this.simulationParameter = new UnidimensionalSimulationParameter(ruleType, ruleConfiguration, limits, initialCondition);
	}
	
	private UnidimensionalSequenceParameter[] getSequences(int totalCells){
		if(totalCells == 1){
			return new UnidimensionalSequenceParameter[]{new UnidimensionalSequenceParameter(1, 1, 1)};
		} else if (totalCells == 2){
			return new UnidimensionalSequenceParameter[]{new UnidimensionalSequenceParameter(1, 1, 1), new UnidimensionalSequenceParameter(2, 2, 0)};
		} else if (totalCells >= 3){
			int centralCell = getCentralCell(totalCells);
			UnidimensionalSequenceParameter sequence1 = new UnidimensionalSequenceParameter(1, centralCell-1, 0);
			UnidimensionalSequenceParameter sequence2 = new UnidimensionalSequenceParameter(centralCell, centralCell, 1);
			UnidimensionalSequenceParameter sequence3 = new UnidimensionalSequenceParameter(centralCell+1, totalCells, 0);
			return new UnidimensionalSequenceParameter[]{sequence1, sequence2, sequence3};
		}
		return null;
	}
	
	private int getCentralCell(int totalCells){
		if(totalCells > 1){
			return (int) Math.ceil((double) totalCells / 2);
		} else return 1;
	}
	
	private void createSimulationController() throws SimulationBuilderException{
		UnidimensionalSimulationBuilder simulationBuilder = new UnidimensionalSimulationBuilder(this.simulationParameter);
		simulationController = new UnidimensionalSimulationController(simulationBuilder, notification);
		main.simulationView.setUniverse((UnidimensionalUniverse) simulationController.getSimulation().getUniverse());
		main.simulationView.reset();
		main.progressBar.setMaximum(simulationParameter.getLimitsParameter().getIterations());
		main.progressBar.setValue(0);
		main.simulationView.setProgressBar(main.progressBar);
		main.simulationView.setValidator(validator);
	}
	
	public void executeComplete(){
		try {
			setActivityState(ActivityState.EXECUTING_RULE);
			if(this.simulationParameter == null){
				createSimulationParameter();
			}
			if(this.simulationController == null){
				createSimulationController();
			}
			simulationController.startCompleteTask();
		} catch (Throwable e) {
			validator.setErrorStatus("Ocorreu um erro ao executar a simulação: "+e);
		}
	}
	
	public void executeIterationEvent(){
		try {
			setActivityState(ActivityState.EXECUTING_RULE);
			this.validator.setNormalStatus("Simulação em progresso ...");
			if(this.simulationParameter == null){
				createSimulationParameter();
			}
			if(this.simulationController == null){
				createSimulationController();
			}
			simulationController.startIterationTask();
		} catch (Throwable e) {
			validator.setErrorStatus("Ocorreu um erro ao executar a simulação: "+e);
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
			main.btnAdd.setEnabled(false);
			main.btnRemove.setEnabled(false);
			main.btnClean.setEnabled(false);
		}
	}
	
	
	public void informPatternCellEvent(){
		main.scrollPane.setEnabled(true);
		main.table.setEnabled(true);
		main.btnAdd.setEnabled(true);
		main.btnRemove.setEnabled(true);
		main.btnClean.setEnabled(true);
	}
	
	
	public boolean isSkipRuleNumberEvent(){
		return this.skipRuleNumberEvent;
	}
	
	
	public void discardEvent(){
		int result;
		if(!omitDiscardConfirmation){
			JCheckBox checkbox = new JCheckBox("Não perguntar novamente");
			String message = "Você deseja descartar a simulação atual?";
			Object[] params = {message, checkbox};
			result = JOptionPane.showConfirmDialog(main.frame, params, null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if(checkbox.isSelected()){
				omitDiscardConfirmation = true;
			}
		} else {
			result = JOptionPane.YES_OPTION;
		}
		if(result == JOptionPane.YES_OPTION){
			simulationController.getSimulation().setActive(false);
			main.simulationView.setUniverse(null);
			simulationController = null;
			simulationParameter = null;
			main.txtRuleNumber.setText("0");
			main.txtCells.setText("1");
			main.txtIterations.setText("1");
			main.transitionsView.hideHighlight();
			main.progressBar.setValue(0);
			validator.updateStatus();
			setActivityState(ActivityState.CONFIGURING_RULE);
		}
	}
	
	private void resetSimulation(){
		simulationController.getSimulation().setActive(false);
		main.simulationView.setUniverse(null);
		simulationController = null;
		simulationParameter = null;
		main.txtRuleNumber.setText("0");
		main.txtCells.setText("1");
		main.txtIterations.setText("1");
		main.transitionsView.hideHighlight();
		main.progressBar.setValue(0);
	}
	
	
	public void saveEvent(){
		ActivityState previous = activityState;
		setActivityState(ActivityState.SAVING_FILE);
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
				validator.setNormalStatus("Arquivo salvo com sucesso.");
				setActivityState(previous);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void openEvent(){
		setActivityState(ActivityState.OPENING_FILE);
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
	    			validator.updateStatus();
	    			if(!validator.isActivityLocked()){
	    				simulationController = null;
	    				main.transitionsView.hideHighlight();
	    				main.progressBar.setValue(0);
	    				executeComplete();
	    			}
	    		} else {
	    			validator.setErrorStatus("Erro ao abrir arquivo: arquivo vazio ou inválido.");
	    		}
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		validator.setErrorStatus("Erro ao abrir arquivo: "+e);
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
	
	
	public void exportEvent(){
		ActivityState previous = activityState;
		setActivityState(ActivityState.EXPORTING_FILE);
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setSelectedFile(fc.getCurrentDirectory() );
		fc.setDialogTitle("Exportar arquivo");
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(new FileNameExtensionFilter("Arquivo PNG (*.png)", "png"));
		int result = fc.showSaveDialog(main.frame);
		if(result == JFileChooser.APPROVE_OPTION){
			
			String fileName = String.valueOf(fc.getSelectedFile());
			if(!fileName.endsWith(".png")){
				fileName = fileName + ".png";
			}
			int width = simulationController.getSimulation().getUniverse().getTime().getRelative().get(0).getLimit();
			int height = simulationController.getSimulation().getUniverse().getTime().getLimit() + 1;
			BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			// initial condition
			for(int i = 0; i < simulationController.getSimulation().getUniverse().getSpace().getInitial().size(); i++){
				UnidimensionalCell c = (UnidimensionalCell) simulationController.getSimulation().getUniverse().getSpace().getInitial().get(i);
				Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
				buffer.setRGB(i, 0, color.getRGB());
			}
			// history
			for(int j = 0; j < simulationController.getSimulation().getUniverse().getSpace().getHistory().size(); j++){
				List<UnidimensionalCell> cells = simulationController.getSimulation().getUniverse().getSpace().getHistory().get(j);
				for(int i = 0; i < cells.size(); i++){
					UnidimensionalCell c = (UnidimensionalCell) cells.get(i);
					Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
					buffer.setRGB(i, j+1, color.getRGB());
				}
			}
			// current/last
			for(int i = 0; i < simulationController.getSimulation().getUniverse().getSpace().getCurrent().size(); i++){
				UnidimensionalCell c = (UnidimensionalCell) simulationController.getSimulation().getUniverse().getSpace().getCurrent().get(i);
				Color color = c.getState().getValue() == 0 ? Color.white : Color.black;
				buffer.setRGB(i, height-1, color.getRGB());
			}
			File f = new File(fileName);
			try {
				ImageIO.write(buffer, "PNG", f);
				validator.setNormalStatus("Arquivo exportado com sucesso.");
				setActivityState(previous);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void setActivityState(ActivityState state){
		switch(state){
		case CONFIGURING_RULE:
			main.transitionsView.setMouseEnabled(true);
			main.txtRuleNumber.setEnabled(true);
			main.txtCells.setEnabled(true);
			main.txtIterations.setEnabled(true);
			main.btnDiscard.setEnabled(false);
			main.btnSimulateComplete.setEnabled(true);
			main.btnSimulateIteration.setEnabled(true);
			main.btnOpen.setEnabled(true);
			main.btnSave.setEnabled(true);
			main.btnExport.setEnabled(false);
			main.progressBar.setStringPainted(false);
			this.activityState = state;
			break;
		case EXECUTING_RULE:
			main.transitionsView.setMouseEnabled(false);
			main.txtRuleNumber.setEnabled(false);
			main.txtCells.setEnabled(false);
			main.txtIterations.setEnabled(false);
			main.btnDiscard.setEnabled(true);
			main.btnSimulateComplete.setEnabled(true);
			main.btnSimulateIteration.setEnabled(true);
			main.btnOpen.setEnabled(false);
			main.btnSave.setEnabled(true);
			main.btnExport.setEnabled(false);
			main.progressBar.setStringPainted(true);
			this.activityState = state;
			break;
		case ANALYSING:
			main.transitionsView.setMouseEnabled(false);
			main.txtRuleNumber.setEnabled(false);
			main.txtCells.setEnabled(false);
			main.txtIterations.setEnabled(false);
			main.btnDiscard.setEnabled(true);
			main.btnSimulateComplete.setEnabled(false);
			main.btnSimulateIteration.setEnabled(false);
			main.btnOpen.setEnabled(true);
			main.btnSave.setEnabled(true);
			main.btnExport.setEnabled(true);
			main.progressBar.setStringPainted(true);
			this.activityState = state;
			break;
		case EXPORTING_FILE:
			break;
		case OPENING_FILE:
			break;
		case SAVING_FILE:
			break;
		default:
			break;
		}
	}
	
}
