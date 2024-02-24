package easyfind.carrinho.controller.mapper;

import easyfind.carrinho.controller.dto.CarrinhoRequestDTO;
import easyfind.carrinho.controller.dto.CarrinhoResponseDTO;
import easyfind.carrinho.model.Carrinho;
import easyfind.carrinho.util.StatusCarrinho;

import java.time.LocalDateTime;

public class CarrinhoMapper {

    public static Carrinho toCarrinho(CarrinhoRequestDTO carrinhoRequestDTO, StatusCarrinho status){
        return new Carrinho(null, LocalDateTime.now(),carrinhoRequestDTO.getQuantidade(), carrinhoRequestDTO.getProduto(), carrinhoRequestDTO.getConsumidor(), status);
    }

    public static CarrinhoResponseDTO toCarrinhoDto(Carrinho carrinho){
        CarrinhoResponseDTO dto = new CarrinhoResponseDTO();

        dto.setConsumidor(carrinho.getConsumidor());
        dto.setQuantidade(carrinho.getQuantidade());
        dto.setProduto(carrinho.getProduto());

        return dto;
    }
}
