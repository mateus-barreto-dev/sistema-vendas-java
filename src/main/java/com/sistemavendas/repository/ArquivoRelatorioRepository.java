package com.sistemavendas.repository;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ArquivoRelatorioRepository implements RelatorioRepository {
    private final String caminhoArquivo;

    public ArquivoRelatorioRepository(String caminhoArquivo){
        this.caminhoArquivo = caminhoArquivo;
    }


    @Override
    public void salvar(String conteudoComprovante) {
        try {
            Files.writeString(Path.of(caminhoArquivo), conteudoComprovante + "\n", 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o relatório no arquivo", e);
        }
    }



    @Override
    public List<String> lerTodos() {
        try (Stream<String> linhas = Files.lines(Path.of(caminhoArquivo))) {
            return linhas.toList();
        } catch (IOException e) {
            return List.of();
        }
    }

    public List<String> buscarNoHistorico(Predicate<String> criterio){
        List<String> comprovantes = new ArrayList<>();
        StringBuilder comprovante = new StringBuilder();
        boolean adicionarLinha = false;
        for (String string : lerTodos()) {
            if (string.startsWith("+")) {
                adicionarLinha = !adicionarLinha;
            }

            comprovante.append(string + "\n");

            if (!adicionarLinha) {
                comprovantes.add(comprovante.toString());
                comprovante.setLength(0);
            }
        }

        return comprovantes.stream().filter(criterio).toList();
    }
    
}
