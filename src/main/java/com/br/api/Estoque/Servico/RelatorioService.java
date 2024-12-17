package com.br.api.Estoque.Servico;

import com.br.api.Estoque.Modelo.Produto;
import com.br.api.Estoque.Repositorio.ProdutoRepository;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @SuppressWarnings("deprecation")
    public byte[] gerarRelatorioProdutos(String formato, String mes, Integer validade) {
        List<Produto> produtos;

        try {
            // Aplicando filtros de mês e validade
            if (mes != null && !mes.isEmpty() && validade != null) {
                produtos = produtoRepository.findByMesAndValidade(mes, validade);
            } else if (mes != null && !mes.isEmpty()) {
                produtos = produtoRepository.findByMes(mes);
            } else if (validade != null) {
                produtos = produtoRepository.findByValidade(validade);
            } else {
                produtos = produtoRepository.findAll();
            }

            // Fonte de dados para o relatório
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(produtos);

            // Prepara o ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Estilos
            StyleBuilder boldStyle = DynamicReports.stl.style()
                .bold()
                .setFontSize(12);
            StyleBuilder columnStyle = DynamicReports.stl.style()
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(DynamicReports.stl.pen1Point());  
            StyleBuilder columnTitleStyle = DynamicReports.stl.style(boldStyle)
                .setBackgroundColor(new Color(72, 61, 139))
                .setForegroundColor(Color.WHITE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setPadding(1); 

            // Configuração das colunas
            TextColumnBuilder<String> nomeColuna = DynamicReports.col.column("Nome", "nome", DynamicReports.type.stringType())
                .setStyle(columnStyle);
            TextColumnBuilder<Integer> quantidadeColuna = DynamicReports.col.column("Quantidade", "quantidade", DynamicReports.type.integerType())
                .setStyle(columnStyle);
            TextColumnBuilder<Integer> validadeColuna = DynamicReports.col.column("Validade", "validade", DynamicReports.type.integerType())
                .setStyle(columnStyle);
            TextColumnBuilder<String> mesColuna = DynamicReports.col.column("Mês", "mes", DynamicReports.type.stringType())
                .setStyle(columnStyle);

            var relatorio = DynamicReports.report()
                .setPageFormat(PageType.A4)
                .setColumnTitleStyle(columnTitleStyle)
                .columns(nomeColuna, quantidadeColuna, validadeColuna, mesColuna)
                .title(
                    DynamicReports.cmp.text("Relatório de Produtos")
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setStyle(DynamicReports.stl.style().bold().setFontSize(18).setTopPadding(10)),
                    DynamicReports.cmp.verticalGap(20)
                )
                .pageFooter(
                    DynamicReports.cmp.horizontalList(
                        DynamicReports.cmp.text("Gerado por EstApp")
                            .setStyle(DynamicReports.stl.style().italic().setFontSize(10)),
                        DynamicReports.cmp.pageXofY()
                            .setStyle(DynamicReports.stl.style().setFontSize(10).setHorizontalAlignment(HorizontalAlignment.RIGHT))
                    )
                )
                // Estilo para linhas alternadas usando setDetailOddRowStyle e setDetailEvenRowStyle
                .setDataSource(dataSource);

            // Gerar o relatório no formato solicitado
            if ("pdf".equalsIgnoreCase(formato)) {
                relatorio.toPdf(outputStream);
            } else if ("excel".equalsIgnoreCase(formato)) {
                // Exportando para Excel sem estilos
                relatorio.toXlsx(Exporters.xlsxExporter(outputStream)
                        .setDetectCellType(true)
                        .setIgnoreCellBackground(true)
                        .setWhitePageBackground(true) 
                        .setIgnoreCellBorder(true) // Ignorar estilos no Excel
                        .setIgnorePageMargins(true));
                        
            } else {
                throw new IllegalArgumentException("Formato inválido: " + formato);
            }

            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar o relatório: " + e.getMessage(), e);
        }
    }
}
