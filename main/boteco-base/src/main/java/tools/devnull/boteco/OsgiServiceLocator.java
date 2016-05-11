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

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A ServiceLocator that lookups services by using the OSGi Registry
 */
public class OsgiServiceLocator implements ServiceLocator {

  public <T> T locate(Class<T> serviceClass) {
    BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
    ServiceReference<T> serviceReference = bundleContext.getServiceReference(serviceClass);
    return bundleContext.getService(serviceReference);
  }

  public <T> T locate(Class<T> serviceClass, String filter) {
    List<T> services = locateAll(serviceClass, filter);
    if (services.isEmpty()) {
      return null;
    } else {
      return services.get(0);
    }
  }

  public <T> List<T> locateAll(Class<T> serviceClass, String filter) {
    BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
    try {
      Collection<ServiceReference<T>> serviceReferences = bundleContext.getServiceReferences(serviceClass, filter);
      return serviceReferences.stream()
          .map(bundleContext::getService)
          .collect(Collectors.toList());
    } catch (InvalidSyntaxException e) {
      LoggerFactory.getLogger(getClass()).error("Error while locating service", e);
      return Collections.emptyList();
    }
  }

}
