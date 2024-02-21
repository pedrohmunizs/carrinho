package easyfind.carrinho.model;

import easyfind.carrinho.util.StatusCarrinho;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Carrinho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataHoraAlocacao;
    private Integer quantidade;
    private Long produto;
    private Long consumidor;
    @Enumerated(EnumType.STRING)
    private StatusCarrinho status;
}
