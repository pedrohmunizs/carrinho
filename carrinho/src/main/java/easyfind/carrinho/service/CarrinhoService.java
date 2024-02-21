package easyfind.carrinho.service;

import easyfind.carrinho.controller.dto.CarrinhoRequestDTO;
import easyfind.carrinho.controller.dto.ConsumidorResponseDTO;
import easyfind.carrinho.controller.dto.PedidoResponseDTO;
import easyfind.carrinho.controller.dto.ProdutoResponseDTO;
import easyfind.carrinho.controller.mapper.CarrinhoMapper;
import easyfind.carrinho.exception.EntidadeNaoEncontradaException;
import easyfind.carrinho.model.Carrinho;
import easyfind.carrinho.repository.CarrinhoRepository;
import easyfind.carrinho.util.StatusCarrinho;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;

    public Carrinho adicionar(CarrinhoRequestDTO carrinho, LocalDateTime dtH){
        return carrinhoRepository.save(CarrinhoMapper.toCarrinho(carrinho,carrinho.getProduto(),carrinho.getConsumidor(), StatusCarrinho.NO_CARRINHO));
    }
    public List<Carrinho> carrinhoDoConsumidor(Long id){
        List<StatusCarrinho> statusList = Arrays.asList(StatusCarrinho.NO_CARRINHO, StatusCarrinho.PRODUTO_ALTERADO, StatusCarrinho.PRODUTO_INATIVO);
        return carrinhoRepository.findByConsumidorAndStatusIn(id, statusList);
    }
    public Carrinho editar(Long id, Integer quantidade){
        Carrinho carrinho = carrinhoRepository.findById(id).orElseThrow(
                ()->new EntidadeNaoEncontradaException("Carrinho não encontrado")
        );
        LocalDateTime dtH = LocalDateTime.now();

        carrinho.setQuantidade(quantidade);
        carrinho.setDataHoraAlocacao(dtH);

        return carrinho;
    }
    public void esvaziarCarrinho(Long idConsumidor){
        List<Carrinho> carrinhos = carrinhoRepository.findByConsumidorAndStatusIn(idConsumidor, Arrays.asList(StatusCarrinho.NO_CARRINHO, StatusCarrinho.PRODUTO_ALTERADO, StatusCarrinho.PRODUTO_INATIVO));
        List<Long> idsCarrinhos = carrinhos.stream().map(Carrinho::getId).collect(Collectors.toList());
        carrinhoRepository.esvaziarCarrinho(StatusCarrinho.RETIRADO, LocalDateTime.now(),idsCarrinhos);
    }
    public void removerProduto(Long id){
        Carrinho carrinho = carrinhoRepository.findById(id).orElseThrow(
                ()-> new EntidadeNaoEncontradaException("Carrinho não encontrado")
        );
        carrinho.setStatus(StatusCarrinho.RETIRADO);
        carrinho.setDataHoraAlocacao(LocalDateTime.now());
    }
    @KafkaListener(topics = {"compra-realizada"}, groupId = "carrinho")
    public void esvaziarCarrinhoComprado(PedidoResponseDTO eventReceived){
        List<Carrinho> carrinhos = carrinhoRepository.findByConsumidorAndStatusIn(eventReceived.getIdConsumidor(), Arrays.asList(StatusCarrinho.NO_CARRINHO));
        List<Long> idsCarrinhos = carrinhos.stream().map(Carrinho::getId).collect(Collectors.toList());
        carrinhoRepository.esvaziarCarrinho(StatusCarrinho.COMPRADO, LocalDateTime.now(),idsCarrinhos);
    }
    @KafkaListener(topics = {"produto-alterado"}, groupId = "carrinho")
    public void produtoAlterado(ProdutoResponseDTO dto,Long idProduto, String evento){
        StatusCarrinho status = StatusCarrinho.PRODUTO_ALTERADO;

        if (dto.getEvento().equals("Delete")){
            status=StatusCarrinho.PRODUTO_INATIVO;
        }
        carrinhoRepository.alterandoStatusPorProduto(status,LocalDateTime.now(),dto.getIdProduto(),Arrays.asList(StatusCarrinho.NO_CARRINHO));
    }
    @KafkaListener(topics = {"consumidor-deletado"}, groupId = "carrinho")
    public void consumidorInativo(ConsumidorResponseDTO dto){
        carrinhoRepository.alterandoStatusPorConsumidorDeletado(StatusCarrinho.RETIRADO, LocalDateTime.now(), dto.getIdConsumidor(),StatusCarrinho.NO_CARRINHO);
    }
}

