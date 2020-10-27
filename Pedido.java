
package Clases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;


public class Pedido {
    
           public Pedido(){};
    
           Conexion conect = new Conexion();
           PreparedStatement ps= null;
           Connection conn=null;
           ResultSet res=null;
           
    public void agregarPedido(String ped_tipo,String ped_categoria,double ped_precio,LocalDate fecha,String ped_descripcion,String ped_comentario,String nombre, int cli_celular, String cli_direccion){
    
        try{
           
           int id=-1;
            
           conn=conect.getConection();
           
           ps= conn.prepareStatement("INSERT INTO clientes (cli_nombre ,cli_celular ,cli_direccion ) VALUES(?,?,?)");
           ps.setString(1, nombre);
           ps.setInt(2, cli_celular);
           ps.setString(3, cli_direccion);
           ps.execute();
           
           ps= conn.prepareStatement("SELECT cli_id FROM clientes WHERE cli_nombre=? ");
           ps.setString(1,nombre);
           res = ps.executeQuery();
           
           if(res.next()) id =res.getInt(1);
           
           ps= conn.prepareStatement("INSERT INTO pedidos (ped_tipo ,ped_categoria,ped_precio,ped_fechaEntrega,ped_descripcion,ped_comentario,ped_cli_id,ped_estado ) VALUES(?,?,?,?,?,?,?,?)");
           ps.setString(1, ped_tipo);
           ps.setString(2, ped_categoria);
           ps.setDouble(3, ped_precio);
           Date ped_fecha = java.sql.Date.valueOf(fecha);
           ps.setDate(4, ped_fecha);
           ps.setString(5, ped_descripcion);
           ps.setString(6, ped_comentario);
           ps.setInt(7, id);
           ps.setString(8, "PENDIENTE");
           ps.execute();

       }catch(Exception ex){ 
           
           System.out.println(ex);
       }
    }
    public void listarPedidos(javax.swing.JTable tablaPedidos){
                        try{
           DefaultTableModel modelo = new DefaultTableModel();
           tablaPedidos.setModel(modelo);
           conn=conect.getConection();
           String sql = "SELECT ped_id, ped_tipo ,ped_categoria,ped_precio,ped_fechaEntrega, cli_nombre, cli_celular, cli_direccion FROM pedidos, clientes WHERE ped_estado = 'PENDIENTE' AND cli_id = ped_cli_id";
           ps= conn.prepareStatement(sql);
           res = ps.executeQuery();
           ResultSetMetaData resMD = res.getMetaData();
           int cantidadColumnas = resMD.getColumnCount();
           
           modelo.addColumn("Número de pedido");
           modelo.addColumn("Tipo");
           modelo.addColumn("Categoría");
           modelo.addColumn("Precio");
           modelo.addColumn("Fecha de entrega");
           modelo.addColumn("Cliente");
           modelo.addColumn("Celular");
           modelo.addColumn("Direccion");
           
           while(res.next()){
              Object[] filas = new Object[cantidadColumnas];
              for(int i = 0; i<cantidadColumnas; i++){
                  filas[i]=res.getObject(i+1);
              }
              modelo.addRow(filas);
           }
       }catch(SQLException ex){ System.out.println(ex.toString());}
    }
    String c_tipo=" ",c_categoria=" ",c_precio=" ",c_fecha=" ",c_nombre=" ",c_celular=" ",c_direccion=" ",c_descripcion=" ",c_comentarios=" ";
    public void consultar(javax.swing.JComboBox<String> seleccionarTipo, javax.swing.JComboBox<String> seleccionarCategoria, javax.swing.JTextField txtPrecio ,javax.swing.JTextField txtFecha ,javax.swing.JTextField txtNombre, javax.swing.JTextField txtCelular ,javax.swing.JTextField txtDireccion ,javax.swing.JTextPane txtDescripcion ,javax.swing.JTextField txtComentarios, int pedId){
          try{
           conn=conect.getConection();  
           int abc=-1;
           ps= conn.prepareStatement("SELECT ped_cli_id FROM pedidos WHERE ped_id=? ");
           ps.setInt(1,pedId);
           res = ps.executeQuery();
           if(res.next()) abc =res.getInt(1);

           ps= conn.prepareStatement("SELECT ped_tipo ,ped_categoria,ped_precio,ped_fechaEntrega, ped_descripcion,ped_comentario, cli_nombre, cli_celular, cli_direccion FROM pedidos,clientes WHERE ped_id=? AND cli_id=? AND ped_estado = 'PENDIENTE'");
           ps.setInt(1,pedId);
           ps.setInt(2,abc);
           res = ps.executeQuery();
           
           if(res.next()) {
               c_tipo=res.getString(1);
               c_categoria=res.getString(2);
               double pre =res.getDouble(3);
               c_precio= Double.toString(pre);
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               Date date =res.getDate(4);
               c_fecha=sdf.format(date);
               c_descripcion=res.getString(5);
               c_comentarios=res.getString(6);
               c_nombre=res.getString(7);
               int cel=res.getInt(8);
               c_celular=Integer.toString(cel);
               c_direccion=res.getString(9);
           }
           
           seleccionarTipo.setSelectedItem(c_tipo);
           seleccionarCategoria.setSelectedItem(c_categoria);
           txtPrecio.setText(c_precio);
           txtFecha.setText(c_fecha);
           txtNombre.setText(c_nombre);
           txtCelular.setText(c_celular);
           txtDireccion.setText(c_direccion);
           txtDescripcion.setText(c_descripcion);
           txtComentarios.setText(c_comentarios);
           
       }catch(SQLException ex){ System.out.println(ex.toString());}
                                        
            }
    public void modificarPedido(String tipo, String categoria, double precio, LocalDate fecha, String Descripcion, String Comentario, String nombre, int celular, String direccion, int pedId){
         try{
            
           conn=conect.getConection();
           
           ps=conn.prepareStatement("update pedidos Set ped_tipo=? WHERE ped_id=? AND ped_estado='PENDIENTE'");
           ps.setString(1,tipo);
           ps.setInt(2, pedId);
           ps.execute();
           ps=conn.prepareStatement("update pedidos Set ped_categoria=? WHERE ped_id=? AND ped_estado='PENDIENTE'");
           ps.setString(1,categoria);
           ps.setInt(2, pedId);
           ps.execute();
           ps=conn.prepareStatement("update pedidos Set ped_precio=? WHERE ped_id=? AND ped_estado='PENDIENTE'");
           ps.setDouble(1,precio);
           ps.setInt(2, pedId);
           ps.execute();
           ps=conn.prepareStatement("update pedidos Set ped_fechaEntrega=? WHERE ped_id=? AND ped_estado='PENDIENTE'");
           Date ped_fecha = java.sql.Date.valueOf(fecha);
           ps.setDate(1,ped_fecha);
           ps.setInt(2, pedId);
           ps.execute();
           ps=conn.prepareStatement("update pedidos Set ped_descripcion=? WHERE ped_id=? AND ped_estado='PENDIENTE'");
           ps.setString(1,Descripcion);
           ps.setInt(2, pedId);
           ps.execute();
           ps=conn.prepareStatement("update pedidos Set ped_comentario=? WHERE ped_id=? AND ped_estado='PENDIENTE'");
           ps.setString(1,Comentario);
           ps.setInt(2, pedId);
           ps.execute();
           
           ps= conn.prepareStatement("SELECT ped_cli_id FROM pedidos WHERE ped_id=? AND ped_estado='PENDIENTE'");
           ps.setInt(1,pedId);
           res = ps.executeQuery();
           int idc=-1;
           if(res.next()) idc =res.getInt(1);
           
           ps=conn.prepareStatement("update clientes Set cli_nombre=? WHERE cli_id=?");
           ps.setString(1,nombre);
           ps.setInt(2, idc);
           ps.execute();
           ps=conn.prepareStatement("update clientes Set cli_celular=? WHERE cli_id=?");
           ps.setInt(1,celular);
           ps.setInt(2, idc);
           ps.execute();
           ps=conn.prepareStatement("update clientes Set cli_direccion=? WHERE cli_id=?");
           ps.setString(1,direccion);
           ps.setInt(2, idc);
           ps.execute();

       }catch(Exception ex){ 
           
           System.out.println(ex);
       }
    }
    
