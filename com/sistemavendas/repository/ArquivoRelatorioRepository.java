package com.sistemavendas.repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class ArquivoRelatorioRepository implements RelatorioRepository {
    private final String caminhoArquivo;

    public ArquivoRelatorioRepository(String caminhoArquivo){
        this.caminhoArquivo = caminhoArquivo;
    }


    @Override
    public void salvar(String conteudoComprovante) {
       try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            bw.write(conteudoComprovante);
            bw.newLine();
       } catch (Exception e) {
         throw new RuntimeException("Erro ao salvar o relatório no arquivo", e);
       }
    }

    @Override
    public List<String> lerTodos() {
       try {
            if (!Files.exists(Paths.get(caminhoArquivo))) {
                 return Collections.emptyList();
            }
            return Files.readAllLines(Paths.get(caminhoArquivo));
       }
       catch(IOException e){
           throw new RuntimeException("Erro ao ler o relatório de vendas", e);
       }
    }
    
}
