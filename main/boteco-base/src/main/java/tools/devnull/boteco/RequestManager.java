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

package tools.devnull.boteco;

/**
 * Interface that defines a component capable of managing {@link Request requests}.
 */
public interface RequestManager {

  /**
   * Creates a request for the given target
   *
   * @param target the target of the request
   * @param <T>    the type of the target
   * @return the request for the given target
   */
  <T> Request<T> request(T target);

  /**
   * Finds a request for the given token.
   *
   * @param token      the token that validates the request
   * @param objectType the type of the requested object
   * @return the object of the request that matches the given token.
   */
  <T> T find(String token, Class<T> objectType);

  /**
   * Similar to {@link #find(String, Class)}, but removes the request
   * from the backend.
   *
   * @param token the token that validates the request
   * @param objectType the type of the requested object
   * @return the object of the request that matches the given token.
   */
  <T> T pull(String token, Class<T> objectType);

}