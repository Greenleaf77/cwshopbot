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
package name.maratik.cw.cwshopbot.model.cwapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author <a href="mailto:maratik@yandex-team.ru">Marat Bukharov</a>
 */
public class RequestStockResponse extends ApiResponseBase<RequestStockResponse.Payload> {

    public RequestStockResponse(
        @JsonProperty("uuid") String uuid,
        @JsonProperty("result") ChatWarsApiResult result,
        @JsonProperty("payload") Payload payload
    ) {
        super(uuid, result, payload);
    }

    @Override
    public String toString() {
        return "RequestStockResponse{} " + super.toString();
    }

    public static class Payload extends UserIdResponsePayload {
        private final Map<String, Integer> stock;

        public Payload(
            @JsonProperty("userId") long userId,
            @JsonProperty("stock") Map<String, Integer> stock
        ) {
            super(userId);
            this.stock = stock;
        }

        public Map<String, Integer> getStock() {
            return stock;
        }

        @Override
        public String toString() {
            return "Payload{" +
                "stock=" + stock +
                "} " + super.toString();
        }
    }
}
