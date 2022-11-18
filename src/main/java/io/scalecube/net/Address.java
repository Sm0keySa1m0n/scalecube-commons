package io.scalecube.net;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Address(String host, int port) implements Serializable {

  public static final Address NULL_ADDRESS = new Address("nullhost", 0);

  public static final Pattern ADDRESS_FORMAT = Pattern.compile("(?<host>^.*):(?<port>\\d+$)");

  public Address(String host, int port) {
    this.host = convertIfLocalhost(host);
    this.port = port;
  }

  @Override
  public String toString() {
    return this.host + ":" + this.port;
  }

  /**
   * Parses given host:port string to create Address instance.
   *
   * @param hostandport must come in form {@code host:port}
   */
  public static Address from(String hostandport) {
    if (hostandport == null || hostandport.isEmpty()) {
      throw new IllegalArgumentException("host-and-port string must be present");
    }

    Matcher matcher = ADDRESS_FORMAT.matcher(hostandport);
    if (!matcher.find()) {
      throw new IllegalArgumentException("can't parse host-and-port string from: " + hostandport);
    }

    String host = matcher.group(1);
    if (host == null || host.isEmpty()) {
      throw new IllegalArgumentException("can't parse host from: " + hostandport);
    }

    int port;
    try {
      port = Integer.parseInt(matcher.group(2));
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("can't parse port from: " + hostandport, ex);
    }

    return new Address(host, port);
  }

  /**
   * Getting local IP address by the address of local host. <b>NOTE:</b> returned IP address is
   * expected to be a publicly visible IP address.
   *
   * @throws RuntimeException wrapped {@link UnknownHostException}
   */
  public static InetAddress getLocalIpAddress() {
    try {
      return InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Checks whether given host string is one of localhost variants. i.e. {@code 127.0.0.1}, {@code
   * 127.0.1.1} or {@code localhost}, and if so - then node's public IP address will be resolved and
   * returned.
   *
   * @param host host string
   * @return local ip address if given host is localhost
   */
  private static String convertIfLocalhost(String host) {
    String result;
    switch (host) {
      case "localhost":
      case "127.0.0.1":
      case "127.0.1.1":
        result = getLocalIpAddress().getHostAddress();
        break;
      default:
        result = host;
    }
    return result;
  }
}
