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
package name.maratik.cw.cwshopbot.application.dao;

import name.maratik.cw.cwshopbot.model.Shop;
import name.maratik.cw.cwshopbot.model.ShopLine;
import name.maratik.cw.cwshopbot.model.cwasset.Assets;
import name.maratik.cw.cwshopbot.model.parser.ParsedShopEdit;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

/**
 * @author <a href="mailto:maratik@yandex-team.ru">Marat Bukharov</a>
 */
@Repository
public class ShopLineDao {
    private static final Logger logger = LogManager.getLogger(ShopLineDao.class);
    public static final String TABLE_NAME = "shopLine";
    public static final String ITEM_CODE_SHOP_CODE_INDEX = "itemCode-shopCode-index";

    private final Table shopLineTable;
    private final Assets assets;

    public ShopLineDao(DynamoDB dynamoDB, Assets assets) {
        shopLineTable = dynamoDB.getTable(TABLE_NAME);
        this.assets = assets;
    }

    public Shop getShopLines(Shop.Builder shopBuilder, String shopCode) {
        shopLineTable.query("shopCode", shopCode)
            .forEach(item -> shopBuilder.addShopLine(ShopLine.builder()
                .setItem(assets.getAllItems().get(item.getString("itemCode")))
                .setPrice(item.getInt("price"))
                .build()
            ));
        return shopBuilder.build();
    }

    public void putShopLines(ParsedShopEdit parsedShopEdit) throws DaoException {
        try {
            parsedShopEdit.getShopLines().stream()
                .map(ShopLine::of)
                .forEach(shopLine -> putShopLine(parsedShopEdit.getShopCode(), shopLine));
        } catch (Exception e) {
            throw new DaoException("Putting shopLines failed", e);
        }
    }

    public void putShopLines(Shop shop) throws DaoException {
        try {
            shop.getShopLines().forEach(shopLine -> putShopLine(shop.getShopCode(), shopLine));
        } catch (Exception e) {
            throw new DaoException("Putting shopLines failed", e);
        }
    }

    private void putShopLine(String shopCode, ShopLine shopLine) {
        logger.debug("Putting {},{} to db", shopCode, shopLine);
        PutItemOutcome outcome = shopLineTable.putItem(new Item()
            .withPrimaryKey(
                "shopCode", shopCode,
                "itemCode", shopLine.getItem().getId()
            ).withInt("price", shopLine.getPrice())
        );
        logger.debug("Result is {}", outcome);
    }

    public Map<String, List<ShopLine>> getAllShopLines() throws DaoException {
        logger.debug("Getting all shop lines");
        try {
            return ImmutableMap.copyOf(StreamSupport.stream(shopLineTable.scan().spliterator(), false)
                .map(item -> new AbstractMap.SimpleImmutableEntry<>(
                    item.getString("shopCode"),
                    ShopLine.builder()
                        .setItem(assets.getAllItems().get(item.getString("itemCode")))
                        .setPrice(item.getInt("price"))
                        .build()
                )).collect(groupingBy(
                    Map.Entry::getKey,
                    mapping(Map.Entry::getValue, toImmutableList())
                ))
            );
        } catch (Exception e) {
            throw new DaoException("Getting all shop lines failed", e);
        }
    }

    public void deleteLine(String shopCode, String itemCode) throws DaoException {
        logger.debug("Delete line for shop='{}', item='{}'", shopCode, itemCode);
        try {
            DeleteItemOutcome outcome = shopLineTable.deleteItem("shopCode", shopCode, "itemCode", itemCode);
            logger.debug("Result is {}", outcome);
        } catch (Exception e) {
            throw new DaoException("Delete line for shop='" + shopCode + "', item='" + itemCode + "' failed", e);
        }
    }
}
