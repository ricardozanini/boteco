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

package tools.devnull.boteco.plugins.karma;

import tools.devnull.boteco.plugin.Command;
import tools.devnull.boteco.plugin.Listener;
import tools.devnull.boteco.plugin.Plugin;

import java.util.Collections;
import java.util.List;

import static tools.devnull.boteco.plugin.Command.command;
import static tools.devnull.boteco.plugin.Listener.listenTo;

public class KarmaPlugin implements Plugin {

  @Override
  public String id() {
    return "karma";
  }

  @Override
  public String description() {
    return "Keeps track of karma points";
  }

  @Override
  public List<Listener> listensTo() {
    return Collections.singletonList(
        listenTo("Messages with terms ending with ++ or --")
            .respondWith("The actual karma after modifying the value")
    );
  }

  @Override
  public List<Command> availableCommands() {
    return Collections.singletonList(
        command("karma")
            .with("term")
            .does("Shows the actual karma of the given term (wildcard '*' supported)")
    );
  }

}
