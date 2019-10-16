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
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.ghjansen.cas.control.exception.InvalidSimulationParameterException;
import com.ghjansen.cas.control.exception.SimulationBuilderException;
import com.ghjansen.cas.ui.desktop.i18n.Language;
import com.ghjansen.cas.ui.desktop.i18n.Translator;
import com.ghjansen.cas.ui.desktop.swing.ActivityState;
import com.ghjansen.cas.ui.desktop.swing.GUIValidator;
import com.ghjansen.cas.ui.desktop.swing.IconListRenderer;
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
	public Gson gson;
	public UnidimensionalSimulationParameter simulationParameter;
	public UnidimensionalSimulationController simulationController;
	public ActivityState activityState;
	private ActivityState previousActivityState;
	private Notification notification;
	public boolean omitDiscardConfirmation = false;

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
		this.previousActivityState = null;
	}
	
	public void createSimulationParameter() throws InvalidSimulationParameterException, SimulationBuilderException{
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
			if(main.rdbtnRandom.isSelected()){
				return new UnidimensionalSequenceParameter[]{new UnidimensionalSequenceParameter(1, 1, getRandomBoolean()? 1 : 0)};
			} else {
				return new UnidimensionalSequenceParameter[]{new UnidimensionalSequenceParameter(1, 1, 1)};
			}
		} else if (totalCells == 2){
			if(main.rdbtnRandom.isSelected()){
				return new UnidimensionalSequenceParameter[]{new UnidimensionalSequenceParameter(1, 1, getRandomBoolean()? 1 : 0), new UnidimensionalSequenceParameter(2, 2, getRandomBoolean()? 1 : 0)};
			} else {
				return new UnidimensionalSequenceParameter[]{new UnidimensionalSequenceParameter(1, 1, 1), new UnidimensionalSequenceParameter(2, 2, 0)};
			}
		} else if (totalCells >= 3){
			if(main.rdbtnRandom.isSelected()){
				UnidimensionalSequenceParameter[] sequence = new UnidimensionalSequenceParameter[totalCells];
				sequence[0] = new UnidimensionalSequenceParameter(1, 1, getRandomBoolean()? 1 : 0);
				for(int i = 2; i <= totalCells; i++){
					sequence[i-1] = new UnidimensionalSequenceParameter(i, i, getRandomBoolean()? 1 : 0);
				}
				return sequence;
			} else {
				int centralCell = getCentralCell(totalCells);
				UnidimensionalSequenceParameter sequence1 = new UnidimensionalSequenceParameter(1, centralCell-1, 0);
				UnidimensionalSequenceParameter sequence2 = new UnidimensionalSequenceParameter(centralCell, centralCell, 1);
				UnidimensionalSequenceParameter sequence3 = new UnidimensionalSequenceParameter(centralCell+1, totalCells, 0);
				return new UnidimensionalSequenceParameter[]{sequence1, sequence2, sequence3};
			}
		}
		return null;
	}

	public static boolean getRandomBoolean() {
		return Math.random() < 0.5;
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
			this.validator.setNormalStatus("msgSimulationInProgress");
			if(this.simulationParameter == null){
				createSimulationParameter();
			}
			if(this.simulationController == null){
				createSimulationController();
			}
			simulationController.startCompleteTask();
		} catch (Throwable e) {
			validator.setErrorStatus("errSimulationExecution", e.toString());
			e.printStackTrace();
		}
	}
	
	public void executeIterationEvent(){
		try {
			setActivityState(ActivityState.EXECUTING_RULE);
			this.validator.setNormalStatus("msgSimulationInProgress");
			if(this.simulationParameter == null){
				createSimulationParameter();
			}
			if(this.simulationController == null){
				createSimulationController();
			}
			simulationController.startIterationTask();
		} catch (Throwable e) {
			validator.setErrorStatus("errSimulationExecution", e.toString());
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
		/*
		if(main.scrollPane != null){
			main.scrollPane.setEnabled(false);
			main.table.setEnabled(false);
			main.btnAdd.setEnabled(false);
			main.btnRemove.setEnabled(false);
			main.btnClean.setEnabled(false);
		}
		*/
	}

	public void RandomEvent(){
		/*
		if(main.scrollPane != null){
			main.scrollPane.setEnabled(false);
			main.table.setEnabled(false);
			main.btnAdd.setEnabled(false);
			main.btnRemove.setEnabled(false);
			main.btnClean.setEnabled(false);
		}
		*/
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
			JCheckBox checkbox = new JCheckBox(Translator.getInstance().get("msgCheckDiscard"));
			String message = Translator.getInstance().get("msgConfirmDialog");
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
		fc.setDialogTitle(Translator.getInstance().get("msgSaveDialogTitle"));
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(new FileNameExtensionFilter(Translator.getInstance().get("casFileExtension"), "cas"));
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
				validator.setNormalStatus("msgSaveSuccess");
				setActivityState(previous);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void openEvent(){
		ButtonModel selected = main.grpInitialCondition.getSelection();
		setActivityState(ActivityState.OPENING_FILE);
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setSelectedFile(fc.getCurrentDirectory() );
		fc.setDialogTitle(Translator.getInstance().get("msgOpenDialogTitle"));
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(new FileNameExtensionFilter(Translator.getInstance().get("casFileExtension"), "cas"));
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
	    			validator.setErrorStatus("errOpenFileInvalid", "");
	    		}
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		validator.setErrorStatus("errOpenFileGeneric", e.toString());
	    	}
		} else {
			revertActivityState();
			main.grpInitialCondition.setSelected(selected, true);
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
		main.rdbtnUniqueCell.setSelected(false);
		main.rdbtnRandom.setSelected(false);
	}
	
	
	public void exportEvent(){
		if(main.keyMonitor.isCtrlPressed()){
			main.aeo.setVisible(true);
		} else {
			exportSimulation();
		}		
	}
	
	public void exportSimulation(){
		ActivityState previous = activityState;
		setActivityState(ActivityState.EXPORTING_FILE);
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setSelectedFile(fc.getCurrentDirectory() );
		fc.setDialogTitle(Translator.getInstance().get("msgExportDialogTitle"));
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(new FileNameExtensionFilter(Translator.getInstance().get("pngFileExtension"), "png"));
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
				validator.setNormalStatus("msgExportSuccess");
				setActivityState(previous);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void languageEvent(){
		if(main.langCombo.getSelectedIndex() == 0){
			Translator.getInstance().setLanguage(Language.PORTUGUESE_BRAZIL);
		} else if (main.langCombo.getSelectedIndex() == 1){
			Translator.getInstance().setLanguage(Language.ENGLISH_UNITED_KINGDOM);
		}
		updateComponentsLanguage();
	}
	
	private void updateComponentsLanguage(){
		main.pnlRuleType.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlRuleType"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		main.rdbtnElementary.setText(Translator.getInstance().get("rdbtnElementary"));
		main.rdbtnTotalistic.setText(Translator.getInstance().get("rdbtnTotalistic"));
		main.pnlRuleConfig.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlRuleConfig"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		main.lblRuleNumber.setText(Translator.getInstance().get("lblRuleNumber"));
		main.pnlLimits.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlLimits"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		main.lblCells.setText(Translator.getInstance().get("lblCells"));
		main.lblIterations.setText(Translator.getInstance().get("lblIterations"));
		main.pnlInitialCondition.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlInitialCondition"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		main.rdbtnUniqueCell.setText(Translator.getInstance().get("rdbtnUniqueCell"));
		main.rdbtnRandom.setText(Translator.getInstance().get("rdbtnRandom"));
		main.pnlControl.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlControl"), TitledBorder.LEADING, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		main.btnOpen.setText(Translator.getInstance().get("btnOpen"));
		main.btnSave.setText(Translator.getInstance().get("btnSave"));
		main.btnExport.setText(Translator.getInstance().get("btnExport"));
		main.lblStatus.setText(Translator.getInstance().get(main.getLastStatusKey()));
		main.pnlView.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlView"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		main.langCombo.removeAllItems();
		Map<Object, Icon> icons = new HashMap<Object, Icon>();
		icons.put(Translator.getInstance().get("langCombo0"), new ImageIcon(Main.class.getResource("br.png")));
		icons.put(Translator.getInstance().get("langCombo1"), new ImageIcon(Main.class.getResource("en.png")));
		Iterator it = icons.keySet().iterator();
		main.langCombo.addItem(Translator.getInstance().get("langCombo0"));
		main.langCombo.addItem(Translator.getInstance().get("langCombo1"));
		main.langCombo.setRenderer(new IconListRenderer(icons));
		main.langCombo.setSelectedIndex(Translator.getInstance().getLanguage().getId());
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
			main.langCombo.setEnabled(true);
			main.rdbtnUniqueCell.setEnabled(true);
			main.rdbtnUniqueCell.setSelected(true);
			main.rdbtnRandom.setEnabled(true);
			changeActivityState(state);
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
			main.langCombo.setEnabled(false);
			main.rdbtnUniqueCell.setEnabled(false);
			main.rdbtnRandom.setEnabled(false);
			changeActivityState(state);
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
			main.langCombo.setEnabled(false);
			main.rdbtnUniqueCell.setEnabled(false);
			main.rdbtnRandom.setEnabled(false);
			changeActivityState(state);
			break;
		case EXPORTING_FILE:
			changeActivityState(state);
			break;
		case OPENING_FILE:
			main.grpInitialCondition.clearSelection();
			changeActivityState(state);
			break;
		case SAVING_FILE:
			changeActivityState(state);
			break;
		default:
			break;
		}
	}
	
	private void changeActivityState(ActivityState newState){
		this.previousActivityState = this.activityState;
		this.activityState = newState;
	}
	
	private void revertActivityState(){
		if(this.previousActivityState != null){
			this.activityState = null;
			setActivityState(this.previousActivityState);
		}
	}
	
	public void aEOCellScaleEvent(){
		if(validator.isAEOCellScaleValid()){
			if(Integer.valueOf(main.aeo.txtAEOCellScale.getText()) > 3) {
				main.aeo.rdbtnAEONo.setEnabled(true);
				main.aeo.rdbtnAEONo.setSelected(true);
				main.aeo.rdbtnAEOYes.setEnabled(true);
				main.aeo.rdbtnAEOYes.setSelected(false);
			} else {
				main.aeo.rdbtnAEONo.setEnabled(false);
				main.aeo.rdbtnAEOYes.setEnabled(false);
			}
		}
		
	}
	
	public void aEOGridYesEvent(){
		main.aeo.txtAEOGridLinesThickness.setEnabled(true);
	}
	
	public void aEOGridNoEvent(){
		main.aeo.txtAEOGridLinesThickness.setEnabled(false);
		main.aeo.txtAEOCellLinesColour.setEnabled(false);
	}
	
	public void aEOGridThicknessEvent(){
		validator.isAEOCellLinesThicknessValid();
	}
	
	public void aEOGridColourEvent(){
		validator.isAEOCellColourValid();
	}
	
}
