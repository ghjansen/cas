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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;

import com.ghjansen.cas.ui.desktop.i18n.Language;
import com.ghjansen.cas.ui.desktop.i18n.Translator;
import com.ghjansen.cas.ui.desktop.manager.EventManager;
import com.ghjansen.cas.ui.desktop.processing.SimulationViewProcessing;
import com.ghjansen.cas.ui.desktop.processing.TransitionsViewProcessing;
import com.ghjansen.cas.ui.desktop.processing.ViewCommonsProcessing;
import javax.swing.SwingConstants;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class Main {
	
	public EventManager em;
	private ViewCommonsProcessing viewCommons;
	public TransitionsViewProcessing transitionsView;
	public SimulationViewProcessing simulationView;

	public JFrame frame;
	public JTextField txtRuleNumber;
	public JTextField txtCells;
	public JTextField txtIterations;
	private final ButtonGroup grpRuleType = new ButtonGroup();
	public final ButtonGroup grpInitialCondition = new ButtonGroup();
	public JTable table;
	public JLabel lblStatus;
	public JScrollPane scrollPane;
	public JButton btnAdd;
	public JButton btnRemove;
	public JButton btnClean;
	public JButton btnDiscard;
	public JButton btnSimulateComplete;
	public JButton btnSimulateIteration;
	public JButton btnOpen;
	public JButton btnSave;
	public JButton btnExport;
	public JProgressBar progressBar;
	public JPanel pnlControl;
	public JComboBox langCombo;

	public JPanel pnlRuleType;
	public JRadioButton rdbtnElementary;
	public JRadioButton rdbtnTotalistic;
	public JPanel pnlRuleConfig;
	public JLabel lblRuleNumber;
	public JPanel pnlLimits;
	public JLabel lblCells;
	public JLabel lblIterations;
	public JPanel pnlInitialCondition;
	public JRadioButton rdbtnUniqueCell;
	public JRadioButton rdbtnRandom;
	public JPanel pnlView;
	private String lastStatusKey;

	public static void main(String[] args) {
		UIManager.put("Table.gridColor", new ColorUIResource(Color.gray));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		Translator.getInstance().setLanguage(Language.ENGLISH_UNITED_KINGDOM);
		initialize();
		em.setActivityState(ActivityState.CONFIGURING_RULE);
	}

	private void initialize() {
		em = new EventManager(this);
		viewCommons = new ViewCommonsProcessing();
		transitionsView = new TransitionsViewProcessing(viewCommons, em);
		simulationView = new SimulationViewProcessing(viewCommons, transitionsView);
		String os = System.getProperty("os.name").toLowerCase();
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("CAS");
		if(os.indexOf("win") >= 0){
			frame.setBounds(100, 100, 952, 667); //frame size for windows
		} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 ){ 
			frame.setBounds(100, 100, 947, 647); //frame size for linux
		} else {
			frame.setBounds(100, 100, 947, 665); //frame size for mac and others
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel pnlUnidimensional = new JPanel();
		pnlUnidimensional.setBounds(0, 0, 947, 645);
		frame.getContentPane().add(pnlUnidimensional);
		pnlUnidimensional.setLayout(null);
		
		pnlRuleType = new JPanel();
		pnlRuleType.setBounds(6, 6, 325, 54);
		pnlUnidimensional.add(pnlRuleType);
		pnlRuleType.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlRuleType"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		
		rdbtnElementary = new JRadioButton(Translator.getInstance().get("rdbtnElementary"));
		rdbtnElementary.setEnabled(false);
		rdbtnElementary.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					em.elementaryRuleTypeEvent();
				}
			}
		});
		grpRuleType.add(rdbtnElementary);
		rdbtnElementary.setSelected(true);
		rdbtnElementary.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		rdbtnTotalistic = new JRadioButton(Translator.getInstance().get("rdbtnTotalistic"));
		rdbtnTotalistic.setEnabled(false);
		rdbtnTotalistic.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					em.totalisticRuleTypeEvent();
				}
			}
		});
		grpRuleType.add(rdbtnTotalistic);
		rdbtnTotalistic.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		GroupLayout gl_pnlRuleType = new GroupLayout(pnlRuleType);
		gl_pnlRuleType.setHorizontalGroup(
			gl_pnlRuleType.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_pnlRuleType.createSequentialGroup()
					.addContainerGap()
					.addComponent(rdbtnElementary)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnTotalistic)
					.addContainerGap(115, Short.MAX_VALUE))
		);
		gl_pnlRuleType.setVerticalGroup(
			gl_pnlRuleType.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlRuleType.createSequentialGroup()
					.addGroup(gl_pnlRuleType.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnElementary)
						.addComponent(rdbtnTotalistic))
					.addContainerGap(8, Short.MAX_VALUE))
		);
		pnlRuleType.setLayout(gl_pnlRuleType);
		
		pnlRuleConfig = new JPanel();
		pnlRuleConfig.setBounds(6, 72, 325, 103);
		pnlUnidimensional.add(pnlRuleConfig);
		pnlRuleConfig.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlRuleConfig"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		
		lblRuleNumber = new JLabel(Translator.getInstance().get("lblRuleNumber"));
		lblRuleNumber.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		txtRuleNumber = new JTextField();
		txtRuleNumber.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				em.transitionsEvent();
			}
			@Override
			public void focusLost(FocusEvent e) {
				em.transitionsEvent();
			}
		});
		txtRuleNumber.setText("0");
		txtRuleNumber.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		txtRuleNumber.setColumns(10);
		txtRuleNumber.getDocument().addDocumentListener(new RuleNumberDocumentListener(this.em));
		
		
		TransitionsViewPanel pnlTransitionsView = new TransitionsViewPanel(transitionsView);
		GroupLayout gl_pnlTransitionsViewProcessing = new GroupLayout(pnlTransitionsView);
		gl_pnlTransitionsViewProcessing.setHorizontalGroup(
			gl_pnlTransitionsViewProcessing.createParallelGroup(Alignment.LEADING)
				.addGap(0, 293, Short.MAX_VALUE)
		);
		gl_pnlTransitionsViewProcessing.setVerticalGroup(
			gl_pnlTransitionsViewProcessing.createParallelGroup(Alignment.LEADING)
				.addGap(0, 37, Short.MAX_VALUE)
		);
		pnlTransitionsView.setLayout(gl_pnlTransitionsViewProcessing);
		GroupLayout gl_pnlRuleConfig = new GroupLayout(pnlRuleConfig);
		gl_pnlRuleConfig.setHorizontalGroup(
			gl_pnlRuleConfig.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlRuleConfig.createSequentialGroup()
					.addGap(5)
					.addComponent(pnlTransitionsView, GroupLayout.PREFERRED_SIZE, 302, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_pnlRuleConfig.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_pnlRuleConfig.createParallelGroup(Alignment.LEADING)	
							.addComponent(lblRuleNumber))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlRuleConfig.createParallelGroup(Alignment.LEADING)
							.addComponent(txtRuleNumber, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
				.addContainerGap())
		);
		gl_pnlRuleConfig.setVerticalGroup(
			gl_pnlRuleConfig.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlRuleConfig.createSequentialGroup()
					.addGap(6)
					.addComponent(pnlTransitionsView, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_pnlRuleConfig.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlRuleConfig.createSequentialGroup()
							.addGap(12)
							.addComponent(lblRuleNumber))
						.addGroup(gl_pnlRuleConfig.createSequentialGroup()
							.addGap(6)
							.addComponent(txtRuleNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		pnlRuleConfig.setLayout(gl_pnlRuleConfig);
		
		pnlLimits = new JPanel();
		pnlLimits.setBounds(6, 187, 325, 95);
		pnlUnidimensional.add(pnlLimits);
		pnlLimits.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlLimits"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		
		lblCells = new JLabel(Translator.getInstance().get("lblCells"));
		lblCells.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		txtCells = new JTextField();
		txtCells.setText("1");
		txtCells.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		txtCells.setColumns(10);
		txtCells.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				em.cellsEvent();
			}
			
			public void insertUpdate(DocumentEvent e) {
				em.cellsEvent();
			}
			
			public void changedUpdate(DocumentEvent e) {
				em.cellsEvent();
			}
		});
		
		lblIterations = new JLabel(Translator.getInstance().get("lblIterations"));
		lblIterations.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		txtIterations = new JTextField();
		txtIterations.setText("1");
		txtIterations.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		txtIterations.setColumns(10);
		txtIterations.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				em.iterationsEvent();
			}
			
			public void insertUpdate(DocumentEvent e) {
				em.iterationsEvent();
			}
			
			public void changedUpdate(DocumentEvent e) {
				em.iterationsEvent();
			}
		});
		GroupLayout gl_pnlLimits = new GroupLayout(pnlLimits);
		gl_pnlLimits.setHorizontalGroup(
			gl_pnlLimits.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLimits.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlLimits.createParallelGroup(Alignment.LEADING)
						.addComponent(lblIterations)
						.addComponent(lblCells))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlLimits.createParallelGroup(Alignment.LEADING)
						.addComponent(txtCells, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
						.addComponent(txtIterations, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_pnlLimits.setVerticalGroup(
			gl_pnlLimits.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLimits.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlLimits.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCells)
						.addComponent(txtCells, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlLimits.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIterations)
						.addComponent(txtIterations, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		pnlLimits.setLayout(gl_pnlLimits);
		
		pnlInitialCondition = new JPanel();
		pnlInitialCondition.setBounds(6, 294, 325, 85);
		pnlUnidimensional.add(pnlInitialCondition);
		pnlInitialCondition.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlInitialCondition"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		
		rdbtnUniqueCell = new JRadioButton(Translator.getInstance().get("rdbtnUniqueCell"));
		rdbtnUniqueCell.setEnabled(true);
		rdbtnUniqueCell.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					em.uniqueCellEvent();
				}
			}
		});
		rdbtnRandom = new JRadioButton(Translator.getInstance().get("rdbtnRandom"));
		rdbtnRandom.setEnabled(true);
		rdbtnRandom.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					em.uniqueCellEvent();
				}
			}
		});
		grpInitialCondition.add(rdbtnUniqueCell);
		grpInitialCondition.add(rdbtnRandom);
		rdbtnUniqueCell.setSelected(true);
		rdbtnUniqueCell.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		rdbtnRandom.setSelected(false);
		rdbtnRandom.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JRadioButton rdbtnInformPattern = new JRadioButton("Informar padrão:");
		rdbtnInformPattern.setEnabled(false);
		rdbtnInformPattern.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					em.informPatternCellEvent();
				}
			}
		});
		grpInitialCondition.add(rdbtnInformPattern);
		rdbtnInformPattern.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		rdbtnInformPattern.setVisible(false);
		
		Object[][] tabledata = {
				{ "inicio", "fim", "Cor" },
				{ "inicio", "fim", "Cor" },
				{ "inicio", "fim", "Cor" },
				{ "inicio", "fim", "Cor" },
				{ "inicio", "fim", "Cor" }};

		String columnheaders[] = { "", "", "" };

		table = new JTable(tabledata, columnheaders);
		table.setEnabled(false);
		table.setPreferredScrollableViewportSize(new Dimension(100, 50));
		table.setBackground(Color.WHITE);
		table.setTableHeader(null);
		table.setVisible(false);
		scrollPane = new JScrollPane(table);
		scrollPane.setEnabled(false);
		scrollPane.getViewport().setBackground(SystemColor.window);
		scrollPane.setVisible(false);
		
		btnAdd = new JButton("Adicionar");
		btnAdd.setEnabled(false);
		btnAdd.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnAdd.setVisible(false);
		
		btnRemove = new JButton("Remover");
		btnRemove.setEnabled(false);
		btnRemove.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnRemove.setVisible(false);
		
		btnClean = new JButton("Limpar");
		btnClean.setEnabled(false);
		btnClean.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnClean.setVisible(false);
		GroupLayout gl_pnlInitialCondition = new GroupLayout(pnlInitialCondition);
		gl_pnlInitialCondition.setHorizontalGroup(
			gl_pnlInitialCondition.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInitialCondition.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlInitialCondition.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
						.addComponent(rdbtnUniqueCell)
						.addComponent(rdbtnRandom)
						.addComponent(rdbtnInformPattern)
						.addGroup(gl_pnlInitialCondition.createSequentialGroup()
							.addComponent(btnAdd)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnRemove)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnClean, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_pnlInitialCondition.setVerticalGroup(
			gl_pnlInitialCondition.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInitialCondition.createSequentialGroup()
					.addContainerGap()
					.addComponent(rdbtnUniqueCell)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnRandom)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnInformPattern)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlInitialCondition.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAdd)
						.addComponent(btnClean)
						.addComponent(btnRemove))
					.addContainerGap(62, Short.MAX_VALUE))
		);
		
		pnlInitialCondition.setLayout(gl_pnlInitialCondition);
		
		pnlControl = new JPanel();
		pnlControl.setBounds(6, 391, 325, 124);
		pnlUnidimensional.add(pnlControl);
		pnlControl.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlControl"), TitledBorder.LEADING, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		
		btnDiscard = new JButton("");
		btnDiscard.setIcon(new ImageIcon(Main.class.getResource("fa-trash.png")));
		btnDiscard.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				em.discardEvent();
			}
		});
		
		btnSimulateComplete = new JButton("");
		btnSimulateComplete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				em.executeComplete();
			}
		});
		btnSimulateComplete.setIcon(new ImageIcon(Main.class.getResource("fa-play.png")));
		
		btnSimulateIteration = new JButton("");
		btnSimulateIteration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				em.executeIterationEvent();
			}
		});
		btnSimulateIteration.setEnabled(false);
		btnSimulateIteration.setIcon(new ImageIcon(Main.class.getResource("fa-step-forward.png")));
		
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setEnabled(true);
		
		btnOpen = new JButton(Translator.getInstance().get("btnOpen"));
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				em.openEvent();
			}
		});
		btnOpen.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		btnSave = new JButton(Translator.getInstance().get("btnSave"));
		btnSave.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				em.saveEvent();
			}
		});
		
		btnExport = new JButton(Translator.getInstance().get("btnExport"));
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				em.exportEvent();
			}
		});
		btnExport.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		GroupLayout gl_pnlControl = new GroupLayout(pnlControl);
		gl_pnlControl.setHorizontalGroup(
			gl_pnlControl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlControl.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlControl.createParallelGroup(Alignment.LEADING)
						.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
						.addGroup(gl_pnlControl.createSequentialGroup()
							.addComponent(btnDiscard, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSimulateComplete, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSimulateIteration, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
						.addGroup(gl_pnlControl.createSequentialGroup()
							.addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnExport, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_pnlControl.setVerticalGroup(
			gl_pnlControl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlControl.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlControl.createParallelGroup(Alignment.LEADING)
						.addComponent(btnDiscard)
						.addComponent(btnSimulateIteration)
						.addComponent(btnSimulateComplete))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_pnlControl.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOpen)
						.addComponent(btnSave)
						.addComponent(btnExport))
					.addGap(35))
		);
		pnlControl.setLayout(gl_pnlControl);
		
		JPanel pnlStatus = new JPanel();
		pnlStatus.setBounds(6, 613, 791, 27);
		pnlUnidimensional.add(pnlStatus);
		
		lblStatus = new JLabel();
		setStatus("lblStatus", "");
		lblStatus.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		GroupLayout gl_pnlStatus = new GroupLayout(pnlStatus);
		gl_pnlStatus.setHorizontalGroup(
			gl_pnlStatus.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlStatus.createSequentialGroup()
					.addComponent(lblStatus, GroupLayout.PREFERRED_SIZE, 771, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(20, Short.MAX_VALUE))
		);
		gl_pnlStatus.setVerticalGroup(
			gl_pnlStatus.createParallelGroup(Alignment.LEADING)
				.addComponent(lblStatus, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
		);
		pnlStatus.setLayout(gl_pnlStatus);
		
		pnlView = new JPanel();
		pnlView.setBounds(336, 6, 606, 606);
		pnlUnidimensional.add(pnlView);
		pnlView.setBorder(new TitledBorder(null, Translator.getInstance().get("pnlView"), TitledBorder.LEFT, TitledBorder.TOP, new Font("Lucida Grande", Font.BOLD, 12), Color.BLACK));
		
		SimulationViewPanel pnlSimulationView = new SimulationViewPanel(simulationView);
		GroupLayout gl_pnlView = new GroupLayout(pnlView);
		gl_pnlView.setHorizontalGroup(
			gl_pnlView.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_pnlView.createSequentialGroup()
					.addContainerGap()
					.addComponent(pnlSimulationView, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_pnlView.setVerticalGroup(
			gl_pnlView.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlView.createSequentialGroup()
					.addComponent(pnlSimulationView, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
					.addContainerGap())
		);
		GroupLayout gl_pnlViewProcessing = new GroupLayout(pnlSimulationView);
		gl_pnlViewProcessing.setHorizontalGroup(
			gl_pnlViewProcessing.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 558, Short.MAX_VALUE)
		);
		gl_pnlViewProcessing.setVerticalGroup(
			gl_pnlViewProcessing.createParallelGroup(Alignment.LEADING)
				.addGap(0, 553, Short.MAX_VALUE)
		);
		pnlSimulationView.setLayout(gl_pnlViewProcessing);
		pnlView.setLayout(gl_pnlView);
		
		Map<Object, Icon> icons = new HashMap<Object, Icon>();
		icons.put(Translator.getInstance().get("langCombo0"), new ImageIcon(Main.class.getResource("br.png")));
		icons.put(Translator.getInstance().get("langCombo1"), new ImageIcon(Main.class.getResource("en.png")));
		
		langCombo = new JComboBox(new Object[] {Translator.getInstance().get("langCombo0"), Translator.getInstance().get("langCombo1")});
		langCombo.setEnabled(true);
		langCombo.setSelectedIndex(1);
		langCombo.setRenderer(new IconListRenderer(icons));
		langCombo.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		langCombo.setBounds(809, 613, 138, 27);
		langCombo.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				em.languageEvent();
			}
		});
		pnlUnidimensional.add(langCombo);
		
		JLabel lblCopiright = new JLabel("Copyright \u00a9 2016  Guilherme Humberto Jansen");
		lblCopiright.setForeground(Color.GRAY);
		lblCopiright.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		lblCopiright.setHorizontalAlignment(SwingConstants.CENTER);
		lblCopiright.setBounds(6, 527, 325, 16);
		pnlUnidimensional.add(lblCopiright);
	}
	
	public void setStatus(String key, String info){
		lastStatusKey = key;
		lblStatus.setText(Translator.getInstance().get(key) + info);
	}
	
	public String getLastStatusKey(){
		return lastStatusKey;
	}
}
