package app;

import model.Pedido;
import model.Envio;
import service.PedidoService;
import service.EnvioService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AppMenu {

    private final PedidoService pedidoService = new PedidoService();
    private final EnvioService envioService = new EnvioService();
    private final Scanner sc = new Scanner(System.in);

    public void iniciar() {

        String opcion;

        do {
            mostrarMenu();
            opcion = sc.nextLine().trim().toUpperCase();

            switch (opcion) {
                case "1" -> crearPedido();
                case "2" -> listarPedidos();
                case "3" -> buscarPedido();
                case "4" -> actualizarPedido();
                case "5" -> eliminarPedido();

                case "6" -> crearEnvio();
                case "7" -> listarEnvios();
                case "8" -> buscarEnvio();
                case "9" -> actualizarEnvio();
                case "10" -> eliminarEnvio();
                case "11" -> crearPedidoConEnvioTransaccion();


                case "X" -> System.out.println("Saliendo…");
                default -> System.out.println("Opcion invalida.");
            }

        } while (!opcion.equals("X"));
    }

    // =======================================
    // PEDIDO: CRUD
    // =======================================

    private void crearPedido() {
        try {
            System.out.println("=== Crear Pedido ===");

            Pedido p = new Pedido();

            System.out.print("Numero: ");
            p.setNumero(sc.nextLine().trim());

            System.out.print("Fecha (YYYY-MM-DD): ");
            p.setFecha(LocalDate.parse(sc.nextLine()));

            System.out.print("Cliente nombre: ");
            p.setClienteNombre(sc.nextLine());

            System.out.print("Total: ");
            p.setTotal(new BigDecimal(sc.nextLine()));

            System.out.print("Estado (NUEVO/FACTURADO/ENVIADO): ");
            p.setEstado(sc.nextLine().toUpperCase());

            p.setEliminado(false);

            long id = pedidoService.insertar(p);
            System.out.println("Pedido creado con ID: " + id);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarPedidos() {
        try {
            System.out.println("=== Lista de Pedidos ===");
            List<Pedido> lista = pedidoService.getAll();

            if (lista.isEmpty()) {
                System.out.println("No hay pedidos.");
                return;
            }

            for (Pedido p : lista) {
                System.out.println(p);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void buscarPedido() {
        try {
            System.out.print("ID del Pedido: ");
            long id = Long.parseLong(sc.nextLine());

            Optional<Pedido> op = pedidoService.getById(id);

            if (op.isPresent()) {
                System.out.println(op.get());
            } else {
                System.out.println("Pedido no encontrado.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void actualizarPedido() {
        try {
            System.out.print("ID del Pedido a actualizar: ");
            long id = Long.parseLong(sc.nextLine());

            Optional<Pedido> opt = pedidoService.getById(id);

            if (opt.isEmpty()) {
                System.out.println("Pedido no encontrado.");
                return;
            }

            Pedido p = opt.get();

            System.out.print("Nuevo numero (" + p.getNumero() + "): ");
            String input = sc.nextLine();
            if (!input.isBlank()) p.setNumero(input);

            System.out.print("Nueva fecha (" + p.getFecha() + "): ");
            input = sc.nextLine();
            if (!input.isBlank()) p.setFecha(LocalDate.parse(input));

            System.out.print("Nuevo cliente nombre (" + p.getClienteNombre() + "): ");
            input = sc.nextLine();
            if (!input.isBlank()) p.setClienteNombre(input);

            System.out.print("Nuevo total (" + p.getTotal() + "): ");
            input = sc.nextLine();
            if (!input.isBlank()) p.setTotal(new BigDecimal(input));

            System.out.print("Nuevo estado (" + p.getEstado() + "): ");
            input = sc.nextLine();
            if (!input.isBlank()) p.setEstado(input.toUpperCase());

            boolean ok = pedidoService.actualizar(p);

            System.out.println(ok ? "Pedido actualizado." : "No se pudo actualizar.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarPedido() {
        try {
            System.out.print("ID del Pedido a eliminar: ");
            long id = Long.parseLong(sc.nextLine());

            boolean ok = pedidoService.eliminar(id);
            System.out.println(ok ? "Pedido eliminado (logico)." : "No se pudo eliminar.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void crearPedidoConEnvioTransaccion() {
    try {
        System.out.println("=== Crear Pedido + Envío (Transacción) ===");

        // ---------- Datos del Pedido ----------
        Pedido p = new Pedido();

        System.out.print("Numero: ");
        p.setNumero(sc.nextLine().trim());

        System.out.print("Fecha (YYYY-MM-DD): ");
        p.setFecha(LocalDate.parse(sc.nextLine()));

        System.out.print("Cliente nombre: ");
        p.setClienteNombre(sc.nextLine());

        System.out.print("Total: ");
        p.setTotal(new BigDecimal(sc.nextLine()));

        System.out.print("Estado (NUEVO/FACTURADO/ENVIADO): ");
        p.setEstado(sc.nextLine().toUpperCase());

        p.setEliminado(false);


        // ---------- Datos del Envío ----------
        Envio e = new Envio();

        // El idPedido lo completa automáticamente el Service porque
        // usa el id generado del Pedido
        System.out.print("Tracking: ");
        e.setTracking(sc.nextLine());

        System.out.print("Empresa (ANDREANI/OCA/CORREO_ARG): ");
        e.setEmpresa(sc.nextLine().toUpperCase());

        System.out.print("Tipo (ESTANDAR/EXPRES): ");
        e.setTipo(sc.nextLine().toUpperCase());

        System.out.print("Costo: ");
        e.setCosto(new BigDecimal(sc.nextLine()));

        System.out.print("Fecha despacho (YYYY-MM-DD) [enter si null]: ");
        String f1 = sc.nextLine().trim();
        e.setFechaDespacho(f1.isBlank() ? null : LocalDate.parse(f1));

        System.out.print("Fecha estimada (YYYY-MM-DD) [enter si null]: ");
        String f2 = sc.nextLine().trim();
        e.setFechaEstimada(f2.isBlank() ? null : LocalDate.parse(f2));

        System.out.print("Estado envio (EN_PREPARACION/EN_TRANSITO/ENTREGADO): ");
        e.setEstado(sc.nextLine().toUpperCase());

        e.setEliminado(false);

        // ---- VINCULAR Pedido con Envío ----
        p.setEnvio(e);

        // ---- LLAMAR AL MÉTODO TRANSACCIONAL YA EXISTENTE ----
        long idCreado = pedidoService.insertar(p);

        System.out.println("✔ Pedido + Envío creados exitosamente. ID Pedido = " + idCreado);

    } catch (Exception ex) {
        System.out.println("✖ ERROR — Se realizó rollback, nada fue guardado");
        System.out.println("Detalle: " + ex.getMessage());
    }
}


    // =======================================
    // ENVIO: CRUD
    // =======================================

    private void crearEnvio() {
        try {
            System.out.println("=== Crear Envio ===");

            Envio e = new Envio();

            System.out.print("ID del Pedido asociado: ");
            e.setIdPedido(Long.parseLong(sc.nextLine()));

            System.out.print("Tracking: ");
            e.setTracking(sc.nextLine());

            System.out.print("Empresa (ANDREANI/OCA/CORREO_ARG): ");
            e.setEmpresa(sc.nextLine().toUpperCase());

            System.out.print("Tipo (ESTANDAR/EXPRES): ");
            e.setTipo(sc.nextLine().toUpperCase());

            System.out.print("Costo: ");
            e.setCosto(new BigDecimal(sc.nextLine()));

            System.out.print("Fecha despacho (YYYY-MM-DD) [enter si null]: ");
            String f1 = sc.nextLine().trim();
            e.setFechaDespacho(f1.isBlank() ? null : LocalDate.parse(f1));

            System.out.print("Fecha estimada (YYYY-MM-DD) [enter si null]: ");
            String f2 = sc.nextLine().trim();
            e.setFechaEstimada(f2.isBlank() ? null : LocalDate.parse(f2));

            System.out.print("Estado (EN_PREPARACION/EN_TRANSITO/ENTREGADO): ");
            e.setEstado(sc.nextLine().toUpperCase());

            e.setEliminado(false);

            long id = envioService.insertar(e);
            System.out.println("Envio creado con ID: " + id);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarEnvios() {
        try {
            System.out.println("=== Lista de Envios ===");
            List<Envio> lista = envioService.getAll();

            if (lista.isEmpty()) {
                System.out.println("No hay envios.");
                return;
            }

            for (Envio e : lista) {
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void buscarEnvio() {
        try {
            System.out.print("ID del Envio: ");
            long id = Long.parseLong(sc.nextLine());

            Optional<Envio> op = envioService.getById(id);

            if (op.isPresent()) {
                System.out.println(op.get());
            } else {
                System.out.println("Envio no encontrado.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void actualizarEnvio() {
        try {
            System.out.print("ID del Envio a actualizar: ");
            long id = Long.parseLong(sc.nextLine());

            Optional<Envio> opt = envioService.getById(id);

            if (opt.isEmpty()) {
                System.out.println("Envio no encontrado.");
                return;
            }

            Envio e = opt.get();

            System.out.print("Nuevo tracking (" + e.getTracking() + "): ");
            String input = sc.nextLine();
            if (!input.isBlank()) e.setTracking(input);

            System.out.print("Nueva empresa (" + e.getEmpresa() + "): ");
            input = sc.nextLine();
            if (!input.isBlank()) e.setEmpresa(input.toUpperCase());

            System.out.print("Nuevo tipo (" + e.getTipo() + "): ");
            input = sc.nextLine();
            if (!input.isBlank()) e.setTipo(input.toUpperCase());

            System.out.print("Nuevo costo (" + e.getCosto() + "): ");
            input = sc.nextLine();
            if (!input.isBlank()) e.setCosto(new BigDecimal(input));

            System.out.print("Nueva fecha despacho (" + e.getFechaDespacho() + "): ");
            input = sc.nextLine();
            if (!input.isBlank()) e.setFechaDespacho(LocalDate.parse(input));

            System.out.print("Nueva fecha estimada (" + e.getFechaEstimada() + "): ");
            input = sc.nextLine();
            if (!input.isBlank()) e.setFechaEstimada(LocalDate.parse(input));

            System.out.print("Nuevo estado (" + e.getEstado() + "): ");
            input = sc.nextLine();
            if (!input.isBlank()) e.setEstado(input.toUpperCase());

            boolean ok = envioService.actualizar(e);

            System.out.println(ok ? "Envio actualizado." : "No se pudo actualizar.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarEnvio() {
        try {
            System.out.print("ID del Envio a eliminar: ");
            long id = Long.parseLong(sc.nextLine());

            boolean ok = envioService.eliminar(id);
            System.out.println(ok ? "Envio eliminado (logico)." : "No se pudo eliminar.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // =======================================
    // Menú principal
    // =======================================

    private void mostrarMenu() {
        System.out.println("\n====== MENU PRINCIPAL ======");
        System.out.println("1) Crear Pedido");
        System.out.println("2) Listar Pedidos");
        System.out.println("3) Buscar Pedido por ID");
        System.out.println("4) Actualizar Pedido");
        System.out.println("5) Eliminar Pedido");

        System.out.println("6) Crear Envio");
        System.out.println("7) Listar Envios");
        System.out.println("8) Buscar Envio por ID");
        System.out.println("9) Actualizar Envio");
        System.out.println("10) Eliminar Envio");
        System.out.println("11) Crear Pedido + Envio (Transaccion)");


        System.out.println("X) Salir");
        System.out.print("Opcion: ");
    }
}
