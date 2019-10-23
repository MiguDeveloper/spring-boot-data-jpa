package pe.tuna.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.tuna.app.models.entity.Cliente;
import pe.tuna.app.models.services.IClienteService;

import javax.validation.Valid;

@Controller
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clienteService.findAll());

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
    public String guardar(@Valid Cliente cliente, BindingResult result, RedirectAttributes flash, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }

        String mensajeflash = cliente.getId() != null ? "Cliente editado correctamente" : "Cliente creado con exito";
        clienteService.save(cliente);
        flash.addFlashAttribute("success", mensajeflash);

        return "redirect:listar";
    }

    @GetMapping("/form/{id}")
    public String edit(@PathVariable Long id, RedirectAttributes flash, Model model) {
        Cliente cliente = null;
        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null){
                flash.addFlashAttribute("danger", "El cliente no existe");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("danger","El ID no puede ser cero");
            return "redirect:/listar";
        }

        model.addAttribute("titulo", "Editar cliente");
        model.addAttribute("cliente", cliente);

        return "form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        if (id > 0) {
            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exito");
        }


        return "redirect:/listar";
    }
}
