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
package name.maratik.cw.eu.cwshopbot.model.cwasset;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import name.maratik.cw.eu.cwshopbot.util.EnumWithCode;

import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:maratik@yandex-team.ru">Marat Bukharov</a>
 */
public enum ItemType implements EnumWithCode {
    HEAVY_ARMOR("heavy armor", ItemClass.ARMOR),
    LIGHT_ARMOR("light armor", ItemClass.ARMOR),
    SHIELD("shield", ItemClass.SECONDARY_WEAPON),
    ROBE_ARMOR("robe armor", ItemClass.ARMOR),
    SWORD("sword", ItemClass.PRIMARY_WEAPON),
    SPEAR("spear", ItemClass.PRIMARY_WEAPON),
    BOW("bow", ItemClass.PRIMARY_WEAPON),
    BLUNT("blunt", ItemClass.PRIMARY_WEAPON),
    DAGGER("dagger", ItemClass.SECONDARY_WEAPON),
    TOOL("tool", ItemClass.SECONDARY_WEAPON);

    private final String code;
    private final ItemClass itemClass;
    private static final Map<String, ItemType> cache = Util.createCache(values());

    ItemType(String code, ItemClass itemClass) {
        this.code = code;
        this.itemClass = itemClass;
    }

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    public ItemClass getItemClass() {
        return itemClass;
    }

    @JsonCreator
    public static Optional<ItemType> findByCode(String code) {
        return Optional.ofNullable(cache.get(code));
    }
}