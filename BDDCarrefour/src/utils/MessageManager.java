package utils;

public class MessageManager {
    // Mensajes de éxito
    public static final String CLIENTE_CREADO = "✅ Cliente creado exitosamente: %s";
    public static final String PRODUCTO_CREADO = "✅ Producto creado exitosamente: %s - Stock: %d unidades";
    public static final String VENTA_CREADA = "✅ Venta registrada exitosamente con ID: %d";
    public static final String STOCK_ACTUALIZADO = "✅ Stock actualizado correctamente para: %s";
    public static final String PRODUCTO_ELIMINADO = "✅ Producto '%s' eliminado correctamente.";

    // Mensajes de error
    public static final String ERROR_CONEXION = "❌ Error de conexión a la base de datos: %s";
    public static final String ERROR_STOCK_INSUFICIENTE = "❌ Stock insuficiente para el producto: %s. Stock disponible: %d";
    public static final String ERROR_CREAR_CLIENTE = "❌ Error al crear el cliente: %s";
    public static final String ERROR_CREAR_PRODUCTO = "❌ Error al crear el producto: %s";
    public static final String ERROR_CREAR_VENTA = "❌ Error al crear la venta: %s";
    public static final String PRODUCTO_NO_ENCONTRADO = "❌ Producto no encontrado con ID: %d";
    public static final String CLIENTE_NO_ENCONTRADO = "❌ Cliente no encontrado con ID: %d";
    public static final String ERROR_ELIMINAR_PRODUCTO = "❌ Error al eliminar el producto: %s";

    // Mensajes informativos
    public static final String INICIO_OPERACION = "ℹ️ Iniciando operación: %s";
    public static final String FIN_OPERACION = "ℹ️ Operación finalizada: %s";
    public static final String DETALLE_VENTA = "📋 %s | Cantidad: %d | Subtotal: $%.2f";
    public static final String TOTAL_VENTA = "💰 Total de la venta: $%.2f";
}