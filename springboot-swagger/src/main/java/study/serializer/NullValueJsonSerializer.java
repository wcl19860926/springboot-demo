package study.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class NullValueJsonSerializer  extends JsonSerializer {

    /**
     * 如果为空写为""
     * @param value
     * @param gen
     * @param serializers
     * @throws IOException
     */
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value==null){
            gen.writeString("");
        }
    }
}
