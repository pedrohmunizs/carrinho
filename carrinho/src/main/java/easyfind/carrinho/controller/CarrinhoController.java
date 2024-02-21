package easyfind.carrinho.controller;

import easyfind.carrinho.client.ConsumidorClient;
import easyfind.carrinho.client.ProdutoClient;
import easyfind.carrinho.controller.dto.CarrinhoResponseDTO;
import easyfind.carrinho.controller.dto.CarrinhoRequestDTO;
import easyfind.carrinho.controller.dto.ConsumidorResponseDTO;
import easyfind.carrinho.controller.dto.ProdutoResponseDTO;
import easyfind.carrinho.controller.mapper.CarrinhoMapper;
import easyfind.carrinho.model.Carrinho;
import easyfind.carrinho.service.CarrinhoService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/carrinhos")
public class CarrinhoController {
    @Autowired
    private CarrinhoService carrinhoService;
    @Autowired
    private ConsumidorClient consumidorClient;
    @Autowired
    private ProdutoClient produtoClient;
    @GetMapping
    public ResponseEntity<List<CarrinhoResponseDTO>> carrinhoDoConsumidor(@RequestParam Long idConsumidor){

        try{
            ConsumidorResponseDTO consumidor = consumidorClient.buscarPorId(idConsumidor);
        }catch (FeignException e){

            int status = e.status();

            if (status == 404){
                return ResponseEntity.notFound().build();
            } else if (status == 400){
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.status(503).build();
            }
        }

        List<Carrinho> carrinho = carrinhoService.carrinhoDoConsumidor(idConsumidor);

        if (carrinho.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<CarrinhoResponseDTO> carrinhos = carrinho.stream().map(CarrinhoMapper::toCarrinhoDto).toList();
        return ResponseEntity.ok(carrinhos);
    }
    @PostMapping
    public ResponseEntity<Carrinho> adicionandoProduto(@Valid @RequestBody CarrinhoRequestDTO carrinho){

        try{
            ConsumidorResponseDTO consumidor = consumidorClient.buscarPorId(carrinho.getConsumidor());
            ProdutoResponseDTO produto = produtoClient.buscarPorId(carrinho.getProduto());
        }catch (FeignException e){

            int status = e.status();

            if (status == 404){
                return ResponseEntity.notFound().build();
            } else if (status == 400){
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.status(503).build();
            }
        }

        LocalDateTime dtH = LocalDateTime.now();
        return ResponseEntity.ok(carrinhoService.adicionar(carrinho,dtH));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<CarrinhoResponseDTO> editarProduto(@PathVariable Long id, @RequestParam Integer quantidade){
        Carrinho carrinho = carrinhoService.editar(id,quantidade);
        return ResponseEntity.ok(CarrinhoMapper.toCarrinhoDto(carrinho));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProduto(@PathVariable Long id){
        carrinhoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/consumidor/{idConsumidor}")
    public ResponseEntity<Void> esvaziarCarrinho(@PathVariable Long idConsumidor){

        try{
            ConsumidorResponseDTO consumidor = consumidorClient.buscarPorId(idConsumidor);
        }catch (FeignException e){

            int status = e.status();

            if (status == 404){
                return ResponseEntity.notFound().build();
            } else if (status == 400){
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.status(503).build();
            }
        }

        carrinhoService.esvaziarCarrinho(idConsumidor);

        return ResponseEntity.noContent().build();
    }
}
