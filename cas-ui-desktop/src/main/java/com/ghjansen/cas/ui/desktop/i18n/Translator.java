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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author Guilherme Humberto Jansen (contact.ghjansen@gmail.com)
 */
public class Translator {

    private static Translator instance;
    private static Gson gson;
    private static GsonBuilder gsonBuilder = new GsonBuilder();
    private Dictionary dictionary = new Dictionary();
    private Language language;

    public static Translator getInstance() {
        if (instance == null) {
            instance = new Translator();
            gson = gsonBuilder.create();
        }
        return instance;
    }

    public void setLanguage(Language language) throws IOException {
        this.language = language;
        String filename = language.getLangtag() + ".json";
        StringBuilder content = new StringBuilder();
        String line = null;
        InputStream is = null;
        BufferedReader br = null;
        is = this.getClass().getResourceAsStream(filename);
        br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while ((line = br.readLine()) != null) {
            content.append(line);
        }
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(content.toString(), type);
        dictionary.setMapping(map);
    }

    public Language getLanguage() {
        return language;
    }

    public String get(String key) {
        return dictionary.getMapping().get(key);
    }

}
