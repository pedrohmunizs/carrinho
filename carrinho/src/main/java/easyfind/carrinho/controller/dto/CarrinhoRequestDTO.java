package easyfind.carrinho.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class CarrinhoRequestDTO {
    @NotNull
    private Integer quantidade;
    @NotNull
    private Long produto;
    @NotNull
    private Long consumidor;
}
