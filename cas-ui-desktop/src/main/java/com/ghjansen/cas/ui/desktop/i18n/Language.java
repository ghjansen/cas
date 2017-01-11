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

package com.ghjansen.cas.ui.desktop.i18n;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public enum Language {
	
	PORTUGUESE_BRAZIL("pt-BR"),
	ENGLISH_UNITED_KINGDOM("en-GB");
	
	private String langtag;

	private Language(String langtag) {
		this.langtag = langtag;
	}

	public String getLangtag() {
		return langtag;
	}
	
}
