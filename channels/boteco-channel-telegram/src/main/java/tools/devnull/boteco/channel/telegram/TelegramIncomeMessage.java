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

package tools.devnull.boteco.channel.telegram;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.User;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.boteco.message.CommandExtractor;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.ServiceLocator;
import tools.devnull.boteco.message.MessageSender;
import tools.devnull.boteco.message.Sender;

class TelegramIncomeMessage implements IncomeMessage {

  private static final long serialVersionUID = -7037612529067018573L;

  private final CommandExtractor extractor;
  private final TelegramPolling.Message message;
  private final Channel channel = new TelegramChannel();
  private final ServiceLocator serviceLocator;
  private final User user;

  TelegramIncomeMessage(CommandExtractor extractor,
                        TelegramPolling.Message message,
                        ServiceLocator serviceLocator,
                        User user) {
    this.extractor = extractor;
    this.message = message;
    this.serviceLocator = serviceLocator;
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
  public boolean hasCommand() {
    return extractor.isCommand(this);
  }

  @Override
  public MessageCommand command() {
    return extractor.extract(this);
  }

  @Override
  public void reply(String content) {
    if (isPrivate()) {
      replyMessage(String.valueOf(message.getFrom().id()), content);
    } else {
      replyMessage(String.valueOf(message.getChat().getId()), sender().mention() + ": " + content);
    }
  }

  @Override
  public void sendBack(String content) {
    serviceLocator.locate(MessageSender.class)
        .send(content)
        .to(String.valueOf(message.getChat().getId()))
        .through(channel().id());
  }

  private void replyMessage(String id, String content) {
    serviceLocator.locate(MessageSender.class)
        .send(content)
        .with("reply_to_message_id", message.getMessageId())
        .to(id)
        .through(channel().id());
  }

}
