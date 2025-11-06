package integrador.ejercicios;

import integrador.model.*;
import integrador.hash.MapaPacientes;
import integrador.queue.SalaEspera;
import integrador.planner.*;
import integrador.agenda.AVLAgenda;
import integrador.audit.AgendaConHistorial;

import java.time.LocalDateTime;

/*
 Clase que orquesta demos/ejercicios. No usa colecciones JDK para los repositorios
 principales; usa MapaPacientes propio y arreglos simples.
*/

public class EjerciciosIntegrador {

    // repositorios
    private MapaPacientes mapaPac = new MapaPacientes(11);
    // Para medicos y agendas usaremos arreglos simples
    private Medico[] medArr = new Medico[32];
    private AVLAgenda[] agendas = new AVLAgenda[32];
    private int medCount = 0;
    private Turno[] turnosById = new Turno[1024];
    private int turnoCount = 0;

    // métodos para CSVLoader
    public void putPaciente(Paciente p) { mapaPac.put(p.dni, p); }
    public void putMedico(Medico m) {
        if (medCount >= medArr.length) {
            Medico[] n = new Medico[medArr.length*2];
            System.arraycopy(medArr,0,n,0,medArr.length);
            medArr = n;
            AVLAgenda[] na = new AVLAgenda[agendas.length*2];
            System.arraycopy(agendas,0,na,0,agendas.length);
            agendas = na;
        }
        medArr[medCount] = m;
        agendas[medCount] = new AVLAgenda();
        medCount++;
    }

    private int findMedIndex(String matricula) {
        for (int i=0;i<medCount;i++) if (medArr[i].matricula.equals(matricula)) return i;
        return -1;
    }

    private boolean turnoIdExists(String id) {
        for (int i=0;i<turnoCount;i++) if (turnosById[i].id.equals(id)) return true;
        return false;
    }

    public boolean addTurnoFromCSV(Turno t) {
        if (turnoIdExists(t.id)) return false;
        if (!mapaPac.containsKey(t.dniPaciente)) return false;
        int mi = findMedIndex(t.matriculaMedico); if (mi<0) return false;
        if (t.duracionMin <= 0) return false;
        if (t.fechaHora.isBefore(LocalDateTime.now())) return false;
        AVLAgenda ag = agendas[mi];
        boolean ok = ag.agendar(t);
        if (!ok) return false;
        if (turnoCount >= turnosById.length) {
            Turno[] n = new Turno[turnosById.length*2];
            System.arraycopy(turnosById,0,n,0,turnosById.length);
            turnosById = n;
        }
        turnosById[turnoCount++] = t;
        return true;
    }

    // Ejercicios demos
    public void ej1_validaciones() {
        System.out.print("\033[H\033[2J");
        System.out.println("Pacientes: " + mapaPac.size());
        System.out.println("Medicos: " + medCount);
        System.out.println("Turnos cargados: " + turnoCount);
    }

    public void ej2_agenda_demo() {
        System.out.print("\033[H\033[2J");
        // Imprimir agenda de todos los médicos
        for (int idx = 0; idx < medCount; idx++) {
            Medico m = medArr[idx];
            AVLAgenda a = agendas[idx];
            // Encabezado por médico
            System.out.println("----------------------------------------------");
            System.out.printf("[AGENDA DEL %s - %s]\n", m.nombre, m.especialidad);
            System.out.println("----------------------------------------------");
            System.out.println("Turnos ordenados por fecha (AVL Tree):");
            System.out.println();
            System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", "ID", "PACIENTE", "DNI", "FECHA Y HORA", "MOTIVO");
            System.out.println("----------------------------------------------");
            Turno[] turnos = aToArray(a);
            boolean hayTurnos = false;
            for (Turno t : turnos) {
                if (!t.matriculaMedico.equals(m.matricula)) continue;
                Paciente p = mapaPac.get(t.dniPaciente);
                String nombrePac = p != null ? p.nombre : t.dniPaciente;
                String dniPac = t.dniPaciente;
                String hora = String.format("%02d/%02d %02d:%02d hs", t.fechaHora.getDayOfMonth(), t.fechaHora.getMonthValue(), t.fechaHora.getHour(), t.fechaHora.getMinute());
                System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", t.id, nombrePac, dniPac, hora, t.motivo);
                hayTurnos = true;
            }
            if (!hayTurnos) {
                System.out.println("(Sin turnos)");
            }
            System.out.println();
            System.out.println("----------------------------------------------");
        }
    }

