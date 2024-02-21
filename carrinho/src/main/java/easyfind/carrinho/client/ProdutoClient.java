package easyfind.carrinho.client;

import easyfind.carrinho.controller.dto.ProdutoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produto", url = "https//localhost:8083/produtos")
public interface ProdutoClient {
    @GetMapping("/{id}")
    ProdutoResponseDTO buscarPorId(@PathVariable Long id);
}
