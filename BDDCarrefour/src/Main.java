import java.sql.Date;
import java.util.Scanner;
import java.util.List;
import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.VentaDAO;
import models.Cliente;
import models.Producto;
import models.Venta;
import models.DetalleVenta;
import utils.MessageManager;
import java.sql.*;
import java.time.LocalDate;
import config.DatabaseConnection;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static ProductoDAO productoDAO = new ProductoDAO();
    private static VentaDAO ventaDAO = new VentaDAO();

    public static void main(String[] args) {
        try {
            // Verificar si la base de datos está vacía e inicializar datos de ejemplo
            if (isDatabaseEmpty()) {
                inicializarDatosEjemplo();
            }

            boolean continuar = true;
            while (continuar) {
                limpiarPantalla();
                mostrarMenuPrincipal();
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (opcion) {
                    case 1:
                        gestionarClientes();
                        break;
                    case 2:
                        gestionarProductos();
                        break;
                    case 3:
                        gestionarVentas();
                        break;
                    case 4:
                        gestionarReportes();
                        break;
                    case 5:
                        limpiarPantalla();
                        System.out.println("¡Gracias por usar el sistema!");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente nuevamente.");
                        pausar();
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void limpiarPantalla() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private static void pausar() {
        System.out.println("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE VENTAS CARREFOUR ===");
        System.out.println("╔════════════════════════════════╗");
        System.out.println("║ 1. Gestión de Clientes         ║");
        System.out.println("║ 2. Gestión de Productos        ║");
        System.out.println("║ 3. Gestión de Ventas          ║");
        System.out.println("║ 4. Reportes                    ║");
        System.out.println("║ 5. Salir                       ║");
        System.out.println("╚════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }

    private static void gestionarClientes() {
        System.out.println("\n=== GESTIÓN DE CLIENTES ===");
        System.out.println("1. Registrar nuevo cliente");
        System.out.println("2. Listar todos los clientes");
        System.out.println("3. Buscar cliente por ID");
        System.out.println("4. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
        int subopcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        switch (subopcion) {
            case 1:
                registrarCliente();
                pausar();
                break;
            case 2:
                listarClientes();
                pausar();
                break;
            case 3:
                buscarClientePorId();
                pausar();
                break;
            case 4:
                break;
            default:
                System.out.println("Opción no válida. Por favor, intente nuevamente.");
                pausar();
        }
    }

    private static void gestionarProductos() {
        System.out.println("\n=== GESTIÓN DE PRODUCTOS ===");
        System.out.println("1. Registrar nuevo producto");
        System.out.println("2. Listar todos los productos");
        System.out.println("3. Buscar producto por ID");
        System.out.println("4. Actualizar stock");
        System.out.println("5. Eliminar producto");
        System.out.println("6. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        switch (opcion) {
            case 1:
                registrarProducto();
                break;
            case 2:
                listarProductos();
                break;
            case 3:
                buscarProductoPorId();
                break;
            case 4:
                actualizarStock();
                break;
            case 5:
                eliminarProducto();
                break;
            case 6:
                return;
            default:
                System.out.println("Opción no válida");
        }
        pausar();
    }

    private static void gestionarVentas() {
        System.out.println("\n=== GESTIÓN DE VENTAS ===");
        System.out.println("1. Realizar nueva venta");
        System.out.println("2. Ver todas las ventas");
        System.out.println("3. Buscar ventas por cliente");
        System.out.println("4. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
        int subopcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        switch (subopcion) {
            case 1:
                realizarVenta();
                pausar();
                break;
            case 2:
                consultarVentas();
                pausar();
                break;
            case 3:
                buscarVentasPorCliente();
                pausar();
                break;
            case 4:
                break;
            default:
                System.out.println("Opción no válida. Por favor, intente nuevamente.");
                pausar();
        }
    }

    private static void gestionarReportes() {
        System.out.println("\n=== REPORTES ===");
        System.out.println("1. Ventas del día");
        System.out.println("2. Productos más vendidos");
        System.out.println("3. Clientes frecuentes");
        System.out.println("4. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
        int subopcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        switch (subopcion) {
            case 1:
                ventasDelDia();
                pausar();
                break;
            case 2:
                productosMasVendidos();
                pausar();
                break;
            case 3:
                clientesFrecuentes();
                pausar();
                break;
            case 4:
                break;
            default:
                System.out.println("Opción no válida. Por favor, intente nuevamente.");
                pausar();
        }
    }

    private static void registrarCliente() {
        System.out.println("\n=== REGISTRO DE CLIENTE ===");
        System.out.print("Ingrese el nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el email del cliente: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese el teléfono del cliente: ");
        String telefono = scanner.nextLine();

        Cliente cliente = new Cliente(nombre, email, telefono);
        if (clienteDAO.crearCliente(cliente)) {
            System.out.println(String.format(MessageManager.CLIENTE_CREADO, cliente.getNombre()));
        } else {
            System.err.println(String.format(MessageManager.ERROR_CREAR_CLIENTE, "Error desconocido"));
        }
    }

    private static void listarClientes() {
        System.out.println("\n=== LISTA DE CLIENTES ===");
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        for (Cliente cliente : clientes) {
            System.out.println(String.format("ID: %d | Nombre: %s | Email: %s | Teléfono: %s",
                    cliente.getClienteId(),
                    cliente.getNombre(),
                    cliente.getEmail(),
                    cliente.getTelefono()));
        }
    }

    private static void buscarClientePorId() {
        System.out.println("\n=== BUSCAR CLIENTE ===");
        System.out.print("Ingrese el ID del cliente: ");
        int clienteId = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        Cliente cliente = clienteDAO.obtenerClientePorId(clienteId);
        if (cliente != null) {
            System.out.println(String.format("ID: %d | Nombre: %s | Email: %s | Teléfono: %s",
                    cliente.getClienteId(),
                    cliente.getNombre(),
                    cliente.getEmail(),
                    cliente.getTelefono()));
        } else {
            System.err.println(String.format(MessageManager.CLIENTE_NO_ENCONTRADO, clienteId));
        }
    }

    private static void registrarProducto() {
        System.out.println("\n=== REGISTRO DE PRODUCTO ===");
        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese la descripción del producto: ");
        String descripcion = scanner.nextLine();
        System.out.print("Ingrese el precio del producto: ");
        double precio = scanner.nextDouble();
        System.out.print("Ingrese el stock inicial: ");
        int stock = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        Producto producto = new Producto(nombre, descripcion, precio, stock);
        if (productoDAO.crearProducto(producto)) {
            System.out.println(String.format(MessageManager.PRODUCTO_CREADO, producto.getNombre(), producto.getStock()));
        } else {
            System.err.println(String.format(MessageManager.ERROR_CREAR_PRODUCTO, producto.getNombre()));
        }
    }

    private static void listarProductos() {
        System.out.println("\n=== LISTA DE PRODUCTOS ===");
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto producto : productos) {
            System.out.println(String.format("ID: %d | %s | Precio: $%.2f | Stock: %d",
                    producto.getProductoId(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getStock()));
        }
    }

    private static void buscarProductoPorId() {
        System.out.println("\n=== BUSCAR PRODUCTO ===");
        System.out.print("Ingrese el ID del producto: ");
        int productoId = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        Producto producto = productoDAO.obtenerProductoPorId(productoId);
        if (producto != null) {
            System.out.println(String.format("ID: %d | %s | Precio: $%.2f | Stock: %d",
                    producto.getProductoId(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getStock()));
        } else {
            System.err.println(String.format(MessageManager.PRODUCTO_NO_ENCONTRADO, productoId));
        }
    }

    private static void actualizarStock() {
        System.out.println("\n=== ACTUALIZAR STOCK ===");
        System.out.print("Ingrese el ID del producto: ");
        int productoId = scanner.nextInt();

        Producto producto = productoDAO.obtenerProductoPorId(productoId);
        if (producto == null) {
            System.err.println(String.format(MessageManager.PRODUCTO_NO_ENCONTRADO, productoId));
            return;
        }

        System.out.println(String.format("Producto actual: %s | Stock actual: %d",
                producto.getNombre(), producto.getStock()));

        System.out.print("Ingrese el nuevo stock: ");
        int nuevoStock = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        if (nuevoStock < 0) {
            System.out.println("❌ El stock no puede ser negativo.");
            return;
        }

        producto.setStock(nuevoStock);
        if (productoDAO.actualizarProducto(producto)) {
            System.out.println(String.format(MessageManager.STOCK_ACTUALIZADO, producto.getNombre()));
        } else {
            System.err.println("Error al actualizar el stock.");
        }
    }

    private static void eliminarProducto() {
        System.out.println("\n=== ELIMINAR PRODUCTO ===");

        // Primero mostrar los productos disponibles
        listarProductos();

        System.out.print("\nIngrese el ID del producto a eliminar: ");
        int productoId = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        Producto producto = productoDAO.obtenerProductoPorId(productoId);
        if (producto == null) {
            System.err.println(String.format(MessageManager.PRODUCTO_NO_ENCONTRADO, productoId));
            return;
        }

        System.out.println(String.format("¿Está seguro que desea eliminar el producto '%s'? (S/N): ",
                producto.getNombre()));
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {
            if (productoDAO.eliminarProducto(productoId)) {
                System.out.println(String.format("✅ Producto '%s' eliminado correctamente.",
                        producto.getNombre()));
            } else {
                System.err.println("❌ No se pudo eliminar el producto. Puede tener ventas asociadas.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private static void realizarVenta() {
        limpiarPantalla();
        System.out.println("\n=== REALIZAR VENTA ===");

        // Primero mostrar los clientes disponibles
        System.out.println("\nClientes disponibles:");
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados. Por favor, registre un cliente primero.");
            return;
        }

        for (Cliente cliente : clientes) {
            System.out.println(String.format("ID: %d | Nombre: %s | Teléfono: %s",
                    cliente.getClienteId(),
                    cliente.getNombre(),
                    cliente.getTelefono()));
        }

        System.out.print("\nIngrese el ID del cliente: ");
        int clienteId = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        // Verificar que el cliente existe
        Cliente clienteSeleccionado = clienteDAO.obtenerClientePorId(clienteId);
        if (clienteSeleccionado == null) {
            System.err.println(String.format(MessageManager.CLIENTE_NO_ENCONTRADO, clienteId));
            return;
        }

        limpiarPantalla();
        System.out.println("Cliente seleccionado: " + clienteSeleccionado.getNombre());

        // También mostrar los productos disponibles
        System.out.println("\nProductos disponibles:");
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados. Por favor, registre productos primero.");
            return;
        }

        for (Producto producto : productos) {
            System.out.println(String.format("ID: %d | %s | Precio: $%.2f | Stock: %d",
                    producto.getProductoId(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getStock()));
        }

        // Continuar con la venta
        Venta venta = new Venta(clienteId, new Date(System.currentTimeMillis()), 0);
        double totalVenta = 0;

        boolean agregarMasProductos = true;
        while (agregarMasProductos) {
            int productoId = 0;
            boolean idValido = false;
            while (!idValido) {
                try {
                    System.out.print("\nIngrese el ID del producto: ");
                    productoId = Integer.parseInt(scanner.nextLine().trim());
                    idValido = true;
                } catch (NumberFormatException e) {
                    System.out.println("❌ Por favor, ingrese un número válido.");
                }
            }

            int cantidad = 0;
            boolean cantidadValida = false;
            while (!cantidadValida) {
                try {
                    System.out.print("Ingrese la cantidad: ");
                    cantidad = Integer.parseInt(scanner.nextLine().trim());
                    if (cantidad <= 0) {
                        System.out.println("❌ La cantidad debe ser mayor a 0.");
                    } else {
                        cantidadValida = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Por favor, ingrese un número válido.");
                }
            }

            Producto producto = productoDAO.obtenerProductoPorId(productoId);
            if (producto != null) {
                if (producto.getStock() >= cantidad) {
                    double subtotal = producto.getPrecio() * cantidad;
                    DetalleVenta detalle = new DetalleVenta(0, productoId, cantidad, subtotal);
                    venta.addDetalle(detalle);
                    totalVenta += subtotal;

                    System.out.println(String.format(MessageManager.DETALLE_VENTA,
                            producto.getNombre(),
                            cantidad,
                            subtotal));
                } else {
                    System.err.println(String.format(MessageManager.ERROR_STOCK_INSUFICIENTE,
                            producto.getNombre(),
                            producto.getStock()));
                }
            } else {
                System.err.println(String.format(MessageManager.PRODUCTO_NO_ENCONTRADO, productoId));
                continue;
            }

            System.out.print("¿Desea agregar más productos? (S/N): ");
            agregarMasProductos = scanner.nextLine().equalsIgnoreCase("S");
        }

        venta.setTotal(totalVenta);
        int ventaId = ventaDAO.crearVenta(venta);
        if (ventaId > 0) {
            System.out.println(String.format(MessageManager.VENTA_CREADA, ventaId));
            System.out.println(String.format(MessageManager.TOTAL_VENTA, totalVenta));
        } else {
            System.err.println(String.format(MessageManager.ERROR_CREAR_VENTA, "Error al procesar la venta"));
        }
    }

    private static void consultarVentas() {
        limpiarPantalla();
        System.out.println("\n=== CONSULTA DE VENTAS ===");
        System.out.print("Ingrese el ID del cliente (0 para todas las ventas): ");
        int clienteId = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        List<Venta> ventas;
        if (clienteId > 0) {
            ventas = ventaDAO.obtenerVentasPorCliente(clienteId);
        } else {
            ventas = ventaDAO.obtenerTodasLasVentas();
        }

        if (ventas.isEmpty()) {
            System.out.println("No se encontraron ventas.");
            return;
        }

        for (Venta venta : ventas) {
            System.out.println("\n=== Venta ID: " + venta.getVentaId() + " ===");
            System.out.println("Fecha: " + venta.getFechaVenta());
            System.out.println("Cliente ID: " + venta.getClienteId());
            System.out.println("Total: $" + venta.getTotal());

            System.out.println("Productos:");
            for (DetalleVenta detalle : venta.getDetalles()) {
                Producto producto = productoDAO.obtenerProductoPorId(detalle.getProductoId());
                if (producto != null) {
                    System.out.println(String.format(MessageManager.DETALLE_VENTA,
                            producto.getNombre(),
                            detalle.getCantidad(),
                            detalle.getSubtotal()));
                }
            }
        }
    }

    private static void buscarVentasPorCliente() {
        System.out.println("\n=== BUSCAR VENTAS POR CLIENTE ===");
        System.out.print("Ingrese el ID del cliente: ");
        int clienteId = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        Cliente cliente = clienteDAO.obtenerClientePorId(clienteId);
        if (cliente == null) {
            System.err.println(String.format(MessageManager.CLIENTE_NO_ENCONTRADO, clienteId));
            return;
        }

        List<Venta> ventas = ventaDAO.obtenerVentasPorCliente(clienteId);
        if (ventas.isEmpty()) {
            System.out.println("No se encontraron ventas para este cliente.");
            return;
        }

        System.out.println(String.format("Ventas del cliente: %s", cliente.getNombre()));
        for (Venta venta : ventas) {
            mostrarDetalleVenta(venta);
        }
    }

    private static void ventasDelDia() {
        System.out.println("\n=== VENTAS DEL DÍA ===");
        List<Venta> ventas = ventaDAO.obtenerVentasDelDia();
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas hoy.");
            return;
        }

        double totalDia = 0;
        for (Venta venta : ventas) {
            mostrarDetalleVenta(venta);
            totalDia += venta.getTotal();
        }
        System.out.println(String.format("\nTotal del día: $%.2f", totalDia));
    }

    private static void productosMasVendidos() {
        System.out.println("\n=== PRODUCTOS MÁS VENDIDOS ===");
        List<Object[]> productos = ventaDAO.obtenerProductosMasVendidos();
        if (productos.isEmpty()) {
            System.out.println("No hay datos de ventas disponibles.");
            return;
        }

        System.out.println("Top productos más vendidos:");
        for (Object[] producto : productos) {
            System.out.println(String.format("%s | Cantidad vendida: %d | Total: $%.2f",
                    producto[0], // nombre producto
                    producto[1], // cantidad
                    producto[2])); // total
        }
    }

    private static void clientesFrecuentes() {
        System.out.println("\n=== CLIENTES FRECUENTES ===");
        List<Object[]> clientes = ventaDAO.obtenerClientesFrecuentes();
        if (clientes.isEmpty()) {
            System.out.println("No hay datos de clientes frecuentes disponibles.");
            return;
        }

        System.out.println("Top clientes por compras:");
        for (Object[] cliente : clientes) {
            System.out.println(String.format("%s | Compras: %d | Total gastado: $%.2f",
                    cliente[0], // nombre cliente
                    cliente[1], // cantidad de compras
                    cliente[2])); // total gastado
        }
    }

    private static void mostrarDetalleVenta(Venta venta) {
        System.out.println("\n=== Venta ID: " + venta.getVentaId() + " ===");
        System.out.println("Fecha: " + venta.getFechaVenta());
        Cliente cliente = clienteDAO.obtenerClientePorId(venta.getClienteId());
        System.out.println("Cliente: " + (cliente != null ? cliente.getNombre() : "Cliente no encontrado"));
        System.out.println("Total: $" + venta.getTotal());

        System.out.println("Productos:");
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = productoDAO.obtenerProductoPorId(detalle.getProductoId());
            if (producto != null) {
                System.out.println(String.format(MessageManager.DETALLE_VENTA,
                        producto.getNombre(),
                        detalle.getCantidad(),
                        detalle.getSubtotal()));
            }
        }
    }

    private static boolean isDatabaseEmpty() {
        String sql = "SELECT COUNT(*) FROM productos";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar la base de datos: " + e.getMessage());
        }
        return true;
    }

    private static void inicializarDatosEjemplo() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Limpiar tablas existentes
            String[] cleanQueries = {
                    "DELETE FROM detalles_ventas",
                    "DELETE FROM ventas",
                    "DELETE FROM productos",
                    "DELETE FROM clientes",
                    "ALTER TABLE detalles_ventas AUTO_INCREMENT = 1",
                    "ALTER TABLE ventas AUTO_INCREMENT = 1",
                    "ALTER TABLE productos AUTO_INCREMENT = 1",
                    "ALTER TABLE clientes AUTO_INCREMENT = 1"
            };

            for (String query : cleanQueries) {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.executeUpdate();
                }
            }

            // Insertar productos
            String insertProductos = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertProductos)) {
                Object[][] productos = {
                        {"Aceite Cocinero 900ml", 1250.00, 100},
                        {"Leche La Serenísima 1L", 890.00, 150},
                        {"Yerba Mate Playadito 500g", 1450.00, 80},
                        {"Fideos Matarazzo 500g", 650.00, 120},
                        {"Arroz Gallo Oro 1kg", 980.00, 90},
                        {"Coca Cola 2.25L", 1100.00, 200},
                        {"Pan Bimbo Artesano", 950.00, 60},
                        {"Papel Higiénico Elite 4u", 1580.00, 100},
                        {"Detergente Ala 750ml", 890.00, 75},
                        {"Galletitas Oreo 118g", 580.00, 150},
                        {"Atún La Campagnola 170g", 980.00, 80},
                        {"Shampoo Pantene 400ml", 2100.00, 40},
                        {"Jabón Dove 90g", 450.00, 120},
                        {"Cerveza Quilmes 1L", 850.00, 200},
                        {"Café La Virginia 250g", 1250.00, 60}
                };

                for (Object[] producto : productos) {
                    stmt.setString(1, (String) producto[0]);
                    stmt.setDouble(2, (Double) producto[1]);
                    stmt.setInt(3, (Integer) producto[2]);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            // Insertar clientes
            String insertClientes = "INSERT INTO clientes (nombre, email, telefono) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertClientes)) {
                Object[][] clientes = {
                        {"María González", "maria.g@email.com", "1145678901"},
                        {"Juan Pérez", "juanp@email.com", "1156789012"},
                        {"Ana Rodríguez", "ana.r@email.com", "1167890123"},
                        {"Carlos López", "carlos.l@email.com", "1178901234"},
                        {"Laura Martínez", "laura.m@email.com", "1189012345"}
                };

                for (Object[] cliente : clientes) {
                    stmt.setString(1, (String) cliente[0]);
                    stmt.setString(2, (String) cliente[1]);
                    stmt.setString(3, (String) cliente[2]);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            // Insertar ventas
            String insertVentas = "INSERT INTO ventas (cliente_id, fecha_venta, total) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertVentas)) {
                Object[][] ventas = {
                        {1, Date.valueOf(LocalDate.now()), 5680.00},
                        {2, Date.valueOf(LocalDate.now()), 3450.00},
                        {3, Date.valueOf(LocalDate.now().minusDays(1)), 7890.00},
                        {4, Date.valueOf(LocalDate.now().minusDays(2)), 4560.00},
                        {5, Date.valueOf(LocalDate.now().minusDays(2)), 6780.00},
                        {1, Date.valueOf(LocalDate.now().minusDays(3)), 3240.00},
                        {2, Date.valueOf(LocalDate.now().minusDays(4)), 5670.00},
                        {3, Date.valueOf(LocalDate.now().minusDays(5)), 4320.00},
                        {4, Date.valueOf(LocalDate.now().minusDays(6)), 8900.00},
                        {5, Date.valueOf(LocalDate.now().minusDays(7)), 6540.00}
                };

                for (Object[] venta : ventas) {
                    stmt.setInt(1, (Integer) venta[0]);
                    stmt.setDate(2, (Date) venta[1]);
                    stmt.setDouble(3, (Double) venta[2]);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            // Insertar detalles de ventas
            String insertDetalles = "INSERT INTO detalles_ventas (venta_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertDetalles)) {
                Object[][] detalles = {
                        // Venta 1
                        {1, 1, 2, 2500.00},  // 2 Aceites
                        {1, 3, 1, 1450.00},  // 1 Yerba
                        {1, 6, 1, 1100.00},  // 1 Coca Cola
                        {1, 10, 1, 580.00},  // 1 Oreo
                        // Venta 2
                        {2, 2, 2, 1780.00},  // 2 Leches
                        {2, 4, 1, 650.00},   // 1 Fideos
                        {2, 5, 1, 980.00},   // 1 Arroz
                        // Venta 3
                        {3, 8, 2, 3160.00},  // 2 Papel Higiénico
                        {3, 12, 2, 4200.00}, // 2 Shampoo
                        {3, 13, 1, 450.00},  // 1 Jabón
                        // Venta 4
                        {4, 14, 3, 2550.00}, // 3 Cervezas
                        {4, 7, 1, 950.00},   // 1 Pan
                        {4, 10, 2, 1160.00}  // 2 Oreos
                };

                for (Object[] detalle : detalles) {
                    stmt.setInt(1, (Integer) detalle[0]);
                    stmt.setInt(2, (Integer) detalle[1]);
                    stmt.setInt(3, (Integer) detalle[2]);
                    stmt.setDouble(4, (Double) detalle[3]);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            System.out.println("✅ Datos de ejemplo insertados correctamente");
        } catch (SQLException e) {
            System.err.println("Error al insertar datos de ejemplo: " + e.getMessage());
        }
    }
}