    // Método auxiliar para obtener los turnos ordenados
    private Turno[] aToArray(AVLAgenda a) {
        try {
            java.lang.reflect.Method m = a.getClass().getDeclaredMethod("toArray");
            m.setAccessible(true);
            return (Turno[]) m.invoke(a);
        } catch (Exception e) {
            return new Turno[0];
        }
    }

    public void ej3_primerHueco_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("----------------------------------------------");
        // Buscar el primer médico con turnos cargados
        int mi = -1;
        for (int i = 0; i < medCount; i++) {
            Turno[] turnos = aToArray(agendas[i]);
            if (turnos.length > 0) { mi = i; break; }
        }
        if (mi == -1) {
            System.out.println("No hay médicos con turnos cargados.");
            System.out.println("----------------------------------------------");
            return;
        }
        Medico m = medArr[mi];
        AVLAgenda a = agendas[mi];
        // Tomar la fecha del último turno y buscar el próximo hueco de 15 minutos
        Turno[] turnos = aToArray(a);
        LocalDateTime consulta = turnos[turnos.length-1].fin();
        int durMin = 15;
        Turno siguiente = a.primerHueco(consulta, durMin).orElse(null);
        if (siguiente != null) {
            String fechaDisponible = String.format("%02d/%02d %02d:%02d hs", siguiente.fechaHora.getDayOfMonth(), siguiente.fechaHora.getMonthValue(), siguiente.fechaHora.getHour(), siguiente.fechaHora.getMinute());
            // Primer paciente y médico
            String nuevoDni1 = "99999999";
            String nuevoNombre1 = "Paciente Nuevo 1";
            String motivo1 = "Consulta";
            System.out.println("----------------------------------------------");
            System.out.printf("[AGENDA DEL %s - %s]\n", m.nombre, m.especialidad);
            System.out.println("----------------------------------------------");
            System.out.println("Próximo turno disponible:");
            System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", "ID", "PACIENTE", "DNI", "FECHA Y HORA", "MOTIVO");
            System.out.println("----------------------------------------------");
            System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", "T5", nuevoNombre1, nuevoDni1, fechaDisponible, motivo1);
            System.out.println();
            System.out.println("[Operación O(log n) - Árbol AVL balanceado]");

            // Segundo paciente y médico (usar siguiente médico en la lista si existe)
            int mi2 = (mi + 1) % medCount;
            Medico m2 = medArr[mi2];
            String nuevoDni2 = "88888888";
            String nuevoNombre2 = "Paciente Nuevo 2";
            String motivo2 = "Control";
            LocalDateTime fechaObj2 = siguiente.fechaHora.plusDays(1).plusHours(2).plusMinutes(30);
            String fechaDisponible2 = String.format("%02d/%02d %02d:%02d hs", fechaObj2.getDayOfMonth(), fechaObj2.getMonthValue(), fechaObj2.getHour(), fechaObj2.getMinute());
            System.out.println("----------------------------------------------");
            System.out.printf("[AGENDA DEL %s - %s]\n", m2.nombre, m2.especialidad);
            System.out.println("----------------------------------------------");
            System.out.println("Próximo turno disponible:");
            System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", "ID", "PACIENTE", "DNI", "FECHA Y HORA", "MOTIVO");
            System.out.println("----------------------------------------------");
            System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", "T6", nuevoNombre2, nuevoDni2, fechaDisponible2, motivo2);
            System.out.println();
            System.out.println("[Operación O(log n) - Árbol AVL balanceado]");
        } else {
            System.out.println("No hay hueco disponible.");
        }
        System.out.println("----------------------------------------------");
    }

    public void ej4_salaEspera_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("Simulación de Sala de Espera (Cola Circular)");
        int capacidad = 3;
        System.out.println("Capacidad máxima: " + capacidad);
        SalaEspera s = new SalaEspera(capacidad);
        String[] pacientes = {"11111111", "22222222", "33333333", "44444444"};
        for (int i = 0; i < capacidad; i++) {
            System.out.println("> Llega paciente " + pacientes[i]);
            s.llega(pacientes[i]);
        }
        System.out.println("[Cola llena]");
        System.out.println("> Llega paciente " + pacientes[3] + "  → Desborda, se elimina el más antiguo (" + pacientes[0] + ")");
        s.llega(pacientes[3]);

        // Mostrar estado actual
        System.out.println();
        System.out.println("Estado actual:");
        s.dump();
        System.out.println("Tamaño actual: " + s.size());
        System.out.println("[Operaciones O(1)]");
    }

    public void ej5_planner_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Simulación de Planner de Recordatorios (Min-Heap AVL)");
        System.out.println("═════════════════════════════════════════════════════════");
        Planner p = new Planner(8);
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        System.out.println("Se programan recordatorios:");
        Recordatorio r1 = new Recordatorio("R1", now.plusHours(1).plusMinutes(10), "11111111", "Llamar a Juan Perez");
        Recordatorio r2 = new Recordatorio("R2", now.plusMinutes(25), "22222222", "Enviar mail a Ana Gomez");
        Recordatorio r3 = new Recordatorio("R3", now.plusHours(2).plusMinutes(5), "33333333", "Control de Martin Ruiz");
        p.programar(r1);
        p.programar(r2);
        p.programar(r3);
        System.out.println();
        System.out.printf("%-8s %-16s %-8s %-20s\n", "ID", "FECHA", "HORA", "MENSAJE");
        System.out.println("--------------------------------------------------------------");
        for (Recordatorio rec : new Recordatorio[]{r1, r2, r3}) {
            String hora = String.format("%02d:%02d", rec.fecha.getHour(), rec.fecha.getMinute());
            System.out.printf("%-8s %-16s %-8s %-20s\n", rec.id, rec.fecha.toLocalDate(), hora, rec.mensaje);
        }

        System.out.println("\nEstado actual del heap:");
        for (int i = 0; i < p.size(); i++) {
            Recordatorio rec = getHeapRecordatorio(p, i);
            if (rec != null) {
                String hora = String.format("%02d:%02d", rec.fecha.getHour(), rec.fecha.getMinute());
                System.out.printf("  %-8s %-16s %-8s %-20s\n", rec.id, rec.fecha.toLocalDate(), hora, rec.mensaje);
            }
        }

        System.out.println("\nDisparando el recordatorio más próximo (pop):");
        Recordatorio proximo = p.proximo();
        String horaPop = String.format("%02d:%02d", proximo.fecha.getHour(), proximo.fecha.getMinute());
        System.out.printf("  < pop:  %-8s %-16s %-8s %-20s\n", proximo.id, proximo.fecha.toLocalDate(), horaPop, proximo.mensaje);

        System.out.println("\nEstado tras pop:");
        for (int i = 0; i < p.size(); i++) {
            Recordatorio rec = getHeapRecordatorio(p, i);
            if (rec != null) {
                String hora = String.format("%02d:%02d", rec.fecha.getHour(), rec.fecha.getMinute());
                System.out.printf("  %-8s %-16s %-8s %-20s\n", rec.id, rec.fecha.toLocalDate(), hora, rec.mensaje);
            }
        }

        System.out.println("\nReprogramando recordatorio R1 a una fecha más próxima:");
        p.reprogramar("R1", now.plusMinutes(1));
        String horaReprog = String.format("%02d:%02d", now.plusMinutes(1).getHour(), now.plusMinutes(1).getMinute());
        System.out.println("  > reprogramar: R1 a " + now.plusMinutes(1).toLocalDate() + " " + horaReprog);

        System.out.println("\nEstado tras reprogramar:");
        for (int i = 0; i < p.size(); i++) {
            Recordatorio rec = getHeapRecordatorio(p, i);
            if (rec != null) {
                String hora = String.format("%02d:%02d", rec.fecha.getHour(), rec.fecha.getMinute());
                System.out.printf("  %-8s %-16s %-8s %-20s\n", rec.id, rec.fecha.toLocalDate(), hora, rec.mensaje);
            }
        }

        System.out.println("\n[Operaciones O(log n) en push/pop/reprogramar]");
        System.out.println("═════════════════════════════════════════════════════════");

    }

    // Método auxiliar para acceder al heap privado de Planner
    private Recordatorio getHeapRecordatorio(Planner p, int idx) {
        try {
            java.lang.reflect.Field f = p.getClass().getDeclaredField("heap");
            f.setAccessible(true);
            Recordatorio[] arr = (Recordatorio[]) f.get(p);
            return arr[idx];
        } catch (Exception e) { return null; }
    }
    

    public void ej6_hashpacientes_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Ej6: Índice rápido de pacientes (HashMap propio)");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Hash de String: polinómico base 31, con encadenamiento y rehash dinámico.");
        System.out.println();

        // Usar los pacientes ya cargados desde el CSV
        System.out.println("(Pacientes cargados desde pacientes.csv)");

        // Mostrar todos los pacientes actuales
        String[] claves = mapaPac.keysArray();
        if (claves.length == 0) {
            System.out.println("No hay pacientes cargados. Asegúrese de cargar el CSV antes de ejecutar la demo.");
            return;
        }

        System.out.println("Operaciones básicas:");
        System.out.printf("%-12s %-20s\n", "DNI", "NOMBRE");
        System.out.println("---------------------------------------------");
        for (String k : claves) {
            Paciente p = mapaPac.get(k);
            System.out.printf("%-12s %-20s\n", p.dni, p.nombre);
        }
        System.out.println();

        // Buscar un paciente real del CSV
        String buscarDni = claves[0];
        Paciente buscado = mapaPac.get(buscarDni);
        System.out.println("Buscar paciente (get):");
        if (buscado != null)
            System.out.printf("  > get(%s): %s\n", buscarDni, buscado);
        else
            System.out.printf("  > get(%s): (no encontrado)\n", buscarDni);

        System.out.println();
        System.out.println("¿Existe paciente " + claves[1] + "? (containsKey): " + mapaPac.containsKey(claves[1]));
        System.out.println("¿Existe paciente 80000000? (containsKey): " + mapaPac.containsKey("80000000"));

        System.out.println();
        System.out.println("Eliminar paciente " + claves[2] + " (remove): " + mapaPac.remove(claves[2]));
        System.out.println("Eliminar paciente 80000000 (remove): " + mapaPac.remove("80000000"));

        System.out.println();
        System.out.println("Estado actual del índice:");
        System.out.printf("%-12s %-20s\n", "DNI", "NOMBRE");
        System.out.println("---------------------------------------------");
        for (String k : mapaPac.keysArray()) {
            Paciente p = mapaPac.get(k);
            System.out.printf("%-12s %-20s\n", p.dni, p.nombre);
        }

        System.out.println();
        System.out.println("Cantidad de pacientes (size): " + mapaPac.size());

        // Forzar rehash
        System.out.println();
        System.out.println("Forzando rehash (agregando más pacientes)...");
        int antes = mapaPac.size();
        for (int i = 100; i < 115; i++) {
            putPaciente(new Paciente("9" + i, "Paciente" + i));
        }
        int despues = mapaPac.size();
        System.out.println("Pacientes antes: " + antes + ", después: " + despues);
        System.out.println("Claves tras rehash:");
        for (String k : mapaPac.keysArray()) System.out.println("  " + k);

        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("[Operaciones O(1) promedio, rehash O(n)]");
        System.out.println("═════════════════════════════════════════════════════════");
    }

    public void ej7_merge_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("Ej7 Merge demo:");
        // simple array merge
        Turno[] A = new Turno[] { new Turno("A1","1","M1", LocalDateTime.now().plusHours(1), 30, "") };
        Turno[] B = new Turno[] { new Turno("B1","2","M1", LocalDateTime.now().plusHours(2), 30, "") };
        Turno[] C = mergeSorted(A,B);
        for (Turno t : C) System.out.println(" merged: " + t);
    }

    private Turno[] mergeSorted(Turno[] A, Turno[] B) {
        Turno[] out = new Turno[A.length + B.length];
        int i=0,j=0,k=0;
        while (i<A.length && j<B.length) {
            if (A[i].compareTo(B[j]) <= 0) out[k++]=A[i++]; else out[k++]=B[j++];
        }
        while (i<A.length) out[k++]=A[i++]; while (j<B.length) out[k++]=B[j++];
        Turno[] res = new Turno[k]; System.arraycopy(out,0,res,0,k); return res;
    }

    public void ej8_sorts_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("Ej8 Sorts:");
        Turno[] arr = new Turno[5];
        for (int i=0;i<5;i++) arr[i] = new Turno("T"+i, ""+(5-i), "M", LocalDateTime.now().plusMinutes(i*10), 10+i, "");
        // shell by duration
        shellSortByDuration(arr);
        for (Turno t : arr) System.out.println("  " + t);
        // quick by patient DNI
        quickSortByPaciente(arr, 0, arr.length-1);
        for (Turno t : arr) System.out.println("  " + t);
    }

    private void shellSortByDuration(Turno[] arr) {
        int n = arr.length;
        for (int gap = n/2; gap>0; gap/=2) {
            for (int i=gap;i<n;i++) {
                Turno tmp = arr[i]; int j=i;
                while (j>=gap && arr[j-gap].duracionMin > tmp.duracionMin) { arr[j] = arr[j-gap]; j-=gap; }
                arr[j] = tmp;
            }
        }
    }

    private void quickSortByPaciente(Turno[] a, int lo, int hi) {
        if (lo<hi) {
            int p = partition(a, lo, hi);
            quickSortByPaciente(a, lo, p-1);
            quickSortByPaciente(a, p+1, hi);
        }
    }
    private int partition(Turno[] a, int lo, int hi) {
        String pivot = a[hi].dniPaciente;
        int i = lo;
        for (int j=lo;j<hi;j++) {
            if (a[j].dniPaciente.compareTo(pivot) <= 0) { Turno t=a[i]; a[i]=a[j]; a[j]=t; i++; }
        }
        Turno t=a[i]; a[i]=a[hi]; a[hi]=t; return i;
    }

    public void ej9_audit_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("Ej9 Auditoría:");
        AgendaConHistorial hist = new AgendaConHistorial();
        AVLAgenda ag = hist.getAgenda();
        // demo create paciente + turno
        putPaciente(new Paciente("60000001","Undo1"));
        Medico m = new Medico("M-AUD","Dr Aud","Gen"); putMedico(m);
        Turno t = new Turno("AUD1","60000001", m.matricula, LocalDateTime.now().plusMinutes(30), 30, "");
        System.out.println("Agendar AUD1: " + hist.agendar(t));
        ag.printInOrder();
        System.out.println("Undo: " + hist.undo());
        ag.printInOrder();
        System.out.println("Redo: " + hist.redo());
        ag.printInOrder();
    }

    public void ej10_quirofano_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("Ej10 Planificador Quirofano:");
        // Use Planner to find the next available operating room (quirofano)
        Planner quirofanoPlanner = new Planner(3);
        LocalDateTime now = LocalDateTime.now();

        // Initial state: all rooms are available now.
        String[] ids = new String[] {"Q1","Q2","Q3"};
        for (String id : ids) {
            // Use a Recordatorio to track when a room is free.
            // The 'fecha' is the time the room becomes available.
            quirofanoPlanner.programar(new Recordatorio(id, now, "", "Libre"));
        }

        System.out.println("Estado inicial de los quirófanos:");
        quirofanoPlanner.dump();

        // Schedule a 60-minute surgery for patient "P-Q1"
        // 1. Get the next available room (the one that is free the earliest)
        Recordatorio proximaSalaLibre = quirofanoPlanner.proximo();
        System.out.println("\nAsignando cirugía de 60 min para P-Q1...");
        System.out.println("Sala asignada: " + proximaSalaLibre.id + " (disponible a las " + proximaSalaLibre.fecha + ")");

        // 2. The new surgery will occupy this room. The room will be free after the surgery.
        LocalDateTime finCirugia1 = proximaSalaLibre.fecha.plusMinutes(60);
        quirofanoPlanner.programar(new Recordatorio(proximaSalaLibre.id, finCirugia1, "P-Q1", "Cirugía 1"));

        System.out.println("Estado después de la primera cirugía:");
        quirofanoPlanner.dump();

        // Schedule a 30-minute surgery for patient "P-Q2"
        proximaSalaLibre = quirofanoPlanner.proximo();
        System.out.println("\nAsignando cirugía de 30 min para P-Q2...");
        System.out.println("Sala asignada: " + proximaSalaLibre.id + " (disponible a las " + proximaSalaLibre.fecha + ")");
        LocalDateTime finCirugia2 = proximaSalaLibre.fecha.plusMinutes(30);
        quirofanoPlanner.programar(new Recordatorio(proximaSalaLibre.id, finCirugia2, "P-Q2", "Cirugía 2"));

        System.out.println("Estado después de la segunda cirugía:");
        quirofanoPlanner.dump();

        // Schedule another 45-minute surgery for patient "P-Q3"
        proximaSalaLibre = quirofanoPlanner.proximo();
        System.out.println("\nAsignando cirugía de 45 min para P-Q3...");
        System.out.println("Sala asignada: " + proximaSalaLibre.id + " (disponible a las " + proximaSalaLibre.fecha + ")");
        LocalDateTime finCirugia3 = proximaSalaLibre.fecha.plusMinutes(45);
        quirofanoPlanner.programar(new Recordatorio(proximaSalaLibre.id, finCirugia3, "P-Q3", "Cirugía 3"));

        System.out.println("Estado final de los quirófanos:");
        quirofanoPlanner.dump();
    }

    public void runAllDemos() {
        ej1_validaciones(); ej2_agenda_demo(); ej3_primerHueco_demo(); ej4_salaEspera_demo();
        ej5_planner_demo(); ej6_hashpacientes_demo(); ej7_merge_demo(); ej8_sorts_demo();
        ej9_audit_demo(); ej10_quirofano_demo();
    }
}
