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

package tools.devnull.boteco.plugins.user;

import tools.devnull.boteco.Destination;
import tools.devnull.boteco.plugins.user.spi.UserRepository;
import tools.devnull.boteco.request.Request;
import tools.devnull.boteco.request.RequestListener;
import tools.devnull.boteco.user.User;
import tools.devnull.boteco.user.UserNotFoundException;

public class UserLinkRequestListener implements RequestListener<UserRequest> {

  private final UserRepository repository;

  public UserLinkRequestListener(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public void onConfirm(Request<UserRequest> request) {
    UserRequest userRequest = request.object(UserRequest.class);
    String userId = userRequest.getUser();
    User user = this.repository.find(userId);
    if (user != null) {
      user.addDestination(Destination.channel(userRequest.getChannel()).to(userRequest.getTarget()));
      this.repository.update(user);
    } else {
      throw new UserNotFoundException("User " + userId + " doesn't exist!");
    }
  }

}
