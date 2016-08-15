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

package tools.devnull.boteco.plugins.subscription;

import tools.devnull.boteco.event.SubscriptionManager;
import tools.devnull.boteco.message.IncomeMessage;

import java.util.function.Consumer;

public class SubscriptionAdd implements Consumer<SubscriptionParameters> {

  private final SubscriptionManager subscriptionManager;
  private final IncomeMessage message;

  public SubscriptionAdd(SubscriptionManager subscriptionManager, IncomeMessage message) {
    this.subscriptionManager = subscriptionManager;
    this.message = message;
  }

  @Override
  public void accept(SubscriptionParameters parameters) {
    boolean alreadyRegistered = this.subscriptionManager.subscriptions(parameters.event()).stream()
        .anyMatch(subscription -> subscription.subscriber().channel().equals(parameters.channel()) &&
            subscription.subscriber().target().equals(parameters.target()));
    if (alreadyRegistered) {
      message.reply("Subscriber has already subscribed to this event!");
    } else {
      if (parameters.shouldRequestConfirmation()) {
        this.subscriptionManager
            .subscribe()
            .target(parameters.target())
            .ofChannel(parameters.channel())
            .withConfirmation()
            .toEvent(parameters.event());
        message.reply("The subscription will be added after confirmation!");
      } else {
        this.subscriptionManager
            .subscribe()
            .target(parameters.target())
            .ofChannel(parameters.channel())
            .toEvent(parameters.event());
        message.reply("Subscription added!");
      }
    }
  }
}
