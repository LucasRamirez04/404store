package _store.envio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import _store.envio.model.Envio;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer> {
}