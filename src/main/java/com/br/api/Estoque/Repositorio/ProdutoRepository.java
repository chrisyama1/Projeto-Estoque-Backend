package com.br.api.Estoque.Repositorio;
import com.br.api.Estoque.Modelo.Produto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//REPOSITORIO PARA FUNÇÕES PUBLICAS DE BUSCA
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Produto findByNome(String nome); 

    List<Produto> findByMesAndValidade(String mes, Integer validade);

    List<Produto> findByMes(String mes);
    List<Produto> findByValidade(Integer validade);
}


