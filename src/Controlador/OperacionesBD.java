/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Vista.CopiaSeguridad;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.ShowUsers;
import org.basex.core.cmd.XQuery;
import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.QueryProcessor;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;

/**
 *
 * @author Usuario
 */
public class OperacionesBD {
    
    private Context contexto;
    private String directorio;

    public OperacionesBD(String directorio) {
        this.directorio = directorio;
    }
    
        public String operacion(String consulta) {
   
        if (contexto == null) {
          crearBD();
      }
      try {
          String xq = new XQuery(consulta).execute(contexto);
          return xq;
      } catch (BaseXException ex) {
          System.out.println("-->ERROR AL CONSULTAR LA BASE DE DATOS:");
          System.err.println(ex);
          return "Error";
      }
  }

        
        public void escribir() throws IOException{
            ArrayList<String> aEscribir = new ArrayList<>();
            operacion("for $b in //coches return  $b/coche/data(@id)");
            for (int i = 0; i < Integer.parseInt(operacion("for $b in //coches return count($b/coche)")); i++) {
               aEscribir.add(operacion("for $b in //coches return $b/coche[@id = " + i + "]"));
            }
            int i = 0;
            for (String string : aEscribir) {
              String nombreArchivo = "coche" + i + ".xml";
              FileWriter fw = new FileWriter(new File(nombreArchivo));
              fw.write(string);
              fw.close();
              i++;
            }
        }
         
       
        
        
    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public String getDirectorio() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }
  

        
        
   public void crearBD() {
      if (contexto == null) {
          contexto = new Context();
         
          try {
              CreateDB baseDatos = new CreateDB("Colecciones", directorio);
              baseDatos.execute(contexto);
              System.out.print(new ShowUsers().execute(contexto));
          } catch (BaseXException ex) {
              System.out.println("-->LA BASE DE DATOS NO HA PODIDO CREARSE:");
              System.err.println(ex);
          }
      }

  }
    
    
}
