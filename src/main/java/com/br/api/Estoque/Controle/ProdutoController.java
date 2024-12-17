package com.br.api.Estoque.Controle;

import com.br.api.Estoque.Servico.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.br.api.Estoque.Modelo.Produto;

import com.br.api.Estoque.Repositorio.ProdutoRepository;
import com.br.api.Estoque.Servico.ProdutoService;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RelatorioService relatorioService;

    // LISTAR PRODUTOS METODO GET
    @GetMapping("/produtos")
    public List<Produto> listarProdutos() {
        return produtoService.listarTodos();
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isPresent()) {
            return ResponseEntity.ok(produto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // CRIAR PRODUTOS METODO POST
    @PostMapping("/criar")
    public ResponseEntity<String> criarProduto(@RequestBody Produto produto) {
        produtoService.criarProduto(produto);
        return ResponseEntity.ok("Produto criado com sucesso!");
    }

    // REGISTRAR ENTRADA DE PRODUTOS METODO PATCH
    @PatchMapping("/entrada")
    public ResponseEntity<?> registrarEntrada(@RequestBody Produto request) {
        try {
            Produto produtoAtualizado = produtoService.registrarEntrada(request);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar a entrada do produto");
        }
    }

    // REGISTRAR SAIDA DE PRODUTOS METODO PATCH
    @PatchMapping("/saida")
    public ResponseEntity<?> registrarSaida(@RequestBody Produto request) {
        try {
            Produto produtoAtualizado = produtoService.registrarSaida(request);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar a saída do produto");
        }
    }

    // EXCLUIR UM PRODUTO METODO DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirProduto(@PathVariable Long id) {
        produtoService.excluirProduto(id);
        return ResponseEntity.ok("Produto excluído com sucesso!");
    }

    // GERAR RELATÓRIO DE PRODUTOS METODO GET
    @GetMapping("/relatorio/produtos")
    public ResponseEntity<byte[]> gerarRelatorioProdutos(
        @RequestParam(defaultValue = "pdf") String formato,
        @RequestParam(required = false) String mes,  // Parâmetro opcional de mês
        @RequestParam(required = false) Integer validade // Parâmetro opcional de validade (ano)
    ) {
        try {
            // Validar o formato
            if (!formato.equalsIgnoreCase("pdf") && !formato.equalsIgnoreCase("excel")) {
                return ResponseEntity.badRequest().body("Formato inválido. Use 'pdf' ou 'excel'.".getBytes());
            }

            // Passar os parâmetros para o serviço de relatório
            byte[] relatorio = relatorioService.gerarRelatorioProdutos(formato, mes, validade);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, formato.equals("excel") ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" : "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio_produtos." + formato)
                .body(relatorio);

        } catch (Exception e) {
            // Log do erro e resposta com código de erro 500
            e.printStackTrace();
            return ResponseEntity.status(500).body(("Erro ao gerar o relatório: " + e.getMessage()).getBytes());
        }
    }
}
