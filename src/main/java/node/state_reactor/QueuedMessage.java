package node.state_reactor;

import node.state_reactor.event.StateEvent;
import se.sics.kompics.Handler;
import se.sics.kompics.KompicsEvent;

import java.util.List;

class QueuedMessage<T extends KompicsEvent> {
        public QueuedMessage(T message, Handler<T> handler, List<StateEvent> onEvents) {
            this.message = message;
            this.handler = handler;
            this.onEvents = onEvents;
        }

        private final T message;
        private final Handler<T> handler;
        private final List<StateEvent> onEvents;

        public boolean isSubscribed(StateEvent event) {
            return onEvents.contains(event);
        }

        public void deliver(){
            handler.handle(message);
        }
    }