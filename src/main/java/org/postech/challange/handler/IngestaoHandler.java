package org.postech.challange.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class IngestaoHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final DynamoDbClient dynamoDB = DynamoDbClient.create();
    private final LambdaClient lambdaClient = LambdaClient.create();
    String notificacaoFunctionName = System.getenv("NOTIFICACAO_FUNCTION_NAME");


    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {

        try {
            String body = (String) input.get("body");

            Map<String, Object> payload = mapper.readValue(body, Map.class);

            String descricao = payload.get("descricao").toString();
            int nota = Integer.parseInt(payload.get("nota").toString());

            String id = UUID.randomUUID().toString();
            String dataEnvio = LocalDateTime.now().toString();
            String urgencia = nota <= 3 ? "CRITICA" : "NORMAL";

            dynamoDB.putItem(PutItemRequest.builder()
                    .tableName("Feedbacks")
                    .item(Map.of(
                            "id", AttributeValue.builder().s(id).build(),
                            "descricao", AttributeValue.builder().s(descricao).build(),
                            "nota", AttributeValue.builder().n(String.valueOf(nota)).build(),
                            "urgencia", AttributeValue.builder().s(urgencia).build(),
                            "dataEnvio", AttributeValue.builder().s(dataEnvio).build()
                    ))
                    .build()
            );

            if ("CRITICA".equals(urgencia)) {

                Map<String, Object> payload1 = Map.of(
                        "descricao", descricao,
                        "nota", nota,
                        "urgencia", urgencia,
                        "dataEnvio", dataEnvio
                );

                try {
                    context.getLogger().log("Chamando Lambda de notificacao: " + notificacaoFunctionName);
                    lambdaClient.invoke(InvokeRequest.builder()
                            .functionName(notificacaoFunctionName)
                            .payload(
                                    software.amazon.awssdk.core.SdkBytes.fromUtf8String(
                                            mapper.writeValueAsString(payload1)
                                    )
                            )
                            .build()
                    );
                } catch (Exception e) {
                    context.getLogger().log("Erro ao chamar Lambda de notificacao: " + e.getMessage());
                }
            }

            return Map.of(
                    "statusCode", 200,
                    "body", "Processado com sucesso: " + id
            );

        } catch (Exception e) {
            context.getLogger().log("Erro: " + e.getMessage());

            return Map.of(
                    "statusCode", 500,
                    "body", "Erro interno ao processar avaliacao"
            );
        }
    }
}
