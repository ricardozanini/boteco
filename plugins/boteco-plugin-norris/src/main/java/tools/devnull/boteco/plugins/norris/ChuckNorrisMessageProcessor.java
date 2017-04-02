/*
 * The MIT License
 *
 * Copyright (c) 2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.boteco.plugins.norris;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.Name;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Words;

import java.io.IOException;

/**
 * A message processor that listens to mentions of "Chuck Norris"
 * and responds with facts
 */
@Name("chuck-norris")
@Words({"chuck norris", "chucknorris"})
public class ChuckNorrisMessageProcessor implements MessageProcessor {

  private static final Logger logger = LoggerFactory.getLogger(ChuckNorrisMessageProcessor.class);

  private final RestClient rest;

  public ChuckNorrisMessageProcessor(RestClient restClient) {
    this.rest = restClient;
  }

  @Override
  public void process(IncomeMessage message) {
    try {
      rest.get("https://api.chucknorris.io/jokes/random")
          .to(ChuckNorrisFact.class)
          .and(message::reply);
    } catch (IOException e) {
      logger.error("Error while fetching Chuck Norris fact", e);
    }
  }

}
