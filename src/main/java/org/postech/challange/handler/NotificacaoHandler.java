package org.postech.challange.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.ses.SesClient;

import java.util.Map;

public class NotificacaoHandler implements RequestHandler<Map<String, Object>, String> {

    private final SesClient sesClient = SesClient.create();

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {

        String descricao = input.get("descricao").toString();
        String urgencia = input.get("urgencia").toString();
        String dataEnvio = input.get("dataEnvio").toString();
        String nota = input.get("nota").toString();

        String corpo = String.format(
                "Feedback Crítico Recebido\n\nData: %s\nNota: %s\nUrgência: %s\nDescrição: %s",
                dataEnvio, nota, urgencia, descricao
        );

        try {
            sesClient.sendEmail(req -> req
                    .source("zezynhogamer@gmail.com")
                    .destination(d -> d.toAddresses("jose.franklin550@gmail.com"))
                    .message(msg -> msg
                            .subject(sub -> sub.data("ALERTA: Feedback Crítico"))
                            .body(body -> body.text(txt -> txt.data(corpo)))
                    )
            );

            return "E-mail enviado com sucesso";

        } catch (Exception e) {
            context.getLogger().log("Erro ao enviar e-mail: " + e.getMessage());
            return "Falha ao enviar e-mail";
        }
    }
}
