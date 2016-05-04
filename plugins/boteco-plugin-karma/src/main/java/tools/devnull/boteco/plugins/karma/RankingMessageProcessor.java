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

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static tools.devnull.boteco.Predicates.command;
import static tools.devnull.boteco.message.MessageChecker.check;

public class RankingMessageProcessor implements MessageProcessor {

  private static final int ASC = 1;
  private static final int DESC = -1;

  private final MongoCollection<Document> karmas;
  private final Gson gson;

  public RankingMessageProcessor(MongoDatabase database) {
    this.karmas = database.getCollection("karmas");
    this.gson = new Gson();
  }

  @Override
  public String id() {
    return "rank";
  }

  @Override
  public boolean canProcess(IncomeMessage message) {
    return check(message).accept(command("rank").withArgs());
  }

  @Override
  public void process(IncomeMessage message) {
    ContentFormatter f = message.channel().formatter();
    List<Karma> result = findKarmas(message.command().arg());
    if (result.isEmpty()) message.reply(f.error("No karmas found"));
    result.forEach(karma -> message.reply("%s (%s)", f.accent(karma.name()), f.number(karma.value())));
  }

  private List<Karma> findKarmas(String term) {
    int order = DESC;
    List<Karma> result = new ArrayList<>();
    BasicDBObject query = new BasicDBObject();
    if (term.startsWith("!")) {
      term = term.substring(1, term.length());
      order = ASC;
    }
    if (term.contains("*")) {
      query.put("_id", new BasicDBObject("$regex", term.replace("*", ".*")).append("$options", "i"));
    } else {
      query.put("_id", term);
    }
    FindIterable<Document> documents = karmas.find(query).sort(new BasicDBObject().append("value", order)).limit(10);
    documents.forEach((Consumer<Document>) document -> result.add(gson.fromJson(document.toJson(), Karma.class)));
    return result;
  }

}