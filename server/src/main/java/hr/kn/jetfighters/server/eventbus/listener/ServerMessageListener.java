package hr.kn.jetfighters.server.eventbus.listener;

public interface ServerMessageListener<T> {
    void handle(T message);
}