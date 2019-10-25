package pe.tuna.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.tuna.app.models.entity.Cliente;
import pe.tuna.app.models.services.IClienteService;
import pe.tuna.app.util.paginator.PageRender;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
        Path pathFoto = Paths.get("uploads").resolve(filename).toAbsolutePath();
        logger.info("[MIGUEL] pathFoto: " + pathFoto);

        Resource recurso = null;

        try{
            recurso = new UrlResource(pathFoto.toUri());
            if (!recurso.exists() || !recurso.isReadable()){
                throw new RuntimeException("Error: no se puede cargar imagen " + pathFoto.toString());
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);

    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        Cliente cliente = clienteService.findOne(id);
        if (cliente == null) {
            flash.addFlashAttribute("danger", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "cliente:" + cliente.getApellido());

        return "ver";
    }

    @GetMapping("/listar")
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        Pageable pageRequest = PageRequest.of(page, 4); // recordar que esto es con springboot 2
        // ya que en spring boot 1 se usa: new PageRequest

        Page<Cliente> clientes = clienteService.findAll(pageRequest);

        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);

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
    public String guardar(@Valid Cliente cliente, BindingResult result, RedirectAttributes flash, Model model,
                          @RequestParam("file") MultipartFile foto) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }
        if (!foto.isEmpty()) {
            // Ejemplo path dentro del proyecto pero no es conveniente ya que al desplegar se elimina
            //Path directorioRecursos = Paths.get("src//main//resources//static//uploads");

            // Path en el server local
            // Path directorioRecursos = Paths.get("/Users/miguelchinchay/Documents/img_spring-boot");
            // String rootPath = directorioRecursos.toFile().getAbsolutePath();

            // sobreescribimos el nombre del archivo para que sea unico
            String uniqueFileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
            Path rootPath = Paths.get("uploads").resolve(uniqueFileName);
            // Ahora debemos tener la ruta absoluta es decir desde c://...
            Path rootAbsolutePath = rootPath.toAbsolutePath();
            logger.info("El rootPath es: " + rootPath);// Path relativo al proyecto
            logger.info("El rootAbsolutePath es: " + rootAbsolutePath); // Path absoluto: c:/...

            try {
                /* Ejemplo con Files.write
                byte[] bytes = foto.getBytes();
                Path rutaCompleta = Paths.get(rootPath + "/" + foto.getOriginalFilename());
                Files.write(rutaCompleta, bytes);
                 */
                // con la ruta absoluta usaremos el metodo copy
                Files.copy(foto.getInputStream(), rootAbsolutePath);
                flash.addFlashAttribute("info", "Has subido correctamente ' "
                        + uniqueFileName
                        + "'");
                cliente.setFoto(uniqueFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

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
            if (cliente == null) {
                flash.addFlashAttribute("danger", "El cliente no existe");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("danger", "El ID no puede ser cero");
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
