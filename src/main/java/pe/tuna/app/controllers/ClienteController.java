package pe.tuna.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pe.tuna.app.models.dao.IClienteDao;
import pe.tuna.app.models.entity.Cliente;

@Controller
public class ClienteController {

    @Autowired
    @Qualifier("clienteDaoJPA")
    private IClienteDao clienteDao;

    @GetMapping("/listar")
    public String listar(Model model){
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clienteDao.findAll());

        return "listar";
    }

    @GetMapping("/form")
    public String formCrear(Model model){
        Cliente cliente = new Cliente();
        model.addAttribute("titulo", "Formulario de cliente");
        model.addAttribute("cliente", cliente);

        return "form";
    }

    @PostMapping("/form")
    public String guardar(Cliente cliente){
        clienteDao.save(cliente);

        return "redirect:listar";
    }
}
