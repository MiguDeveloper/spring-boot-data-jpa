package pe.tuna.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.tuna.app.models.entity.Cliente;
import pe.tuna.app.models.entity.Factura;
import pe.tuna.app.models.entity.ItemFactura;
import pe.tuna.app.models.entity.Producto;
import pe.tuna.app.models.services.IClienteService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

    @Autowired
    private IClienteService clienteService;

    private static final Logger log = LoggerFactory.getLogger(FacturaController.class);

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Model model, RedirectAttributes flash) {
        Cliente cliente = clienteService.findOne(clienteId);

        if (cliente == null) {
            flash.addFlashAttribute("danger", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }
        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Crear factura");

        return "factura/form";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody
    List<Producto> cargarProductos(@PathVariable String term) {
        return clienteService.findProductoByNombre(term);
    }

    @PostMapping("/form")
    public String guardar(@Valid Factura factura,
                          BindingResult result,
                          Model model,
                          @RequestParam(name = "item_id[]",required = false) Long[] itemId,
                          @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
                          RedirectAttributes flash,
                          SessionStatus status){

        if (result.hasErrors()){
            model.addAttribute("titulo", "Crear factura");
            return "factura/form";
        }

        if (itemId == null || itemId.length == 0){
            model.addAttribute("titulo", "Crear factura");
            model.addAttribute("danger", "Error: la factura debe tener al menos un producto");
            return "factura/form";
        }

        for (int i=0; i<itemId.length; i++){
            Producto producto = clienteService.findProductoById(itemId[i]);
            ItemFactura linea = new ItemFactura();
            linea.setProducto(producto);
            linea.setCantidad(cantidad[i]);
            factura.addItemFactura(linea);
            log.info("[DEBUG MIGUEL] ID: " + itemId[i].toString() + ", CANTIDAD: " + cantidad[i].toString());
        }

        clienteService.saveFactura(factura);

        status.setComplete();
        flash.addFlashAttribute("success","Factura creada con éxito");

        return "redirect:/ver/" + factura.getCliente().getId();
    }
}
