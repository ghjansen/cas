/*
 * CAS - Cellular Automata Simulator
 * Copyright (C) 2020  Guilherme Humberto Jansen
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

package com.ghjansen.cas.ui.desktop.i18n;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public enum TranslationKey {

    PNL_RULE_TYPE("pnlRuleType"),
    RDBTN_ELEMENTARY("rdbtnElementary"),
    RDBTN_TOTALISTIC("rdbtnTotalistic"),
    PNL_RULE_CONFIG("pnlRuleConfig"),
    LBL_RULE_NUMBER("lblRuleNumber"),
    PNL_LIMITS("pnlLimits"),
    LBL_CELLS("lblCells"),
    LBL_ITERATIONS("lblIterations"),
    PNL_INITIAL_CONDITION("pnlInitialCondition"),
    RDBTN_UNIQUE_CELL("rdbtnUniqueCell"),
    RDBTN_RANDOM("rdbtnRandom"),
    PNL_CONTROL("pnlControl"),
    BTN_OPEN("btnOpen"),
    BTN_SAVE("btnSave"),
    BTN_EXPORT("btnExport"),
    LBL_STATUS("lblStatus"),
    PNL_VIEW("pnlView"),
    LANG_COMBO_0("langCombo0"),
    LANG_COMBO_1("langCombo1"),
    MSG_SIMULATION_IN_PROGRESS("msgSimulationInProgress"),
    MSG_CHECK_DISCARD("msgCheckDiscard"),
    MSG_CONFIRM_DIALOG("msgConfirmDialog"),
    CAS_FILE_EXTENSION("casFileExtension"),
    MSG_SAVE_DIALOG_TITLE("msgSaveDialogTitle"),
    MSG_SAVE_SUCCESS("msgSaveSuccess"),
    MSG_OPEN_DIALOG_TITLE("msgOpenDialogTitle"),
    PNG_FILE_EXTENSION("pngFileExtension"),
    MSG_EXPORT_DIALOG_TITLE("msgExportDialogTitle"),
    MSG_EXPORT_SUCCESS("msgExportSuccess"),
    MSG_SIMULATION_SUCCESS_WAITING("msgSimulationSuccessWaiting"),
    MSG_RENDERING_SUCCESS("msgRenderingSuccess"),
    ERR_SIMULATION_EXECUTION("errSimulationExecution"),
    ERR_OPEN_FILE_INVALID("errOpenFileInvalid"),
    ERR_OPEN_FILE_GENERIC("errOpenFileGeneric"),
    ERR_SIMULATION("errSimulation"),
    ERR_RULE_NUMBER("errRuleNumber"),
    ERR_CELLS("errCells"),
    ERR_ITERATIONS("errIterations"),
    ERR_TRANSLATION("errTranslation"),
    ERR_SAVE("errSave"),
    ERR_EXPORT("errExport");

    private String key;

    private TranslationKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
