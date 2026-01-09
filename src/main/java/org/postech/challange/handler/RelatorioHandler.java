package org.postech.challange.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RelatorioHandler implements RequestHandler<Map<String, Object>, String> {

    private final DynamoDbClient dynamoDB = DynamoDbClient.create();

    @Override
    public String handleRequest(Map<String, Object> event, Context context) {

        ScanResponse response = dynamoDB.scan(r -> r.tableName("Feedbacks"));

        int total = response.count();
        int somaNotas = 0;

        Map<String, Integer> porUrgencia = new HashMap<>();
        Map<String, Integer> porDia = new HashMap<>();

        for (Map<String, AttributeValue> item : response.items()) {

            int nota = Integer.parseInt(item.get("nota").n());
            somaNotas += nota;

            String urgencia = item.get("urgencia").s();
            porUrgencia.put(urgencia, porUrgencia.getOrDefault(urgencia, 0) + 1);

            String dataEnvio = item.get("dataEnvio").s();
            String dia = LocalDate.parse(dataEnvio.substring(0, 10)).toString();
            porDia.put(dia, porDia.getOrDefault(dia, 0) + 1);
        }

        double media = total > 0 ? (double) somaNotas / total : 0;

        context.getLogger().log("===== RELATORIO SEMANAL =====");
        context.getLogger().log("Total de feedbacks: " + total);
        context.getLogger().log("Media das notas: " + media);
        context.getLogger().log("Quantidade por urgencia: " + porUrgencia);
        context.getLogger().log("Quantidade por dia: " + porDia);

        return "Relatorio gerado com sucesso. Total: " + total + ", Media: " + media;
    }
}
