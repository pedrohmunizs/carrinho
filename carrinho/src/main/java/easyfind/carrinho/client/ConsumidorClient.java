package easyfind.carrinho.client;

import easyfind.carrinho.controller.dto.ConsumidorResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

    @FeignClient(name = "consumidor", url = "https://localhost:8082/consumidores")
public interface ConsumidorClient {

    @GetMapping("/{id}")
    ConsumidorResponseDTO buscarPorId(@PathVariable Long id);
}