    public void finalizarPed(javax.swing.JTextField txtPrecio ,javax.swing.JTextField txtFecha ,javax.swing.JTextField txtNombre,javax.swing.JTextPane txtDescripcion, int pedId){
    try{
           conn=conect.getConection();  
           int abc=-1;
           ps= conn.prepareStatement("SELECT ped_cli_id FROM pedidos WHERE ped_id=? AND ped_estado='PENDIENTE'");
           ps.setInt(1,pedId);
           res = ps.executeQuery();
           if(res.next()) abc =res.getInt(1);

           ps= conn.prepareStatement("SELECT ped_precio,ped_fechaEntrega, ped_descripcion, cli_nombre FROM pedidos,clientes WHERE ped_id=? AND cli_id=? AND ped_estado = 'PENDIENTE'");
           ps.setInt(1,pedId);
           ps.setInt(2,abc);
           res = ps.executeQuery();
           
           if(res.next()) {
               
               double pre =res.getDouble(1);
               c_precio= Double.toString(pre);
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               Date date =res.getDate(2);
               c_fecha=sdf.format(date);
               c_descripcion=res.getString(3);
               c_nombre=res.getString(4);
           }
           
           txtPrecio.setText(c_precio);
           txtFecha.setText(c_fecha);
           txtNombre.setText(c_nombre);
           txtDescripcion.setText(c_descripcion);
           
       }catch(SQLException ex){ System.out.println(ex.toString());}
    }
   
