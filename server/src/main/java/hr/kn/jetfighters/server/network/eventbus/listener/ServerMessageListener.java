package hr.kn.jetfighters.server.network.eventbus.listener;

public interface ServerMessageListener<T> {
    void handle(T message);
}