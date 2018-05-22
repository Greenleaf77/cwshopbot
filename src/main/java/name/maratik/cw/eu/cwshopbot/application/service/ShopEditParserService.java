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
package name.maratik.cw.eu.cwshopbot.application.service;

import name.maratik.cw.eu.cwshopbot.model.parser.ParsedShopEdit;
import name.maratik.cw.eu.cwshopbot.parser.LoggingErrorListener;
import name.maratik.cw.eu.cwshopbot.parser.ParseException;
import name.maratik.cw.eu.cwshopbot.parser.generated.ShopEditLexer;
import name.maratik.cw.eu.cwshopbot.parser.generated.ShopEditParser;
import name.maratik.cw.eu.cwshopbot.parser.generated.ShopEditParserBaseListener;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;

import java.util.Optional;

import static name.maratik.cw.eu.cwshopbot.parser.ParserUtils.catchParseErrors;
import static name.maratik.cw.eu.cwshopbot.util.Utils.reformatMessage;

/**
 * @author <a href="mailto:maratik@yandex-team.ru">Marat Bukharov</a>
 */
@Component
public class ShopEditParserService implements CWParser<ParsedShopEdit> {
    private static final Logger logger = LogManager.getLogger(ShopEditParserService.class);

    private final ItemSearchService itemSearchService;

    public ShopEditParserService(ItemSearchService itemSearchService) {
        this.itemSearchService = itemSearchService;
    }

    @Override
    public Optional<ParsedShopEdit> parse(Message message) {
        String formattedMessage = reformatMessage(message);
        CodePointCharStream messageCharStream = CharStreams.fromString(formattedMessage);
        ShopEditLexer lexer = new ShopEditLexer(messageCharStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new LoggingErrorListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ShopEditParser parser = new ShopEditParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        return catchParseErrors(() -> {
            ParsedShopEdit.Builder builder = ParsedShopEdit.builder();
            ParseTreeWalker.DEFAULT.walk(new ShopEditParserListenerImpl(builder), parser.shopEdit());
            return Optional.of(builder.build());
        }, message);
    }

    private static class ShopEditParserListenerImpl extends ShopEditParserBaseListener {
        private final ParsedShopEdit.Builder builder;
        private ParsedShopEdit.ShopLine.Builder shopLineBuilder;

        public ShopEditParserListenerImpl(ParsedShopEdit.Builder builder) {
            this.builder = builder;
        }

        @Override
        public void exitShopName(ShopEditParser.ShopNameContext ctx) {
            logger.trace("exitShopName: {}", ctx::getText);
            builder.setShopName(ctx.getText());
        }

        @Override
        public void exitShopNumber(ShopEditParser.ShopNumberContext ctx) {
            logger.trace("exitShopNumber: {}", ctx::getText);
            String text = ctx.getText();
            try {
                builder.setShopNumber(Integer.parseInt(text));
            } catch (NumberFormatException e) {
                throw new ParseException("Unsupported shop number value: " + text, e);
            }
        }

        @Override
        public void exitCurrentOffers(ShopEditParser.CurrentOffersContext ctx) {
            logger.trace("exitCurrentOffers: {}", ctx::getText);
            String text = ctx.getText();
            try {
                builder.setOffersCount(Integer.parseInt(text));
            } catch (NumberFormatException e) {
                throw new ParseException("Unsupported current offers value: " + text, e);
            }
        }
    }
}