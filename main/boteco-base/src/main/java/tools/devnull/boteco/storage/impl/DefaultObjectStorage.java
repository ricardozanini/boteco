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

package tools.devnull.boteco.storage.impl;

import tools.devnull.boteco.storage.ObjectStorage;
import tools.devnull.boteco.storage.RemoveOperation;
import tools.devnull.boteco.storage.RetrieveOperation;
import tools.devnull.boteco.storage.Storable;
import tools.devnull.boteco.storage.StoreOperation;
import tools.devnull.boteco.storage.StoreValueSelector;
import tools.devnull.boteco.storage.ValueSelector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

/**
 * The default object storage. This class uses maps to store value and
 * should be used only for testing purposes.
 */
public class DefaultObjectStorage implements ObjectStorage {

  private final Map<String, Map<Serializable, Storable>> storage;
  private final Supplier<Map<Serializable, Storable>> supplier;

  public DefaultObjectStorage(Map<String, Map<Serializable,
      Storable>> storage, Supplier<Map<Serializable, Storable>> supplier) {
    this.storage = storage;
    this.supplier = supplier;
  }

  public DefaultObjectStorage() {
    this(new ConcurrentHashMap<>(20), () -> new ConcurrentHashMap<>(100));
  }

  private synchronized Map storage(String name) {
    if (storage.containsKey(name)) {
      return storage.get(name);
    }
    Map<Serializable, Storable> newStorage = supplier.get();
    storage.put(name, newStorage);
    return newStorage;
  }

  @Override
  public StoreOperation store() {
    return new StoreOperation() {
      @Override
      public <T extends Storable> StoreValueSelector<T> into(String store) {
        return value -> storage(store).put(value.id(), value);
      }
    };
  }

  @Override
  public RetrieveOperation retrieve() {
    return new RetrieveOperation() {
      @Override
      public <T extends Storable> ValueSelector<T> from(String store) {
        return new ValueSelector<T>() {
          @Override
          public T id(Serializable id) {
            return (T) storage(store).get(id);
          }

          @Override
          public List<T> all() {
            return new ArrayList<>((Collection<T>) storage(store).values());
          }

          @Override
          public List<T> where(Predicate filter) {
            return (List<T>) storage(store).values().stream().filter(filter).collect(toList());
          }
        };
      }
    };
  }

  @Override
  public RemoveOperation remove() {
    return new RemoveOperation() {
      @Override
      public <T extends Storable> ValueSelector<T> from(String store) {
        return new ValueSelector<T>() {
          @Override
          public T id(Serializable id) {
            return (T) storage(store).remove(id);
          }

          @Override
          public List<T> all() {
            try {
              return new ArrayList<>((Collection<T>) storage(store).values());
            } finally {
              storage(store).clear();
            }
          }

          @Override
          public List<T> where(Predicate filter) {
            List result = new ArrayList<>();
            Map map = storage(store);
            Set<Map.Entry<Serializable, Storable>> entrySet = map.entrySet();
            for (Map.Entry<Serializable, Storable> entry : entrySet) {
              if (filter.test(entry.getValue())) {
                result.add(map.remove(entry.getKey()));
              }
            }
            return result;
          }
        };
      }
    };
  }

}