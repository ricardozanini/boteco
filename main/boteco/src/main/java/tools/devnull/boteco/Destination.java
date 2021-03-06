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

package tools.devnull.boteco;

import tools.devnull.boteco.client.jms.JmsDestination;

/**
 * An utility class to create destinations.
 */
public interface Destination {

  /**
   * Creates a queue destination based on the given queue name.
   *
   * @param name the queue name
   * @return a queue destination
   */
  static JmsDestination queue(String name) {
    return session -> session.createQueue(name);
  }

  /**
   * Creates a topic destination based on the given topic name.
   *
   * @param name the topic name
   * @return a topic destination
   */
  static JmsDestination topic(String name) {
    return session -> session.createTopic(name);
  }

  /**
   * Creates a message location based on the given channel id and target.
   *
   * @param channel the channel of the location
   * @return a component to select the target of the location
   */
  static TargetResultSelector<String, MessageLocation> channel(Channel channel) {
    return channel(channel.id());
  }

  /**
   * Creates a message location based on the given channel id and target.
   *
   * @param channelId the channel id of the location
   * @return a component to select the target of the location
   */
  static TargetResultSelector<String, MessageLocation> channel(String channelId) {
    return target -> new UserMessageLocation(channelId, target);
  }

}
