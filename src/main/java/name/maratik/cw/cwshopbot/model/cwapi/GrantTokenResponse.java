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

/**
 * @author <a href="mailto:maratik@yandex-team.ru">Marat Bukharov</a>
 */
public class GrantTokenResponse extends ApiResponseBase<GrantTokenResponse.Payload> {

    public GrantTokenResponse(
        @JsonProperty("uuid") String uuid,
        @JsonProperty("result") ChatWarsApiResult result,
        @JsonProperty("payload") Payload payload
    ) {
        super(uuid, result, payload);
    }

    @Override
    public String toString() {
        return "GrantTokenResponse{} " + super.toString();
    }

    public static class Payload extends UserIdResponsePayload {
        private final String id;
        private final String token;

        public Payload(
            @JsonProperty("userId") long userId,
            @JsonProperty("id") String id,
            @JsonProperty("token") String token
        ) {
            super(userId);
            this.id = id;
            this.token = token;
        }

        public String getId() {
            return id;
        }

        public String getToken() {
            return token;
        }

        @Override
        public String toString() {
            return "Payload{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                "} " + super.toString();
        }
    }
}
