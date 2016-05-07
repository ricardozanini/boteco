/*
 * The MIT License
 *
 * Copyright (c) 2016 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Permission  is hereby granted, free of charge, to any person obtaining
 * a  copy  of  this  software  and  associated  documentation files (the
 * "Software"),  to  deal  in the Software without restriction, including
 * without  limitation  the  rights to use, copy, modify, merge, publish,
 * distribute,  sublicense,  and/or  sell  copies of the Software, and to
 * permit  persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * The  above  copyright  notice  and  this  permission  notice  shall be
 * included  in  all  copies  or  substantial  portions  of the Software.
 *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT  WARRANTY OF ANY KIND,
 * EXPRESS  OR  IMPLIED,  INCLUDING  BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN  NO  EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM,  DAMAGES  OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT  OR  OTHERWISE,  ARISING  FROM,  OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE   OR   THE   USE   OR   OTHER   DEALINGS  IN  THE  SOFTWARE.
 */

package tools.devnull.boteco.plugins.stocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.ServiceLocator;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;

import java.io.IOException;
import java.util.function.Function;

import static tools.devnull.boteco.Predicates.command;
import static tools.devnull.boteco.message.MessageChecker.check;

public class StocksMessageProcessor implements MessageProcessor, ServiceLocator {

  private static final Logger logger = LoggerFactory.getLogger(StocksMessageProcessor.class);

  @Override
  public String id() {
    return "stocks";
  }

  @Override
  public boolean canProcess(IncomeMessage message) {
    return check(message).accept(
        command("stock").withArgs()
            .or(command("stocks").withArgs())
    );
  }

  @Override
  public void process(IncomeMessage message) {
    ContentFormatter f = message.channel().formatter();
    String query = message.command().arg();
    String url = "https://finance.google.com/finance/info?client=ig&q=" + query;
    try {
      locate(RestClient.class).get(url)
          .extract(json())
          .to(StockResult.class)
          .and(stock -> stock.reply(message))
          .orElse(() -> message.reply("%s: I didn't found results for %s",
              f.mention(message.sender()),
              f.accent(query))
          );
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }

  // extracts the json portion of the result
  private Function<String, String> json() {
    return body ->
        (body.contains("{") && body.contains("}")) ?
            body.substring(body.indexOf("{"), body.lastIndexOf("}") + 1) :
            body;

  }

}
