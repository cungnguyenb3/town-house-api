//package vn.com.pn.config;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//
//import java.text.SimpleDateFormat;
//
//@Provider
//public class JacksonConfig implements ContextResolver<ObjectMapper> {
//    private final ObjectMapper defaultMapper;
//    private final ObjectMapper specializedMapper;
//
//    public JacksonMapperProvider() {
//        defaultMapper = createDefaultMapper();
//        specializedMapper = createSpecializedMapper();
//    }
//
//    private static ObjectMapper createDefaultMapper() {
//        return new ObjectMapper()
//                .setSerializationInclusion(JsonInclude.Include.ALWAYS)
//                .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
//                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
//                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
//                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
//    }
//
//    private static ObjectMapper createSpecializedMapper() {
//        return new ObjectMapper()
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"))
//                .registerModule(new SpecializedModule1())
//                .registerModule(new SpecializedModule2());
//    }
//
//    @Override
//    public ObjectMapper getContext(Class<?> type) {
//        if (SomeType.isAssignableFrom(type)) {
//            return specializedMapper;
//        }
//        else {
//            return defaultMapper;
//        }
//    }
//}