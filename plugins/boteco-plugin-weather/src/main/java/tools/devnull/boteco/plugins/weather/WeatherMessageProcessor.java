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

package tools.devnull.boteco.plugins.weather;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.client.rest.RestClient;

import java.net.URI;

import static tools.devnull.boteco.message.MessageChecker.check;
import static tools.devnull.boteco.Predicates.command;

public class WeatherMessageProcessor implements MessageProcessor {

  private static final Logger logger = LoggerFactory.getLogger(WeatherMessageProcessor.class);

  private final RestClient restClient;

  public WeatherMessageProcessor(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public String id() {
    return "weather";
  }

  @Override
  public boolean canProcess(IncomeMessage message) {
    return check(message).accept(command("weather").withArgs());
  }

  @Override
  public void process(IncomeMessage message) {
    try {
      URI uri = new URIBuilder()
          .setScheme("https")
          .setHost("query.yahooapis.com")
          .setPath("/v1/public/yql")
          .addParameter("format", "json")
          .addParameter("q", "select item from weather.forecast where woeid in " +
              "(select woeid from geo.places(1) where text=\"" +
              message.command().arg() + "\") and u=\"c\"")
          .build();
      WeatherResults results = restClient.get(uri).to(WeatherResults.class).result();
      ContentFormatter formatter = message.channel().formatter();
      if (results.hasResult()) {
        message.reply(String.format("%s: %s - %s / %s",
            formatter.accent(results.text()),
            formatter.alternativeAccent(results.condition()),
            formatter.value(String.valueOf(results.temperatureInCelsius()) + "\u00BAC"),
            formatter.value(String.valueOf(results.temperatureInFahrenheits()) + "\u00BAF")));
      } else {
        message.reply(formatter.error("Could not find any weather for " + message.command().arg()));
      }
    } catch (Exception e) {
      logger.error("Error while fetching weather", e);
    }
  }

}