    public void confirmarFinalizar(int pedId){
        try{
            
           conn=conect.getConection();
           
           ps=conn.prepareStatement("update pedidos Set ped_estado=? WHERE ped_id=? AND ped_estado='PENDIENTE'");
           ps.setString(1,"COMPLETADO");
           ps.setInt(2, pedId);
           ps.execute();
           
       }catch(Exception ex){ 
           
           System.out.println(ex);
       }
    }
        public void listarPedidosCompletados(javax.swing.JTable tablaPedidos){
                        try{
           DefaultTableModel modelo = new DefaultTableModel();
           tablaPedidos.setModel(modelo);
           conn=conect.getConection();
           String sql = "SELECT ped_id, ped_tipo ,ped_categoria,ped_precio,ped_fechaEntrega, cli_nombre, cli_celular, cli_direccion FROM pedidos, clientes WHERE ped_estado = 'COMPLETADO' AND cli_id = ped_cli_id";
           ps= conn.prepareStatement(sql);
           res = ps.executeQuery();
           ResultSetMetaData resMD = res.getMetaData();
           int cantidadColumnas = resMD.getColumnCount();
           
           modelo.addColumn("Número de pedido");
           modelo.addColumn("Tipo");
           modelo.addColumn("Categoría");
           modelo.addColumn("Precio");
           modelo.addColumn("Fecha de entrega");
           modelo.addColumn("Cliente");
           modelo.addColumn("Celular");
           modelo.addColumn("Direccion");
           
           while(res.next()){
              Object[] filas = new Object[cantidadColumnas];
              for(int i = 0; i<cantidadColumnas; i++){
                  filas[i]=res.getObject(i+1);
              }
              modelo.addRow(filas);
           }
       }catch(SQLException ex){ System.out.println(ex.toString());}
    }
        public void consultar2(javax.swing.JComboBox<String> seleccionarTipo, javax.swing.JComboBox<String> seleccionarCategoria, javax.swing.JTextField txtPrecio ,javax.swing.JTextField txtFecha ,javax.swing.JTextField txtNombre, javax.swing.JTextField txtCelular ,javax.swing.JTextField txtDireccion ,javax.swing.JTextPane txtDescripcion ,javax.swing.JTextField txtComentarios, int pedId){
          try{
           conn=conect.getConection();  
           int abc=-1;
           ps= conn.prepareStatement("SELECT ped_cli_id FROM pedidos WHERE ped_id=? AND ped_estado='COMPLETADO' ");
           ps.setInt(1,pedId);
           res = ps.executeQuery();
           if(res.next()) abc =res.getInt(1);

           ps= conn.prepareStatement("SELECT ped_tipo ,ped_categoria,ped_precio,ped_fechaEntrega, ped_descripcion,ped_comentario, cli_nombre, cli_celular, cli_direccion FROM pedidos,clientes WHERE ped_id=? AND cli_id=? AND ped_estado = 'COMPLETADO'");
           ps.setInt(1,pedId);
           ps.setInt(2,abc);
           res = ps.executeQuery();
           
           if(res.next()) {
               c_tipo=res.getString(1);
               c_categoria=res.getString(2);
               double pre =res.getDouble(3);
               c_precio= Double.toString(pre);
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               Date date =res.getDate(4);
               c_fecha=sdf.format(date);
               c_descripcion=res.getString(5);
               c_comentarios=res.getString(6);
               c_nombre=res.getString(7);
               int cel=res.getInt(8);
               c_celular=Integer.toString(cel);
               c_direccion=res.getString(9);
           }
           
           seleccionarTipo.setSelectedItem(c_tipo);
           seleccionarCategoria.setSelectedItem(c_categoria);
           txtPrecio.setText(c_precio);
           txtFecha.setText(c_fecha);
           txtNombre.setText(c_nombre);
           txtCelular.setText(c_celular);
           txtDireccion.setText(c_direccion);
           txtDescripcion.setText(c_descripcion);
           txtComentarios.setText(c_comentarios);
           
       }catch(SQLException ex){ System.out.println(ex.toString());}
                                        
            }
        public void modificarPedido2(String tipo, String categoria, double precio, LocalDate fecha, String Descripcion, String Comentario, String nombre, int celular, String direccion, int pedId){
         try{
            
           conn=conect.getConection();
           
           ps=conn.prepareStatement("update pedidos Set ped_tipo=? WHERE ped_id=? AND ped_estado='COMPLETADO'");
           ps.setString(1,tipo);
           ps.setInt(2, pedId);
           ps.execute();
           ps=conn.prepareStatement("update pedidos Set ped_categoria=? WHERE ped_id=? AND ped_estado='COMPLETADO'");
           ps.setString(1,categoria);
           ps.setInt(2, pedId);
           ps.execute();
           ps=conn.prepareStatement("update pedidos Set ped_precio=? WHERE ped_id=? AND ped_estado='COMPLETADO'");
           ps.setDouble(1,precio);
           ps.setInt(2, pedId);
           ps.execute();
           ps=conn.prepareStatement("update pedidos Set ped_fechaEntrega=? WHERE ped_id=? AND ped_estado='COMPLETADO'");
           Date ped_fecha = java.sql.Date.valueOf(fecha);
           ps.setDate(1,ped_fecha);
           ps.setInt(2, pedId);
           ps.execute();
           ps=conn.prepareStatement("update pedidos Set ped_descripcion=? WHERE ped_id=? AND ped_estado='COMPLETADO'");
           ps.setString(1,Descripcion);
           ps.setInt(2, pedId);
           ps.execute();
           ps=conn.prepareStatement("update pedidos Set ped_comentario=? WHERE ped_id=? AND ped_estado='COMPLETADO'");
           ps.setString(1,Comentario);
           ps.setInt(2, pedId);
           ps.execute();
           
           ps= conn.prepareStatement("SELECT ped_cli_id FROM pedidos WHERE ped_id=? AND ped_estado='COMPLETADO'");
           ps.setInt(1,pedId);
           res = ps.executeQuery();
           int idc=-1;
           if(res.next()) idc =res.getInt(1);
           
           ps=conn.prepareStatement("update clientes Set cli_nombre=? WHERE cli_id=?");
           ps.setString(1,nombre);
           ps.setInt(2, idc);
           ps.execute();
           ps=conn.prepareStatement("update clientes Set cli_celular=? WHERE cli_id=?");
           ps.setInt(1,celular);
           ps.setInt(2, idc);
           ps.execute();
           ps=conn.prepareStatement("update clientes Set cli_direccion=? WHERE cli_id=?");
           ps.setString(1,direccion);
           ps.setInt(2, idc);
           ps.execute();

       }catch(Exception ex){ 
           
           System.out.println(ex);
       }
    }
        public void eliminarPedido(int id, String estatus){
            try{
            
           conn=conect.getConection();
           
           ps=conn.prepareStatement("DELETE FROM pedidos WHERE ped_id=? AND ped_estado=?");
           ps.setInt(1, id);
           ps.setString(2,estatus);
           ps.execute();

       }catch(Exception ex){ 
           
           System.out.println(ex);
       }
        }
}
