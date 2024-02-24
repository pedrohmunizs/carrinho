package easyfind.carrinho.repository;

import easyfind.carrinho.model.Carrinho;
import easyfind.carrinho.util.StatusCarrinho;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    List<Carrinho> findByConsumidorAndStatusIn(Long consumidor, List<StatusCarrinho> status);
    @Modifying
    @Transactional
    @Query("UPDATE Carrinho c SET c.status= :status, c.dataHoraAlocacao = :dtH WHERE c.id IN :id")
    Integer esvaziarCarrinho(StatusCarrinho status, LocalDateTime dtH, List<Long> id);
    @Modifying
    @Transactional
    @Query("UPDATE Carrinho c SET c.status= :status, c.dataHoraAlocacao = :dtH WHERE c.produto = :idProduto AND c.status IN :listStatus")
    Integer alterandoStatusPorProduto(StatusCarrinho status, LocalDateTime dtH, Long idProduto, List<StatusCarrinho> listStatus);
    @Modifying
    @Transactional
    @Query("UPDATE Carrinho c SET c.status= :status, c.dataHoraAlocacao = :dtH WHERE c.consumidor = :idConsumidor AND c.status IN :statusConsulta")
    Integer alterandoStatusPorConsumidorDeletado(StatusCarrinho status, LocalDateTime dtH, Long idConsumidor, List<StatusCarrinho> statusConsulta);

}
