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

package tools.devnull.boteco.predicates;

import org.junit.Test;
import tools.devnull.boteco.Predicates;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageCommand;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageCommandPredicateTest {

  private IncomeMessage newMessage(String content) {
    IncomeMessage message = mock(IncomeMessage.class);
    when(message.content()).thenReturn(content);
    when(message.hasCommand()).thenReturn(false);
    return message;
  }

  private IncomeMessage newCommand(String name) {
    IncomeMessage message = mock(IncomeMessage.class);
    MessageCommand command = mock(MessageCommand.class);
    when(command.name()).thenReturn(name);
    when(message.content()).thenReturn(name);
    when(message.hasCommand()).thenReturn(true);
    when(message.command()).thenReturn(command);
    return message;
  }

  @Test
  public void test() {
    Predicate<IncomeMessage> predicate = Predicates.command("ping");
    assertTrue(predicate.test(newCommand("ping")));
    assertTrue(predicate.test(newCommand("PING")));
    assertFalse(predicate.test(newMessage("ping")));
    assertFalse(predicate.test(newCommand("pong")));
  }

}
