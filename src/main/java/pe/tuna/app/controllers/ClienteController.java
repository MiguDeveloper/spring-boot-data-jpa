package pe.tuna.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pe.tuna.app.models.dao.IClienteDao;
import pe.tuna.app.models.entity.Cliente;

import javax.validation.Valid;

@Controller
public class ClienteController {

    @Autowired
    @Qualifier("clienteDaoJPA")
    private IClienteDao clienteDao;

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clienteDao.findAll());

        return "listar";
    }

    @GetMapping("/form")
    public String formCrear(Model model) {
        Cliente cliente = new Cliente();
        model.addAttribute("titulo", "Formulario de cliente");
        model.addAttribute("cliente", cliente);

        return "form";
    }

    @PostMapping("/form")
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }

        clienteDao.save(cliente);

        return "redirect:listar";
    }

    @GetMapping("/form/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Cliente cliente = null;
        if (id > 0) {
            cliente = clienteDao.findOne(id);
        } else {
            return "redirect:/listar";
        }

        model.addAttribute("titulo", "Editar cliente");
        model.addAttribute("cliente", cliente);

        return "form";
    }
}
