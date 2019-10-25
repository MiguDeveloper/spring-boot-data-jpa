package pe.tuna.app.models.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.tuna.app.models.entity.Cliente;

import java.util.List;

public interface IClienteService {
    public List<Cliente> findAll();
    public Page<Cliente> findAll(Pageable pageable);
    public void save(Cliente cliente);
    public Cliente findOne(Long id);
    public void delete(Long id);
}
