package org.postech.challange.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Avaliacao {
    public String id;
    public String descricao;
    public int nota;
    public String urgencia;
    public String dataEnvio;
}