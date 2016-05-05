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

package tools.devnull.boteco.channel.telegram;

import tools.devnull.boteco.ContentFormatter;

public class TelegramContentFormatter implements ContentFormatter {

  @Override
  public String accent(Object content) {
    return "*" + String.valueOf(content) + "*";
  }

  @Override
  public String alternativeAccent(Object content) {
    return "_" + String.valueOf(content) + "_";
  }

  @Override
  public String positive(Object content) {
    return value(content);
  }

  @Override
  public String negative(Object content) {
    return value(content);
  }

  @Override
  public String value(Object content) {
    return "`" + String.valueOf(content) + "`";
  }

  @Override
  public String error(Object content) {
    return "*" + String.valueOf(content) + "*";
  }

  @Override
  public String link(String title, String url) {
    return String.format("[%s](%s)", title, url);
  }

  @Override
  public String tag(Object content) {
    return String.format("[[%s]]", String.valueOf(content));
  }

  @Override
  public String mention(String user) {
    return "@" + user;
  }

}
