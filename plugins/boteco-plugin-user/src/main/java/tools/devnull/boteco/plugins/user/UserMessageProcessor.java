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

package tools.devnull.boteco.plugins.user;

import tools.devnull.boteco.AlwaysActive;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessingException;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;
import tools.devnull.boteco.plugins.user.spi.UserRepository;
import tools.devnull.boteco.user.User;
import tools.devnull.boteco.user.UserManager;

/**
 * A message processor for user operations.
 */
@Command("user")
@AlwaysActive
public class UserMessageProcessor implements MessageProcessor {

  private final UserManager userManager;
  private final UserRepository repository;

  public UserMessageProcessor(UserManager userManager, UserRepository repository) {
    this.userManager = userManager;
    this.repository = repository;
  }

  @Override
  public void process(IncomeMessage message) {
    message.command()
        .on("new", userId -> {
          userManager.create(userId, message.location());
          message.reply("User created");
        })
        .on("delete", () -> {
          User user = message.user()
              .orElseThrow(() -> new MessageProcessingException("You're not registered."));
          this.repository.delete(user);
          message.reply("Your user is now deleted!");
        })
        .on("link", LinkRequest.class, request -> {
          userManager.link(message, request.userId(), request.channel(), request.target());
          message.reply("Link requested and will be effective after confirmation.");
        })
        .on("unlink", UnlinkRequest.class, request -> {
          User user = request.user();
          user.removeDestination(request.channel());
          this.repository.update(user);
          message.reply("The destination was removed from your user.");
        })
        .on("default", channel -> {
          User user = message.user()
              .orElseThrow(() -> new MessageProcessingException("You're not registered."));
          if (channel.isEmpty()) {
            channel = message.channel().id();
          }
          user.setPrimaryDestination(channel);
          this.repository.update(user);
          message.reply("Your new primary destination was saved!");
        })
        .execute();
  }

}
