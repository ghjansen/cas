/*
 * CAS - Cellular Automata Simulator
 * Copyright (C) 2019  Guilherme Humberto Jansen
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

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.ghjansen.cas.ui.desktop.manager.EventManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class AEOFrame extends JDialog {
	private final static String FONT_NAME = "Lucida Grande";
	private JTextField txtAEOCellScale;
	private JTextField txtAEOGridLinesThickness;
	private JTextField txtAEOCellLinesColour;
	private JRadioButton rdbtnAEOYes;
	private JRadioButton rdbtnAEONo;
	private final ButtonGroup grpGridAEO = new ButtonGroup();
	private JButton btnAEOExport;
	private JLabel lblAEOStatus;
	public AEOFrame(final EventManager em, JFrame parent) {
		super(parent, "Advanced Export Options (ALPHA)", true);
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setBounds(100, 100, 300, 260);
		getContentPane().setLayout(null);
		JPanel panelAEO = new JPanel();
		panelAEO.setBounds(6, 6, 288, 177);
		panelAEO.setBorder(new TitledBorder(null, "Advanced Export Options", TitledBorder.LEFT, TitledBorder.TOP, new Font(FONT_NAME, Font.BOLD, 12), Color.BLACK));
		
		getContentPane().add(panelAEO);
		
		JLabel lblAEOWarning = new JLabel("WARNING: This is a development prototype!");
		lblAEOWarning.setBackground(Color.BLACK);
		lblAEOWarning.setForeground(Color.RED);
		lblAEOWarning.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		
		JLabel lblAEOCellScale = new JLabel("Cell Scale:");
		lblAEOCellScale.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		
		JLabel lblAEOShowGridLines = new JLabel("Show Grid Lines:");
		lblAEOShowGridLines.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		
		JLabel lblAEOGridLinesThickness = new JLabel("Grid Lines Thickness:");
		lblAEOGridLinesThickness.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		
		JLabel lblAEOGridLinesColour = new JLabel("Grid Lines Colour:");
		lblAEOGridLinesColour.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		
		txtAEOCellScale = new JTextField();
		txtAEOCellScale.setText("1");
		txtAEOCellScale.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		txtAEOCellScale.setColumns(10);
		txtAEOCellScale.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				em.aEOCellScaleEvent();
			}
			
			public void insertUpdate(DocumentEvent e) {
				em.aEOCellScaleEvent();
			}
			
			public void changedUpdate(DocumentEvent e) {
				em.aEOCellScaleEvent();
			}
		});
		
		rdbtnAEOYes = new JRadioButton("Yes");
		grpGridAEO.add(rdbtnAEOYes);
		rdbtnAEOYes.setEnabled(false);
		rdbtnAEOYes.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		rdbtnAEOYes.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					em.aEOGridYesEvent();
				}
			}
		});
		
		rdbtnAEONo = new JRadioButton("No");
		grpGridAEO.add(rdbtnAEONo);
		rdbtnAEONo.setEnabled(false);
		rdbtnAEONo.setSelected(true);
		rdbtnAEONo.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		rdbtnAEONo.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					em.aEOGridNoEvent();
				}
			}
		});
		
		txtAEOGridLinesThickness = new JTextField();
		txtAEOGridLinesThickness.setEnabled(false);
		txtAEOGridLinesThickness.setText("1");
		txtAEOGridLinesThickness.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		txtAEOGridLinesThickness.setColumns(10);
		txtAEOGridLinesThickness.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				em.aEOGridThicknessEvent();
			}
			
			public void insertUpdate(DocumentEvent e) {
				em.aEOGridThicknessEvent();
			}
			
			public void changedUpdate(DocumentEvent e) {
				em.aEOGridThicknessEvent();
			}
		});
		
		txtAEOCellLinesColour = new JTextField();
		txtAEOCellLinesColour.setEnabled(false);
		txtAEOCellLinesColour.setText("#888888");
		txtAEOCellLinesColour.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		txtAEOCellLinesColour.setColumns(10);
		txtAEOCellLinesColour.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				em.aEOGridColourEvent();
			}
			
			public void insertUpdate(DocumentEvent e) {
				em.aEOGridColourEvent();
			}
			
			public void changedUpdate(DocumentEvent e) {
				em.aEOGridColourEvent();
			}
		});
		
		GroupLayout gl_panelAEO = new GroupLayout(panelAEO);
		gl_panelAEO.setHorizontalGroup(
			gl_panelAEO.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelAEO.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelAEO.createParallelGroup(Alignment.LEADING)
						.addComponent(lblAEOWarning, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addComponent(lblAEOCellScale, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(txtAEOCellScale, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addComponent(lblAEOShowGridLines, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(rdbtnAEOYes)
							.addGap(12)
							.addComponent(rdbtnAEONo))
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addComponent(lblAEOGridLinesThickness)
							.addGap(12)
							.addComponent(txtAEOGridLinesThickness, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addComponent(lblAEOGridLinesColour, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(txtAEOCellLinesColour, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panelAEO.setVerticalGroup(
			gl_panelAEO.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelAEO.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAEOWarning, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panelAEO.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addGap(11)
							.addComponent(lblAEOCellScale, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtAEOCellScale, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelAEO.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addGap(4)
							.addComponent(lblAEOShowGridLines, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
						.addComponent(rdbtnAEOYes)
						.addComponent(rdbtnAEONo))
					.addGroup(gl_panelAEO.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addGap(9)
							.addComponent(lblAEOGridLinesThickness, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addGap(4)
							.addComponent(txtAEOGridLinesThickness, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_panelAEO.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addGap(11)
							.addComponent(lblAEOGridLinesColour, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelAEO.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtAEOCellLinesColour, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)))
					.addGap(76))
		);
		panelAEO.setLayout(gl_panelAEO);
		
		JButton btnAEOCancel = new JButton("Cancel");
		btnAEOCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				txtAEOCellScale.requestFocus();
			}
		});
		btnAEOCancel.setBounds(6, 205, 138, 29);
		btnAEOCancel.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		getContentPane().add(btnAEOCancel);
		
		btnAEOExport = new JButton("Export");
		btnAEOExport.setBounds(156, 205, 138, 29);
		btnAEOExport.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		btnAEOExport.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				em.aEOExportEvent();
			}
		});
		getContentPane().add(btnAEOExport);
		
		lblAEOStatus = new JLabel("");
		lblAEOStatus.setBounds(6, 185, 288, 16);
		lblAEOStatus.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
		getContentPane().add(lblAEOStatus);
		this.setVisible(false);
	}

	public JTextField getTxtAEOCellScale() {
		return txtAEOCellScale;
	}

	public JTextField getTxtAEOGridLinesThickness() {
		return txtAEOGridLinesThickness;
	}

	public JTextField getTxtAEOCellLinesColour() {
		return txtAEOCellLinesColour;
	}

	public JRadioButton getRdbtnAEOYes() {
		return rdbtnAEOYes;
	}

	public JRadioButton getRdbtnAEONo() {
		return rdbtnAEONo;
	}

	public JButton getBtnAEOExport() {
		return btnAEOExport;
	}

	public JLabel getLblAEOStatus() {
		return lblAEOStatus;
	}
}
