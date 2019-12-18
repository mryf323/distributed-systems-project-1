package node.state_reactor;

import node.state_reactor.event.StateEvent;
import se.sics.kompics.Handler;
import se.sics.kompics.KompicsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Reactor {

    public Reactor() {
    }


    private List<QueuedMessage> messageList = new ArrayList<>();
    private List<StateEvent> occurred = new ArrayList<>();

    public void afterStateChanged(StateEvent e) {
        occurred.add(e);
    }


    public <T extends KompicsEvent> void queue(T message, Handler<T> handler, StateEvent... onEvents) {
        messageList.add(new QueuedMessage<>(message, handler, asList(onEvents)));
    }

    public void react() {
        ArrayList<StateEvent> recentOccurred = new ArrayList<>(this.occurred);
        this.occurred.clear();
        for (StateEvent e : recentOccurred) {
            List<QueuedMessage> toDeliver = messageList.stream()
                    .filter(m -> m.isSubscribed(e)).collect(Collectors.toList());
            messageList.removeAll(toDeliver);
            toDeliver.forEach(QueuedMessage::deliver);
        }


    }
}
