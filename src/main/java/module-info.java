module io.scalecube {

  exports io.scalecube.errors;
  exports io.scalecube.net;
  exports io.scalecube.reactor;
  exports io.scalecube.runners;
  exports io.scalecube.utils;

  requires jdk.unsupported;
  requires transitive reactor.core;
}
