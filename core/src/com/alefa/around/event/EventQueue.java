package com.alefa.around.event;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;

import java.util.PriorityQueue;

/**
 * Created by LEEFAMILY on 2018-04-19.
 */

public class EventQueue implements Listener<GameEvent> {

    /* -- Fields -- */
    private PriorityQueue<GameEvent> eventsQueue;

    /* -- Constructor -- */
    public EventQueue() {
        eventsQueue = new PriorityQueue<GameEvent>();
    }

    /* -- Public methods -- */
    public GameEvent[] getEvents() {
        GameEvent[] events = eventsQueue.toArray(new GameEvent[0]);
        eventsQueue.clear();
        return events;
    }

    @Override
    public void receive(Signal<GameEvent> signal, GameEvent object) {
        eventsQueue.add(object);
    }
}
