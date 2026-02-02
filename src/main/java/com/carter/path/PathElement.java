package com.carter.path;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Waypoint.class, name = "waypoint"),
                @JsonSubTypes.Type(value = Translation.class, name = "translation"),
                @JsonSubTypes.Type(value = EventTrigger.class, name = "event_trigger"),
                @JsonSubTypes.Type(value = Rotation.class, name = "rotation") })
public abstract class PathElement {
        public String type;

        public PathElement() {
        }
}