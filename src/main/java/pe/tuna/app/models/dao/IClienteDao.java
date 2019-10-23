package pe.tuna.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.tuna.app.models.entity.Cliente;


public interface IClienteDao extends JpaRepository<Cliente, Long> {

}
