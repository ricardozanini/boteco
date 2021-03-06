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

package tools.devnull.boteco.channel.irc;

import org.apache.camel.CamelContext;
import org.apache.camel.component.irc.IrcComponent;
import org.apache.camel.component.irc.IrcConfiguration;
import org.schwering.irc.lib.IRCConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.event.EventBus;
import tools.devnull.boteco.plugins.irc.spi.IrcChannelsRepository;
import tools.devnull.boteco.provider.Provider;

/**
 * An extension of the IrcComponent that can use custom listeners
 */
public class BotecoIrcComponent extends IrcComponent {

  private static final Logger logger = LoggerFactory.getLogger(BotecoIrcComponent.class);

  private final IrcChannelsRepository repository;
  private final EventBus bus;
  private BotecoIrcEventListener listener;
  private final CamelContext camelContext;
  private final IrcConnectionProvider provider;

  public BotecoIrcComponent(IrcChannelsRepository repository,
                            EventBus bus,
                            CamelContext camelContext,
                            ServiceRegistry registry) {
    this.repository = repository;
    this.bus = bus;
    this.camelContext = camelContext;
    this.provider = new IrcConnectionProvider();
    registry.registerProvider(IRCConnection.class, this.provider, true);
  }

  @Override
  protected IRCConnection createConnection(IrcConfiguration configuration) {
    IRCConnection connection = super.createConnection(configuration);
    listener = new BotecoIrcEventListener(connection, configuration, repository, bus);
    connection.addIRCEventListener(listener);
    this.provider.changeConnection(connection);
    return connection;
  }

  public boolean isDisconnected() {
    return listener != null && listener.isDisconnected();
  }

  public void reconnect() {
    try {
      doStop(); //force the cache to clear so we can refresh the irc connection later
      camelContext.stopRoute("boteco.message.to.irc");
      camelContext.stopRoute("boteco.message.from.irc");

      camelContext.startRoute("boteco.message.to.irc");
      camelContext.startRoute("boteco.message.from.irc");
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  static class IrcConnectionProvider implements Provider<IRCConnection> {

    private IRCConnection connection;

    public synchronized void changeConnection(IRCConnection newConnection) {
      this.connection = newConnection;
    }

    @Override
    public String id() {
      return "irc-channel";
    }

    @Override
    public String description() {
      return "Provides the current IRC connection used by the IRC Channel";
    }

    @Override
    public IRCConnection get() {
      return this.connection;
    }

  }

}
