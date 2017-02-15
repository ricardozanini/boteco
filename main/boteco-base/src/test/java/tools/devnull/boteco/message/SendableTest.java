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

package tools.devnull.boteco.message;

import org.junit.Test;
import tools.devnull.kodo.Spec;

import java.util.Objects;
import java.util.function.Predicate;

import static tools.devnull.kodo.Expectation.to;

public class SendableTest {

  @Test
  public void testHelper() {
    Spec.given(Sendable.message("lorem ipsum"))
        .expect(Sendable::message, to().be("lorem ipsum"))
        .expect(Sendable::title, to(beNull()))
        .expect(Sendable::url, to(beNull()))
        .expect(Sendable::priority, to().be(Priority.NORMAL));
  }

  @Test
  public void testAppend() {
    Spec.given(Sendable.message("lorem ipsum").append(" dolor sit amet"))
        .expect(Sendable::message, to().be("lorem ipsum dolor sit amet"))
        .expect(Sendable::title, to(beNull()))
        .expect(Sendable::url, to(beNull()))
        .expect(Sendable::priority, to().be(Priority.NORMAL));
  }

  @Test
  public void testPrepend() {
    Spec.given(Sendable.message("dolor sit amet").prepend("lorem ipsum "))
        .expect(Sendable::message, to().be("lorem ipsum dolor sit amet"))
        .expect(Sendable::title, to(beNull()))
        .expect(Sendable::url, to(beNull()))
        .expect(Sendable::priority, to().be(Priority.NORMAL));
  }

  private Predicate<String> beNull() {
    return Objects::isNull;
  }

}
