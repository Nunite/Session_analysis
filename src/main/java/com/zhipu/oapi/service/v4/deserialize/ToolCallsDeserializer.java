package com.zhipu.oapi.service.v4.deserialize;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zhipu.oapi.service.v4.model.ToolCalls;

import java.io.IOException;

/**
 * Deserializer that can build instances of {@link ToolCalls} from any
 * JSON content, using appropriate {@link ToolCalls} type.
 */
public class ToolCallsDeserializer extends BaseNodeDeserializer<ToolCalls> {

    private final static ObjectMapper MAPPER = new ObjectMapper();


    /**
     * Singleton instance of generic deserializer for {@link ToolCallsDeserializer}.
     * Only used for types other than JSON Object and Array.
     */
    private final static ToolCallsDeserializer instance = new ToolCallsDeserializer();

    public ToolCallsDeserializer() {
        // `null` means that explicit "merge" is honored and may or may not work, but
        // that per-type and global defaults do not enable merging. This because
        // some node types (Object, Array) do support, others don't.
        super(ToolCalls.class, null);
    }

    /**
     * Factory method for accessing deserializer for specific node type
     */
    public static JsonDeserializer<? extends JsonNode> getDeserializer(Class<?> nodeClass) {
        if (nodeClass == ObjectNode.class) {
            return ObjectDeserializer.getInstance();
        }
        // For others, generic one works fine
        return instance;
    }

    /*
    /**********************************************************
    /* Actual deserializer implementations
    /**********************************************************
     */

    @Override
    public ToolCalls getNullValue(DeserializationContext ctxt) {
        return null;
    }

    /**
     * Implementation that will produce types of any JSON nodes; not just one
     * deserializer is registered to handle (in case of more specialized handler).
     * Overridden by typed sub-classes for more thorough checking
     */
    @Override
    public ToolCalls deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentTokenId() == JsonTokenId.ID_START_OBJECT) {
            ObjectNode jsonNodes = deserializeObject(p, ctxt, ctxt.getNodeFactory());
            return new ToolCalls(jsonNodes);
        }
        return null;
    }

}
