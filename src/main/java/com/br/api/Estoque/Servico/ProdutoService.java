package com.br.api.Estoque.Servico;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.br.api.Estoque.Modelo.Produto;
import com.br.api.Estoque.Repositorio.ProdutoRepository;
import jakarta.transaction.Transactional;


//ONDE AS FUNÇÕES ACIONAM O REPOSITORIO E REALIZAM AS ALTERAÇÕES SOLICITADAS EM CONEXÃO COM O BANCO DE DADOS
@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    //LISTAR OS PRODUTOS
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    //CRIAR UM PRODUTO
    public Produto criarProduto(Produto produto) {

        if (!StringUtils.hasLength(produto.getNome())) {
            throw new IllegalArgumentException("O nome do produto é obrigatório");
        }
        if (produto.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero");
        }
        if (!StringUtils.hasLength(produto.getMes())){
            throw new IllegalArgumentException("O mês precisa ser selecionado");
        }
        else {
            Produto novoProduto = produtoRepository.saveAndFlush(produto);
            return novoProduto;
        }
    }


    //REGISTRAR UMA ENTRADA DE PRODUTO
    @Transactional
    public Produto registrarEntrada(Produto produto) {
        Optional<Produto> prod = produtoRepository.findById(produto.getId());
        if(prod.isPresent()){
            Produto produtoEncontrado = prod.get();
            produtoEncontrado.setQuantidade(produtoEncontrado.getQuantidade() + produto.getQuantidade());
            return produtoRepository.save(produtoEncontrado);
        }else{
            throw new RuntimeException("Produto não encontrado com ID: " + produto.getId());
        }
    }

    //REGISTRAR UMA SAIDA DE PRODUTO
    public Produto registrarSaida(Produto produto) {
        Optional<Produto> prod = produtoRepository.findById(produto.getId());
        if (prod.isPresent()) {
            Produto produtoEncontrado = prod.get();
            if (produtoEncontrado.getQuantidade() < produto.getQuantidade()) {
                throw new RuntimeException("Quantidade solicitada maior que o estoque disponível");
            }
            produtoEncontrado.setQuantidade(produtoEncontrado.getQuantidade() - produto.getQuantidade());
            return produtoRepository.save(produtoEncontrado);
        }else{
            throw new RuntimeException("Produto não encontrado com ID: " + produto.getId());
        }
    }

    //EXCLUIR UM PRODUTO
    public void excluirProduto(Long id) {
        Produto objeto = produtoRepository.findById(id).get();
        produtoRepository.delete(objeto);
    }

    //BUSCAR POR ID
    public Produto findById(Long id) {
        
        produtoRepository.findById(id);
                return null;

    }
}
