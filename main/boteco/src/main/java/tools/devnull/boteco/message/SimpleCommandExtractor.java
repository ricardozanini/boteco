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

package tools.devnull.boteco.message;

import tools.devnull.trugger.Optional;

import java.io.Serializable;

/**
 * A command extractor that treats all messages as commands
 */
public class SimpleCommandExtractor implements CommandExtractor, Serializable {

  private static final long serialVersionUID = -3269566201774993002L;

  public Optional<MessageCommand> extract(Message message) {
    MessageCommand command;
    StringBuilder content = new StringBuilder(message.content());
    int firstSpace = content.indexOf(" ");
    if (firstSpace < 0) { // no arguments
      command = new ExtractedCommand(message, content.toString(), "");
    } else {
      command = new ExtractedCommand(message, content.substring(0, firstSpace),
          content.substring(firstSpace, content.length()).trim()
      );
    }
    return Optional.of(command);
  }

}
