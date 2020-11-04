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
import java.io.*;
import java.util.*;

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
import com.ghjansen.cas.ui.desktop.swing.SimulationExportUtil;
import com.ghjansen.cas.ui.desktop.swing.SimulationParameterJsonAdapter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalInitialConditionParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalLimitsParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalRuleConfigurationParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalRuleTypeParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSequenceParameter;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationBuilder;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationController;
import com.ghjansen.cas.unidimensional.control.UnidimensionalSimulationParameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.ghjansen.cas.ui.desktop.i18n.TranslationKey.*;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class EventManager {

    private static final String DEFAULT_FONT_NAME = "Lucida Grande";

    private Main main;
    private boolean skipRuleNumberEvent;
    private Color invalidFieldColor;
    private GUIValidator validator;
    private Gson gson;
    private UnidimensionalSimulationParameter simulationParameter;
    private UnidimensionalSimulationController simulationController;
    private ActivityState activityState;
    private ActivityState previousActivityState;
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
        this.previousActivityState = null;
    }

    public void createSimulationParameter() throws InvalidSimulationParameterException {
        int[] s = main.transitionsView.getStates();
        int iterations = Integer.parseInt(main.txtIterations.getText());
        int cells = Integer.parseInt(main.txtCells.getText());
        UnidimensionalRuleTypeParameter ruleType = new UnidimensionalRuleTypeParameter(true);
        UnidimensionalRuleConfigurationParameter ruleConfiguration = new UnidimensionalRuleConfigurationParameter(s[7], s[6], s[5], s[4], s[3], s[2], s[1], s[0]);
        UnidimensionalLimitsParameter limits = new UnidimensionalLimitsParameter(cells, iterations);
        UnidimensionalInitialConditionParameter initialCondition = new UnidimensionalInitialConditionParameter(getSequences(cells));
        this.simulationParameter = new UnidimensionalSimulationParameter(ruleType, ruleConfiguration, limits, initialCondition);
    }

    private UnidimensionalSequenceParameter[] getSequences(int totalCells) {
        ArrayList<UnidimensionalSequenceParameter> unidimensionalSequenceParameterList = new ArrayList<UnidimensionalSequenceParameter>();
        if (main.rdbtnRandom.isSelected()) {
            //random initial condition sequences
            for (int i = 0; i < totalCells; i++) {
                unidimensionalSequenceParameterList.add(new UnidimensionalSequenceParameter(i + 1, i + 1, getRandomBoolean() ? 1 : 0));
            }
        } else {
            //unique cell in black initial condition
            if (totalCells == 1) {
                unidimensionalSequenceParameterList.add(new UnidimensionalSequenceParameter(1, 1, 1));
            } else if (totalCells == 2) {
                unidimensionalSequenceParameterList.add(new UnidimensionalSequenceParameter(1, 1, 1));
                unidimensionalSequenceParameterList.add(new UnidimensionalSequenceParameter(2, 2, 0));
            } else if (totalCells >= 3) {
                int centralCell = getCentralCell(totalCells);
                unidimensionalSequenceParameterList.add(new UnidimensionalSequenceParameter(1, centralCell - 1, 0));
                unidimensionalSequenceParameterList.add(new UnidimensionalSequenceParameter(centralCell, centralCell, 1));
                unidimensionalSequenceParameterList.add(new UnidimensionalSequenceParameter(centralCell + 1, totalCells, 0));
            }
        }
        return unidimensionalSequenceParameterList.toArray(new UnidimensionalSequenceParameter[unidimensionalSequenceParameterList.size()]);
    }

    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }

    private int getCentralCell(int totalCells) {
        if (totalCells > 1) {
            return (int) Math.ceil((double) totalCells / 2);
        } else return 1;
    }

    private void createSimulationController() throws SimulationBuilderException {
        UnidimensionalSimulationBuilder simulationBuilder = new UnidimensionalSimulationBuilder(this.simulationParameter);
        simulationController = new UnidimensionalSimulationController(simulationBuilder, notification);
        main.simulationView.setUniverse(simulationController.getSimulation().getUniverse());
        main.simulationView.reset();
        main.progressBar.setMaximum(simulationParameter.getLimitsParameter().getIterations());
        main.progressBar.setValue(0);
        main.simulationView.setProgressBar(main.progressBar);
        main.simulationView.setValidator(validator);
    }

    public void executeComplete() {
        try {
            setActivityState(ActivityState.EXECUTING_RULE);
            this.validator.setNormalStatus("msgSimulationInProgress");
            if (this.simulationParameter == null) {
                createSimulationParameter();
            }
            if (this.simulationController == null) {
                createSimulationController();
            }
            simulationController.startCompleteTask();
        } catch (Exception e) {
            validator.setErrorStatus("errSimulationExecution", e.toString());
        }
    }

    public void executeIterationEvent() {
        try {
            setActivityState(ActivityState.EXECUTING_RULE);
            this.validator.setNormalStatus("msgSimulationInProgress");
            if (this.simulationParameter == null) {
                createSimulationParameter();
            }
            if (this.simulationController == null) {
                createSimulationController();
            }
            simulationController.startIterationTask();
        } catch (Exception e) {
            validator.setErrorStatus("errSimulationExecution", e.toString());
        }
    }

    public void elementaryRuleTypeEvent() {
        //For future implementation
    }


    public void totalisticRuleTypeEvent() {
        //For future implementation
    }


    public void transitionsEvent() {
        int[] states = main.transitionsView.getStates();
        int result = 0;
        for (int i = 0; i < states.length; i++) {
            result = (int) (result + (states[i] == 1 ? Math.pow(2, i) : 0));
        }
        this.skipRuleNumberEvent = true;
        main.txtRuleNumber.setText(String.valueOf(result));
        main.txtRuleNumber.setBackground(SystemColor.text);
        validator.updateStatus();
        this.skipRuleNumberEvent = false;
    }


    public void ruleNumberEvent() {
        if (validator.isRuleNumberValid()) {
            int value = Integer.parseInt(main.txtRuleNumber.getText());
            main.txtRuleNumber.setBackground(SystemColor.text);
            char[] binary = Integer.toBinaryString(value).toCharArray();
            int[] states = new int[8];
            for (int i = 0; i < states.length; i++) {
                if (i < binary.length) {
                    states[i] = Integer.parseInt(String.valueOf(binary[binary.length - 1 - i]));
                } else {
                    states[i] = 0;
                }
            }
            main.transitionsView.setStates(states);
        }
    }


    public void cellsEvent() {
        validator.isCellsValid();
    }


    public void iterationsEvent() {
        validator.isIterationsValid();
    }


    public void uniqueCellEvent() {
        //For future implementation
    }

    public void randomEvent() {
        //For future implementation
    }

    public void informPatternCellEvent() {
        main.scrollPane.setEnabled(true);
        main.table.setEnabled(true);
        main.btnAdd.setEnabled(true);
        main.btnRemove.setEnabled(true);
        main.btnClean.setEnabled(true);
    }


    public boolean isSkipRuleNumberEvent() {
        return this.skipRuleNumberEvent;
    }


    public void discardEvent() {
        int result;
        if (!omitDiscardConfirmation) {
            JCheckBox checkbox = new JCheckBox(Translator.getInstance().get("msgCheckDiscard"));
            String message = Translator.getInstance().get("msgConfirmDialog");
            Object[] params = {message, checkbox};
            result = JOptionPane.showConfirmDialog(main.frame, params, null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (checkbox.isSelected()) {
                omitDiscardConfirmation = true;
            }
        } else {
            result = JOptionPane.YES_OPTION;
        }
        if (result == JOptionPane.YES_OPTION) {
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

    public void saveEvent() {
        ActivityState previous = activityState;
        setActivityState(ActivityState.SAVING_FILE);
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setSelectedFile(fc.getCurrentDirectory());
        fc.setDialogTitle(Translator.getInstance().get("msgSaveDialogTitle"));
        fc.setMultiSelectionEnabled(false);
        fc.setFileFilter(new FileNameExtensionFilter(Translator.getInstance().get("casFileExtension"), "cas"));
        int result = fc.showSaveDialog(main.frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            if (this.simulationParameter == null) {
                try {
                    createSimulationParameter();
                } catch (InvalidSimulationParameterException e) {
                    validator.setErrorStatus(ERR_SAVE, e.toString());
                }
            }
            String fileName = String.valueOf(fc.getSelectedFile());
            if (!fileName.endsWith(".cas")) {
                fileName = fileName + ".cas";
            }
            String content = gson.toJson(simulationParameter);
            FileWriter fw = null;
            try {
                fw = new FileWriter(fileName);
                fw.write(content);
                fw.close();
                validator.setNormalStatus("msgSaveSuccess");
                setActivityState(previous);
            } catch (IOException e) {
                validator.setErrorStatus(ERR_SAVE, e.toString());
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        validator.setErrorStatus(ERR_SAVE, e.toString());
                    }
                }

            }
        }
    }

	public void openEvent() {
		ButtonModel selected = main.grpInitialCondition.getSelection();
		setActivityState(ActivityState.OPENING_FILE);
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setSelectedFile(fc.getCurrentDirectory());
		fc.setDialogTitle(Translator.getInstance().get("msgOpenDialogTitle"));
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(new FileNameExtensionFilter(Translator.getInstance().get("casFileExtension"), "cas"));
		int result = fc.showOpenDialog(main.frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			String content = readFile(fc.getSelectedFile());
			if (content.length() > 0) {
				this.simulationParameter = gson.fromJson(content, UnidimensionalSimulationParameter.class);
				updateVisualParameters();
				validator.updateStatus();
				if (!validator.isActivityLocked()) {
					simulationController = null;
					main.transitionsView.hideHighlight();
					main.progressBar.setValue(0);
					executeComplete();
				}
			} else {
				validator.setErrorStatus(ERR_OPEN_FILE_INVALID, "");
			}
		} else {
			revertActivityState();
			main.grpInitialCondition.setSelected(selected, true);
		}
	}

	private String readFile(File file) {
		BufferedReader br = null;
		String content = "";
		try {
			br = new BufferedReader(new FileReader(file));
			content = readLines(br);
		} catch (Exception e) {
			validator.setErrorStatus(ERR_OPEN_FILE_GENERIC, e.toString());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					validator.setErrorStatus(ERR_OPEN_FILE_GENERIC, e.toString());
				}
			}
		}
		return content;
	}

	private String readLines(BufferedReader br) throws IOException {
		StringBuilder content = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			content.append(line);
		}
		return content.toString();
	}

    private void updateVisualParameters() {
        int[] statesParameter = this.simulationParameter.getRuleConfigurationParameter().getStateValues();
        int[] statesVisual = new int[statesParameter.length];
        for (int i = 0; i < statesParameter.length; i++) {
            statesVisual[statesVisual.length - 1 - i] = statesParameter[i];
        }
        main.transitionsView.setStates(statesVisual);
        transitionsEvent();
        main.txtCells.setText(String.valueOf(this.simulationParameter.getLimitsParameter().getCells()));
        main.txtIterations.setText(String.valueOf(this.simulationParameter.getLimitsParameter().getIterations()));
        main.rdbtnUniqueCell.setSelected(false);
        main.rdbtnRandom.setSelected(false);
    }


    public void exportEvent() {
        if (main.keyMonitor.isCtrlPressed()) {
            main.aeo.setVisible(true);
        } else {
            exportSimulation(1, false, 0, null);
        }
    }

    public void exportSimulation(int cellScale, boolean showGrid, int gridLineThickness, Color gridLineColour) {
        ActivityState previous = activityState;
        setActivityState(ActivityState.EXPORTING_FILE);
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setSelectedFile(fc.getCurrentDirectory());
        fc.setDialogTitle(Translator.getInstance().get("msgExportDialogTitle"));
        fc.setMultiSelectionEnabled(false);
        fc.setFileFilter(new FileNameExtensionFilter(Translator.getInstance().get("pngFileExtension"), "png"));
        int result = fc.showSaveDialog(main.frame);
        if (result == JFileChooser.APPROVE_OPTION) {

            String fileName = String.valueOf(fc.getSelectedFile());
            if (!fileName.endsWith(".png")) {
                fileName = fileName + ".png";
            }

            BufferedImage buffer = SimulationExportUtil.createBufferedImage(simulationController, cellScale, showGrid, gridLineThickness, gridLineColour);

            File f = new File(fileName);
            try {
                ImageIO.write(buffer, "PNG", f);
                validator.setNormalStatus("msgExportSuccess");
                setActivityState(previous);
            } catch (IOException e) {
                validator.setErrorStatus("errExport", e.toString());
            }
        }
    }

    public void languageEvent() {
        try {
            if (main.langCombo.getSelectedIndex() == 0) {
                Translator.getInstance().setLanguage(Language.PORTUGUESE_BRAZIL);
            } else if (main.langCombo.getSelectedIndex() == 1) {
                Translator.getInstance().setLanguage(Language.ENGLISH_UNITED_KINGDOM);
            }
            updateComponentsLanguage();
        } catch (IOException e) {
            validator.setErrorStatus("errTranslation", e.getMessage());
        }
    }

    private void updateComponentsLanguage() {
        main.pnlRuleType.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlRuleType"), TitledBorder.LEFT, TitledBorder.TOP, new Font(DEFAULT_FONT_NAME, Font.BOLD, 12), Color.BLACK));
        main.rdbtnElementary.setText(Translator.getInstance().get("rdbtnElementary"));
        main.rdbtnTotalistic.setText(Translator.getInstance().get("rdbtnTotalistic"));
        main.pnlRuleConfig.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlRuleConfig"), TitledBorder.LEFT, TitledBorder.TOP, new Font(DEFAULT_FONT_NAME, Font.BOLD, 12), Color.BLACK));
        main.lblRuleNumber.setText(Translator.getInstance().get("lblRuleNumber"));
        main.pnlLimits.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlLimits"), TitledBorder.LEFT, TitledBorder.TOP, new Font(DEFAULT_FONT_NAME, Font.BOLD, 12), Color.BLACK));
        main.lblCells.setText(Translator.getInstance().get("lblCells"));
        main.lblIterations.setText(Translator.getInstance().get("lblIterations"));
        main.pnlInitialCondition.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlInitialCondition"), TitledBorder.LEFT, TitledBorder.TOP, new Font(DEFAULT_FONT_NAME, Font.BOLD, 12), Color.BLACK));
        main.rdbtnUniqueCell.setText(Translator.getInstance().get("rdbtnUniqueCell"));
        main.rdbtnRandom.setText(Translator.getInstance().get("rdbtnRandom"));
        main.pnlControl.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlControl"), TitledBorder.LEADING, TitledBorder.TOP, new Font(DEFAULT_FONT_NAME, Font.BOLD, 12), Color.BLACK));
        main.btnOpen.setText(Translator.getInstance().get("btnOpen"));
        main.btnSave.setText(Translator.getInstance().get("btnSave"));
        main.btnExport.setText(Translator.getInstance().get("btnExport"));
        main.lblStatus.setText(Translator.getInstance().get(main.getLastStatusKey()));
        main.pnlView.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlView"), TitledBorder.LEFT, TitledBorder.TOP, new Font(DEFAULT_FONT_NAME, Font.BOLD, 12), Color.BLACK));
        main.langCombo.removeAllItems();
        Map<Object, Icon> icons = new HashMap<Object, Icon>();
        icons.put(Translator.getInstance().get("langCombo0"), new ImageIcon(Main.class.getResource("br.png")));
        icons.put(Translator.getInstance().get("langCombo1"), new ImageIcon(Main.class.getResource("en.png")));
        main.langCombo.addItem(Translator.getInstance().get("langCombo0"));
        main.langCombo.addItem(Translator.getInstance().get("langCombo1"));
        main.langCombo.setRenderer(new IconListRenderer(icons));
        main.langCombo.setSelectedIndex(Translator.getInstance().getLanguage().getId());
    }


    public void setActivityState(ActivityState state) {
        switch (state) {
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

    private void changeActivityState(ActivityState newState) {
        this.previousActivityState = this.activityState;
        this.activityState = newState;
    }

    private void revertActivityState() {
        if (this.previousActivityState != null) {
            this.activityState = null;
            setActivityState(this.previousActivityState);
        }
    }

    public void aEOCellScaleEvent() {
        if (validator.isAEOCellScaleValid()) {
            if (Integer.valueOf(main.aeo.txtAEOCellScale.getText()) >= 3) {
                if (!main.aeo.rdbtnAEOYes.isEnabled()) {
                    main.aeo.rdbtnAEONo.setEnabled(true);
                    main.aeo.rdbtnAEOYes.setEnabled(true);
                }
                if (!main.aeo.rdbtnAEOYes.isSelected()) {
                    main.aeo.rdbtnAEONo.setSelected(true);
                } else {
                    aEOGridYesEvent();
                }
            } else {
                main.aeo.rdbtnAEONo.setEnabled(false);
                main.aeo.rdbtnAEOYes.setEnabled(false);
            }
        }

    }

    public void aEOGridYesEvent() {
        main.aeo.txtAEOGridLinesThickness.setEnabled(true);
        main.aeo.txtAEOCellLinesColour.setEnabled(true);
        validator.isAEOCellLinesThicknessValid();
        validator.isAEOCellColourValid();
    }

    public void aEOGridNoEvent() {
        main.aeo.txtAEOGridLinesThickness.setEnabled(false);
        main.aeo.txtAEOGridLinesThickness.setBackground(SystemColor.text);
        main.aeo.txtAEOCellLinesColour.setEnabled(false);
        main.aeo.txtAEOCellLinesColour.setBackground(SystemColor.text);
        aEOCellScaleEvent();
    }

    public void aEOGridThicknessEvent() {
        validator.isAEOCellLinesThicknessValid();
    }

    public void aEOGridColourEvent() {
        validator.isAEOCellColourValid();
    }

    public void aEOExportEvent() {
        if (!validator.isAEOActivityLocked()) {
            main.aeo.setVisible(false);
            main.aeo.txtAEOCellScale.requestFocus();
            int scale = Integer.parseInt(main.aeo.txtAEOCellScale.getText());
            boolean showGrid = main.aeo.rdbtnAEOYes.isSelected();
            int thickness = Integer.parseInt(main.aeo.txtAEOGridLinesThickness.getText());
            String hex = main.aeo.txtAEOCellLinesColour.getText().replace("#", "");
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            int rgb = r;
            rgb = (rgb << 8) + g;
            rgb = (rgb << 8) + b;
            Color gridColour = new Color(rgb);
            exportSimulation(scale, showGrid, thickness, gridColour);
        }
    }

    public GUIValidator getValidator() {
        return validator;
    }

    public void setValidator(GUIValidator validator) {
        this.validator = validator;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public UnidimensionalSimulationParameter getSimulationParameter() {
        return simulationParameter;
    }

    public void setSimulationParameter(UnidimensionalSimulationParameter simulationParameter) {
        this.simulationParameter = simulationParameter;
    }

    public UnidimensionalSimulationController getSimulationController() {
        return simulationController;
    }

    public void setSimulationController(UnidimensionalSimulationController simulationController) {
        this.simulationController = simulationController;
    }

    public ActivityState getActivityState() {
        return activityState;
    }

    public boolean isOmitDiscardConfirmation() {
        return omitDiscardConfirmation;
    }

    public void setOmitDiscardConfirmation(boolean omitDiscardConfirmation) {
        this.omitDiscardConfirmation = omitDiscardConfirmation;
    }
}
