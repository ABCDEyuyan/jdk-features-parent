package com.nf.mvc.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import java.time.LocalDate;
import static com.nf.mvc.util.ObjectMapperUtils.getObjectMapper;

public class JacksonTest {
    @Test
    public void testJackson() throws Exception{
        JacksonEntity entity = new JacksonEntity(100, "abc", LocalDate.of(1999, 2, 18));
        ObjectMapper objectMapper = getObjectMapper();
        String json = objectMapper.writeValueAsString(entity);
        System.out.println("json = " + json);
    }
}
