package org.postech.challange.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.postech.challange.model.Avaliacao;

public class FeedbackService {
	public double calcularMedia(List<Avaliacao> lista) {
		return lista.stream().mapToInt(a -> a.nota).average().orElse(0.0);
	}

	public Map<String, Long> contarPorUrgencia(List<Avaliacao> lista) {
		return lista.stream().collect(Collectors.groupingBy(a -> a.urgencia, Collectors.counting()));
	}
}