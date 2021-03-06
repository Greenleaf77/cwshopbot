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
package name.maratik.cw.cwshopbot.model;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="mailto:maratik@yandex-team.ru">Marat Bukharov</a>
 */
public class Shop {
    private final long userId;
    private final String shopName;
    private final String charName;
    private final String shopCode;
    private final int maxOffersCount;
    private final String shopCommand;
    private final int shopNumber;
    private final List<ShopLine> shopLines;

    private Shop(long userId, String shopName, String charName, String shopCode, int maxOffersCount, String shopCommand,
                 int shopNumber, List<ShopLine> shopLines) {
        this.userId = userId;
        this.shopName = Objects.requireNonNull(shopName, "shopName");
        this.charName = Objects.requireNonNull(charName, "charName");
        this.shopCode = Objects.requireNonNull(shopCode, "shopCode");
        this.maxOffersCount = maxOffersCount;
        this.shopCommand = Objects.requireNonNull(shopCommand, "shopCommand");
        this.shopNumber = shopNumber;
        this.shopLines = Objects.requireNonNull(shopLines, "shopLines");
    }

    public long getUserId() {
        return userId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getCharName() {
        return charName;
    }

    public String getShopCode() {
        return shopCode;
    }

    public int getMaxOffersCount() {
        return maxOffersCount;
    }

    public String getShopCommand() {
        return shopCommand;
    }

    public int getShopNumber() {
        return shopNumber;
    }

    public List<ShopLine> getShopLines() {
        return shopLines;
    }

    @Override
    public String toString() {
        return "Shop{" +
            "userId=" + userId +
            ", shopName='" + shopName + '\'' +
            ", charName='" + charName + '\'' +
            ", shopCode='" + shopCode + '\'' +
            ", maxOffersCount=" + maxOffersCount +
            ", shopCommand='" + shopCommand + '\'' +
            ", shopNumber=" + shopNumber +
            ", shopLines=" + shopLines +
            '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long userId;
        private String shopName;
        private String charName;
        private String shopCode;
        private int maxOffersCount;
        private String shopCommand;
        private int shopNumber;
        private final ImmutableList.Builder<ShopLine> shopLines = ImmutableList.builder();

        private Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setShopName(String shopName) {
            this.shopName = shopName;
            return this;
        }

        public Builder setCharName(String charName) {
            this.charName = charName;
            return this;
        }

        public Builder setShopCode(String shopCode) {
            this.shopCode = shopCode;
            return this;
        }

        public Builder setMaxOffersCount(int maxOffersCount) {
            this.maxOffersCount = maxOffersCount;
            return this;
        }

        public Builder setShopCommand(String shopCommand) {
            this.shopCommand = shopCommand;
            return this;
        }

        public Builder setShopNumber(int shopNumber) {
            this.shopNumber = shopNumber;
            return this;
        }

        public Builder addShopLine(ShopLine shopLine) {
            this.shopLines.add(Objects.requireNonNull(shopLine, "shopLine"));
            return this;
        }

        public Shop build() {
            return new Shop(userId, shopName, charName, shopCode, maxOffersCount, shopCommand, shopNumber,
                shopLines.build()
            );
        }
    }
}
