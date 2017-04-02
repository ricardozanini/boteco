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

package tools.devnull.boteco.channel.telegram;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.message.Message;
import tools.devnull.boteco.message.Sender;
import tools.devnull.boteco.user.User;

class TelegramIncomeMessage implements Message {

  private static final long serialVersionUID = -7037612529067018573L;

  private final TelegramPolling.Message message;
  private final Channel channel;
  private final User user;

  TelegramIncomeMessage(Channel channel,
                        TelegramPolling.Message message,
                        User user) {
    this.channel = channel;
    this.message = message;
    this.user = user;
  }

  @Override
  public Channel channel() {
    return channel;
  }

  @Override
  public User user() {
    return this.user;
  }

  @Override
  public String content() {
    String text = message.getText();
    return text == null ? "" : text;
  }

  @Override
  public boolean isPrivate() {
    return message.getChat().getId() > 0;
  }

  @Override
  public boolean isGroup() {
    return message.getChat().getId() < 0;
  }

  @Override
  public Sender sender() {
    return message.getFrom();
  }

  @Override
  public String target() {
    return String.valueOf(message.getChat().getId());
  }

  @Override
  public String replyTo() {
    return String.valueOf(message.getMessageId());
  }

}
