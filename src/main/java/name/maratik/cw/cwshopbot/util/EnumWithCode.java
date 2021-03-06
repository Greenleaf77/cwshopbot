//    cwshopbot
//    Copyright (C) 2018  Marat Bukharov.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU Affero General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU Affero General Public License for more details.
//
//    You should have received a copy of the GNU Affero General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
package name.maratik.cw.cwshopbot.util;

import java.util.Arrays;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

/**
 * @author <a href="mailto:maratik@yandex-team.ru">Marat Bukharov</a>
 */
public interface EnumWithCode {
    String getCode();

    class Util {
        public static <T extends EnumWithCode> Map<String, T> createCache(T[] values) {
            return Arrays.stream(values).collect(toImmutableMap(T::getCode, value -> value));
        }
    }
}
