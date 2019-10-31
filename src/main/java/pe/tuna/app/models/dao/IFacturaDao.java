package pe.tuna.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.tuna.app.models.entity.Factura;

public interface IFacturaDao extends JpaRepository<Factura, Long> {
}
