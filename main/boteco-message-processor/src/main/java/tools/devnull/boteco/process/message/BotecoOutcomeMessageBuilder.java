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

package tools.devnull.boteco.process.message;

import tools.devnull.boteco.message.OutcomeMessage;
import tools.devnull.boteco.message.OutcomeMessageBuilder;
import tools.devnull.boteco.client.jms.JmsClient;

import java.util.HashMap;
import java.util.Map;

import static tools.devnull.boteco.client.jms.Destinations.queue;

public class BotecoOutcomeMessageBuilder implements OutcomeMessageBuilder {

  private final JmsClient client;
  private final String queueFormat;
  private final String content;
  private final Map<String, Object> headers;
  private String target;

  public BotecoOutcomeMessageBuilder(JmsClient client, String queueFormat, String content) {
    this.client = client;
    this.queueFormat = queueFormat;
    this.content = content;
    this.headers = new HashMap<>();
  }

  @Override
  public OutcomeMessageBuilder to(String target) {
    this.target = target;
    return this;
  }

  @Override
  public OutcomeMessageBuilder with(String headerName, Object headerValue) {
    this.headers.put(headerName, headerValue);
    return this;
  }

  @Override
  public void through(String channel) {
    client.send(new OutcomeMessage(target, content, headers)).to(queue(String.format(queueFormat, channel)));
  }

}