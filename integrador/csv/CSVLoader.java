package integrador.csv;

import integrador.model.*;
import integrador.ejercicios.EjerciciosIntegrador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;


public class CSVLoader {


    public void loadAll(String rutaPac, String rutaMed, String rutaTurn, EjerciciosIntegrador target) {
        try {
            int p = loadPacientes(rutaPac, target);
            int m = loadMedicos(rutaMed, target);
            int[] t = loadTurnos(rutaTurn, target);
            System.out.println("Cargados: Pacientes=" + p + " Medicos=" + m + " TurnosOK=" + t[0] + " Rechazados=" + t[1]);
        } catch (Exception e) {
            System.out.println("Error carga CSV: " + e.getMessage());
        }
    }

    public int loadPacientes(String ruta, EjerciciosIntegrador target) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String l; int c=0;
        while ((l=br.readLine())!=null) {
            l=l.trim(); if (l.isEmpty() || l.startsWith("#") || l.toLowerCase().startsWith("dni")) continue;
            String[] tok = l.split(",", -1);
            String dni = tok[0].trim(), nombre = tok.length>1?tok[1].trim():"";
            target.putPaciente(new Paciente(dni, nombre)); c++;
        }
        br.close(); return c;
    }

    public int loadMedicos(String ruta, EjerciciosIntegrador target) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String l; int c=0;
        while ((l=br.readLine())!=null) {
            l=l.trim(); if (l.isEmpty() || l.startsWith("#") || l.toLowerCase().startsWith("matricula")) continue;
            String[] tok = l.split(",", -1);
            String mat = tok[0].trim(), nombre = tok.length>1?tok[1].trim():"", esp = tok.length>2?tok[2].trim():"";
            target.putMedico(new Medico(mat, nombre, esp)); c++;
        }
        br.close(); return c;
    }

    public int[] loadTurnos(String ruta, EjerciciosIntegrador target) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String l; int ok=0, rej=0;
        while ((l=br.readLine())!=null) {
            l=l.trim(); if (l.isEmpty() || l.startsWith("#") || l.toLowerCase().startsWith("id")) continue;
            String[] tok = l.split(",", -1);
            try {
                String id = tok[0].trim(), dni = tok[1].trim(), mat = tok[2].trim();
                String fechaStr = tok[3].trim();
                String horaStr = tok[4].trim();
                int dur = Integer.parseInt(tok[5].trim());
                String motivo = tok.length>6?tok[6].trim():"";
                // Combinar fecha y hora
                String fechaHoraStr = fechaStr + "T" + (horaStr.length()==5?horaStr:horaStr+":00");
                LocalDateTime fh = LocalDateTime.parse(fechaHoraStr);
                Turno t = new Turno(id, dni, mat, fh, dur, motivo);
                boolean added = target.addTurnoFromCSV(t);
                if (added) ok++; else rej++;
            } catch (Exception ex) { rej++; }
        }
        br.close(); return new int[]{ok, rej};
    }
}
