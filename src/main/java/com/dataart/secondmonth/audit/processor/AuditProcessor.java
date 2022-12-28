package com.dataart.secondmonth.audit.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;

@Slf4j
public class AuditProcessor {

    private final static String PROJECTION_POSTFIX = "Projection";
    private final static String PROJECTION_METHOD_PREFIX = "get";

    public static JsonNode parameters2Json(int parameterCount, Object[] argsValues, String[] argsNames, Class<?>[] projections) {
        final var mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .findAndRegisterModules();

        final var json = new AtomicReference<JsonNode>();
        json.set(mapper.createObjectNode());

        IntStream
                .range(0, parameterCount)
                .forEach(i -> {
                    final var clazz = argsValues[i].getClass();
                    final var nodeString = new AtomicReference<String>();

                    if (isPrimitiveOrWrapper(clazz) || clazz == String.class || projections.length == 0) {
                        try {
                            nodeString.set(mapper.writeValueAsString(argsValues[i]));
                        } catch (JsonProcessingException e) {
                            log.error("Cannot process obj to json.", e);
                        }
                    } else {
                        Arrays.stream(projections)
                                .filter(projection ->
                                        projection.getSimpleName().replace(PROJECTION_POSTFIX, "").equals(clazz.getSimpleName())
                                )
                                .findFirst()
                                .ifPresent(projection -> {
                                    final var instance = getProxyInstance(argsValues[i], clazz, projection);

                                    try {
                                        nodeString.set(mapper.writeValueAsString(instance));
                                    } catch (JsonProcessingException e) {
                                        log.error("Cannot process obj to json.", e);
                                    }
                                });
                    }

                    JsonNode node = mapper.createObjectNode();
                    try {
                        node = mapper.readTree(nodeString.get());
                    } catch (JsonProcessingException e) {
                        log.error("String with Json is corrupted.", e);
                    }

                    final var metaParentNode = mapper.createObjectNode();
                    final var metaNode = mapper.createObjectNode();

                    metaNode.put("className", clazz.getName());

                    metaParentNode.putIfAbsent("meta", metaNode);
                    metaParentNode.putIfAbsent("content", node);

                    final var jsonNode = (ObjectNode) json.get();
                    jsonNode.putIfAbsent(argsNames[i], metaParentNode);

                    json.set(jsonNode);

                });

        return json.get();
    }

    private static Object getProxyInstance(Object argsValue, Class<?> clazz, Class<?> projection) {
        return Proxy.newProxyInstance(
                projection.getClassLoader(),
                new Class[]{projection},
                (proxy, method, args) -> {
                    final var methodName = method.getName();
                    final var objFields = clazz.getFields();
                    final var objFieldsRestrictedAccess = clazz.getDeclaredFields();

                    final var optionalField =
                            Stream.concat(Arrays.stream(objFields), Arrays.stream(objFieldsRestrictedAccess))
                                    .filter(field ->
                                            field.getName().equalsIgnoreCase(methodName.replace(PROJECTION_METHOD_PREFIX, ""))
                                    )
                                    .findFirst();

                    if (optionalField.isPresent()) {
                        final var field = optionalField.get();
                        field.setAccessible(true);

                        final var projectionFieldTypeName = method.getReturnType().getSimpleName();
                        final var objectFieldTypeName = field.getType().getSimpleName();
                        final var fieldValue = field.get(argsValue);
                        if (fieldValue != null && projectionFieldTypeName.contains(PROJECTION_POSTFIX) &&
                                projectionFieldTypeName.replace(PROJECTION_POSTFIX, "").equalsIgnoreCase(objectFieldTypeName)) {
                            return getProxyInstance(field.get(argsValue), field.getType(), method.getReturnType());
                        }

                        return fieldValue;
                    }

                    return null;
                }
        );
    }

}
