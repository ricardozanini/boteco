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

package tools.devnull.boteco.plugins.timebomb;

import java.util.function.Consumer;

public class TimeBomb {

  private final String code;
  private int ticks;
  private int attempts;
  private boolean defused;
  private boolean active;

  private Consumer<Integer> tickListener = tick -> {
  };
  private Consumer<Feedback> missListener = feedback -> {
  };
  private Consumer<String> defuseListener = code -> {
  };
  private Consumer<String> blowListener = code -> {
  };

  public TimeBomb(String code,
                  int ticks,
                  int attempts) {
    this.code = code;
    this.ticks = ticks;
    this.attempts = attempts;
    this.defused = false;
    this.active = true;
  }

  public TimeBomb onTick(Consumer<Integer> listener) {
    this.tickListener = tickListener.andThen(listener);
    return this;
  }

  public TimeBomb onMiss(Consumer<Feedback> listener) {
    this.missListener = missListener.andThen(listener);
    return this;
  }

  public TimeBomb onDefuse(Consumer<String> listener) {
    this.defuseListener = defuseListener.andThen(listener);
    return this;
  }

  public TimeBomb onBlow(Consumer<String> listener) {
    this.blowListener = blowListener.andThen(listener);
    return this;
  }

  public String code() {
    return code;
  }

  public int attempts() {
    return attempts;
  }

  public boolean defuse(String value) {
    attempts--;
    if (code.equals(value)) {
      defused = true;
      active = false;
      defuseListener.accept(code);
    } else {
      if (attempts == 0) {
        active = false;
        blowListener.accept(code);
      } else {
        missListener.accept(new Feedback(code, value, attempts));
      }
    }
    return defused;
  }

  public void tick() {
    if (active) {
      tickListener.accept(ticks);
      ticks--;
      if (ticks == 0) {
        if (!defused) {
          blowListener.accept(code);
        }
        active = false;
      }
    } else {
      throw new IllegalStateException("The bomb is not active anymore");
    }
  }

}
