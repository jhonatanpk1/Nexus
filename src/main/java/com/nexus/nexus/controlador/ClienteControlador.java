package com.nexus.nexus.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexus.nexus.modelo.entidad.Cliente;
import com.nexus.nexus.modelo.servicio.IClienteServicio;


@Controller
@RequestMapping("/vista")
public class ClienteControlador {

	@Autowired
	private IClienteServicio clienteServicio;

	@GetMapping("/listar")
	public String listadoClientes(Model modelo) {
		List<Cliente> listadoClientes=clienteServicio.listarTodos();
		modelo.addAttribute("clientes", listadoClientes);
		return "/vista/listar";
	}

	@GetMapping("/registrocliente")
	public String Crear(Model modelo) {
		Cliente clientes= new Cliente();

		modelo.addAttribute("titulo", "Formulario: Nuevo Contacto");
		modelo.addAttribute("clientes", clientes);
		return "/vista/registrocliente";
	}

	@PostMapping("/save")
	public String guardar(@ModelAttribute Cliente clientes , Model modelo) {
		modelo.addAttribute("titulo", "Formulario: Nuevo Contacto");
		modelo.addAttribute("clientes", clientes);
		clienteServicio.guardar(clientes);
		return "redirect:/vista/listar";
	}

	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idClientes, Model modelo) {
		Cliente clientes = new Cliente();
		if(idClientes > 0) {
				clientes=clienteServicio.buscarPorId(idClientes);
				if(clientes == null) {
					return "redirect:/vista/listar";
				}
		}else {
			return "redirect:/vista/listar";
		}
		modelo.addAttribute("titulo", "Formulario: Editar Contacto");
		modelo.addAttribute("clientes", clientes);
		return "/vista/registrocliente";
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Integer idClientes) {
		Cliente clientes= new Cliente();
		if(idClientes > 0) {
			clientes=clienteServicio.buscarPorId(idClientes);
			if(clientes == null) {
				return "redirect:/vista/listar";
			}
		}else {
			return "redirect:/vista/listar";
		}

		clienteServicio.eliminar(idClientes);
		return "redirect:/vista/listar";
	}
}