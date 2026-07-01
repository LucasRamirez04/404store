package _store.promocion.repository;

import _store.promocion.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {

    // permite buscar un cupón directamente por su código (lo usará el Service para validar)
    Optional<Promocion> findByCodigo(String codigo);
}