package port;

import message.BaseMessage;
import se.sics.kompics.PortType;

public class EdgePort extends PortType {{
    positive(BaseMessage.class);
    negative(BaseMessage.class);
}}