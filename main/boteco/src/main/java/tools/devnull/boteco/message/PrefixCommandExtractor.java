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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A command extractor that uses prefix to check if the message is a command.
 */
public class PrefixCommandExtractor implements CommandExtractor, Serializable {

  private static final long serialVersionUID = 3153909150938096646L;
  private final Pattern prefix;

  public PrefixCommandExtractor(String prefix) {
    this.prefix = Pattern.compile("^" + prefix, Pattern.CASE_INSENSITIVE);
  }

  public Optional<MessageCommand> extract(Message message) {
    boolean privateMessage = message.isPrivate();
    String content = message.content();
    Matcher matcher = prefix.matcher(content);
    if (matcher.find()) {
      content = content.substring(matcher.end());
    } else if(!privateMessage) {
      return Optional.empty();
    }
    ExtractedCommand command;
    int firstSpace = content.indexOf(" ");
    if (firstSpace < 0) { // no arguments
      command = new ExtractedCommand(message, content, "");
    } else {
      command = new ExtractedCommand(message, content.substring(0, firstSpace),
          content.substring(firstSpace, content.length()).trim()
      );
    }
    return Optional.of(command);
  }

}
